package parser.pdfbox.core.operator.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.apache.pdfbox.util.Matrix;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimplePoint;
import de.freiburg.iif.model.simple.SimpleRectangle;
import parser.pdfbox.core.operator.OperatorProcessor;
import parser.pdfbox.model.PdfBoxColor;
import parser.pdfbox.model.PdfBoxFigure;
import parser.pdfbox.model.PdfBoxShape;

/**
 * Implementation of content stream operator for page drawer.
 * 
 * @author Claudius Korzen
 */
public class BeginInlineImage extends OperatorProcessor {

  /**
   * process : BI : begin inline image.
   * 
   * @param operator
   *          The operator that is being executed.
   * @param arguments
   *          List
   * @throws IOException
   *           If there is an error displaying the inline image.
   */
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    Matrix ctm = context.getCurrentTransformationMatrix();
    COSDictionary params = operator.getImageParameters();
    
    int width = params.getInt(COSName.W, COSName.WIDTH, -1);
    int height = params.getInt(COSName.H, COSName.HEIGHT, -1);
    
    // TODO: use transform().

    float minX = ctm.getTranslateX();
    float maxX = minX + (width * ctm.getScaleX());
    float minY = ctm.getTranslateY();
    float maxY = minY + (height * ctm.getScaleY());

    // Type3 streams may contain BI operands, but we don't wan't to consider
    // those.
    if (!context.isType3Stream()) {
//      Rectangle boundBox = new SimpleRectangle(minX, minY, maxX, maxY);
      
      Rectangle boundBox = SimpleRectangle.from2Vertices(
          new SimplePoint(minX, minY), 
          new SimplePoint(maxX, maxY));
      
      PDImage image = new PDInlineImage(operator.getImageParameters(),
          operator.getImageData(), context.getResources());
      
      PdfBoxColor exclusiveColor = getExclusiveColor(image.getImage());
      
      if (exclusiveColor != null) {
        PdfBoxShape shape = new PdfBoxShape(context.getCurrentPage());
        shape.setRectangle(boundBox);
        shape.setColor(exclusiveColor);
        context.showShape(shape);
      } else {
        PdfBoxFigure figure = new PdfBoxFigure(context.getCurrentPage());
        figure.setRectangle(boundBox);
        context.showFigure(figure);
      }
    }
  }

  @Override
  public String getName() {
    return "BI";
  }

  /**
   * Checks if the given image consists only of a single color and returns the
   * color if so. Returns null if there a at least two different colors.
   */
  protected static PdfBoxColor getExclusiveColor(BufferedImage image)
    throws IOException {
    if (image == null) {
      return null;
    }

    int lastRgb = Integer.MAX_VALUE;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        int rgb = image.getRGB(i, j);
        if (lastRgb != Integer.MAX_VALUE && lastRgb != rgb) {
          return null;
        }
        lastRgb = rgb;
      }
    }

    if (lastRgb == Integer.MAX_VALUE) {
      return null;
    }
    return PdfBoxColor.create(toRGBArray(lastRgb));
  }

  public static float[] toRGBArray(int pixel) {
    float alpha = (pixel >> 24) & 0xff;
    float red = ((pixel >> 16) & 0xff) / 255f;
    float green = ((pixel >> 8) & 0xff) / 255f;
    float blue = ((pixel) & 0xff) / 255f;
    return new float[] { red, green, blue, alpha };
  }
}