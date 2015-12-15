package parser.pdfbox.core.operator.graphics;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.util.Matrix;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import parser.pdfbox.core.operator.OperatorProcessor;
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
      Rectangle boundBox = new SimpleRectangle(minX, minY, maxX, maxY);
      
      // TODO: Implement more robust way to distinguish figures and shapes.
      if (boundBox.getHeight() < 5 || boundBox.getWidth() < 5) {
        PdfBoxShape shape = new PdfBoxShape(context.getCurrentPage());
        shape.setRectangle(boundBox);
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
}