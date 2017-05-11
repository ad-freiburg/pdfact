package icecite.parser.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.apache.pdfbox.util.Matrix;

import com.google.inject.Inject;

import icecite.models.PdfColor;
import icecite.models.PdfFigure;
import icecite.models.PdfFigure.PdfFigureFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShape.PdfShapeFactory;
import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;
import icecite.utils.image.ImageUtils;

/**
 * BI: Begin inline image.
 * 
 * @author Claudius Korzen
 */
public class BeginInlineImage extends OperatorProcessor {
  /**
   * The factory to create instances of {@link PdfFigure}.
   */
  @Inject
  protected PdfFigureFactory pdfFigureFactory;

  /**
   * The factory to create instances of {@link PdfShape}.
   */
  @Inject
  protected PdfShapeFactory pdfShapeFactory;

  // ==========================================================================

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    Matrix ctm = this.engine.getCurrentTransformationMatrix();
    COSDictionary params = op.getImageParameters();

    int width = params.getInt(COSName.W, COSName.WIDTH, -1);
    int height = params.getInt(COSName.H, COSName.HEIGHT, -1);

    // TODO: use engine.transform().

    float minX = ctm.getTranslateX();
    float maxX = minX + (width * ctm.getScaleX());
    float minY = ctm.getTranslateY();
    float maxY = minY + (height * ctm.getScaleY());

    // Type3 streams may contain BI operands, but we don't want to consider
    // those.
    if (!this.engine.isType3Stream()) {
      Rectangle boundBox = PlainRectangle.from2Vertices(
          new PlainPoint(minX, minY), new PlainPoint(maxX, maxY));

      PDImage image = new PDInlineImage(op.getImageParameters(),
          op.getImageData(), this.engine.getResources());

      // If the image consists of only one color, consider it as a shape.
      PdfColor exclusiveColor = ImageUtils.getExclusiveColor(image.getImage());
      if (exclusiveColor != null) {
        PdfShape shape = this.pdfShapeFactory.create();
        shape.setBoundingBox(boundBox);
        shape.setColor(exclusiveColor);
        this.engine.handlePdfShape(shape);
      } else {
        PdfFigure figure = this.pdfFigureFactory.create();
        figure.setBoundingBox(boundBox);
        this.engine.handlePdfFigure(figure);
      }
    }
  }

  @Override
  public String getName() {
    return "BI";
  }
}