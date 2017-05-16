package icecite.parser.stream.pdfbox.operators.graphic;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import com.google.inject.Inject;

import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfFigure.PdfFigureFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShape.PdfShapeFactory;
import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.color.ColorUtils;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;

/**
 * Do: Invoke a named xobject.
 * 
 * @author Claudius Korzen
 */
public class Invoke extends OperatorProcessor {
  /**
   * The factory to create instances of {@link PdfFigure}.
   */
  @Inject
  protected PdfFigureFactory pdfFigureFactory;

  /**
   * The factory to create instances of {@link PdfColor}.
   */
  @Inject
  protected PdfColorFactory pdfColorFactory;
  
  /**
   * The factory to create instances of {@link PdfShape}.
   */
  @Inject
  protected PdfShapeFactory pdfShapeFactory;

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    // Get the name of the PDXOject.
    COSName name = (COSName) args.get(0);

    // Get the PDXObject.
    PDXObject xobject = this.engine.getResources().getXObject(name);

    // if (xobject instanceof PDFormXObject) {
    // PDFormXObject form = (PDFormXObject) xobject;
    //
    // // if there is an optional form matrix, we have to map the form space to
    // // the user space
    // Matrix matrix = form.getMatrix();
    // if (matrix != null) {
    // Matrix xCTM = matrix.multiply(context.getCurrentTransformationMatrix());
    // context.getGraphicsState().setCurrentTransformationMatrix(xCTM);
    //
    // // Transform PDRectangle => SimpleRectangle.
    // PDRectangle rectangle = form.getBBox();
    // Rectangle boundingBox = new SimpleRectangle();
    //
    // boundingBox.setMinX(rectangle.getLowerLeftX());
    // boundingBox.setMinY(rectangle.getLowerLeftY());
    // boundingBox.setMaxX(rectangle.getUpperRightX());
    // boundingBox.setMaxY(rectangle.getUpperRightY());
    //
    // context.showForm(boundingBox);
    // }
    // // find some optional resources, instead of using the current resources
    // context.processStream(form);
    // } else
    if (xobject instanceof PDImageXObject) {
      PDImageXObject image = (PDImageXObject) xobject;

      int imageWidth = image.getWidth();
      int imageHeight = image.getHeight();

      Matrix ctm = this.engine.getCurrentTransformationMatrix().clone();
      AffineTransform ctmAT = ctm.createAffineTransform();
      ctmAT.scale(1f / imageWidth, 1f / imageHeight);
      Matrix at = new Matrix(ctmAT);

      Rectangle boundBox = PlainRectangle.from2Vertices(
          new PlainPoint(ctm.getTranslateX(), ctm.getTranslateY()),
          new PlainPoint(ctm.getTranslateX() + at.getScaleX() * imageWidth,
              ctm.getTranslateY() + at.getScaleY() * imageHeight));

      // Rectangle boundBox = new SimpleRectangle();
      // boundBox.setMinX(ctm.getTranslateX());
      // boundBox.setMinY(ctm.getTranslateY());
      // boundBox.setMaxX(ctm.getTranslateX() + at.getScaleX() * imageWidth);
      // boundBox.setMaxY(ctm.getTranslateY() + at.getScaleY() * imageHeight);

      // If the image consists of only one color, consider it as a shape.
      float[] exclusiveColor = ColorUtils.getExclusiveColor(image.getImage());
      if (exclusiveColor != null) {
        PdfColor color = this.pdfColorFactory.create();
        color.setRGB(exclusiveColor);
        PdfShape shape = this.pdfShapeFactory.create();
        shape.setBoundingBox(boundBox);
        shape.setColor(color);
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
    return "Do";
  }
}
