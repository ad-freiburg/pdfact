package pdfact.pipes.parse.stream.pdfbox.operators.graphic;

import static pdfact.PdfActCoreSettings.FLOATING_NUMBER_PRECISION;

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

import pdfact.model.Color;
import pdfact.model.Figure;
import pdfact.model.Color.ColorFactory;
import pdfact.model.Figure.FigureFactory;
import pdfact.model.Point.PointFactory;
import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.model.Point;
import pdfact.model.Position;
import pdfact.model.Position.PositionFactory;
import pdfact.model.Shape;
import pdfact.model.Shape.ShapeFactory;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.util.ColorUtils;
import pdfact.util.MathUtils;

/**
 * Do: Invoke a named xobject.
 * 
 * @author Claudius Korzen
 */
public class Invoke extends OperatorProcessor {
  /**
   * The factory to create instances of {@link Figure}.
   */
  protected FigureFactory figureFactory;

  /**
   * The factory to create instances of {@link Color}.
   */
  protected ColorFactory colorFactory;

  /**
   * The factory to create instances of {@link Shape}.
   */
  protected ShapeFactory shapeFactory;

  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  /**
   * The factory to create instances of {@link Position}.
   */
  protected PositionFactory positionFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new OperatorProcessor to process the operation "Invoke".
   * 
   * @param figureFactory
   *        The factory to create instances of {@link Figure}.
   * @param colorFactory
   *        The factory to create instances of {@link Color}.
   * @param shapeFactory
   *        The factory to create instances of {@link Shape}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param positionactory
   *        The factory to create instances of {@link Position}.
   */
  @Inject
  public Invoke(FigureFactory figureFactory, ColorFactory colorFactory,
      ShapeFactory shapeFactory, PointFactory pointFactory,
      PositionFactory positionactory) {
    this.figureFactory = figureFactory;
    this.colorFactory = colorFactory;
    this.shapeFactory = shapeFactory;
    this.pointFactory = pointFactory;
    this.positionFactory = positionactory;
  }

  // ==========================================================================

  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
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

      // TODO: Check if ur and ll are indeeed ur and ll.
      float minX = ctm.getTranslateX();
      float minY = ctm.getTranslateY();
      float maxX = minX + at.getScaleX() * imageWidth;
      float maxY = minY + at.getScaleY() * imageHeight;

      // Round the values.
      minX = MathUtils.round(minX, FLOATING_NUMBER_PRECISION);
      minY = MathUtils.round(minY, FLOATING_NUMBER_PRECISION);
      maxX = MathUtils.round(maxX, FLOATING_NUMBER_PRECISION);
      maxY = MathUtils.round(maxY, FLOATING_NUMBER_PRECISION);

      Point ll = this.pointFactory.create(minX, minY);
      Point ur = this.pointFactory.create(maxX, maxY);
      Position position = this.positionFactory.create(page, ll, ur);

      // If the image consists of only one color, consider it as a shape.
      // TODO: Manage the colors.
      float[] exclusiveColor = ColorUtils.getExclusiveColor(image.getImage());

      if (exclusiveColor != null) {
        Color color = this.colorFactory.create();
        color.setRGB(exclusiveColor);
        Shape shape = this.shapeFactory.create();
        shape.setPosition(position);
        shape.setColor(color);
        this.engine.handlePdfShape(pdf, page, shape);
      } else {
        Figure figure = this.figureFactory.create();
        figure.setPosition(position);
        this.engine.handlePdfFigure(pdf, page, figure);
      }
    }
  }

  @Override
  public String getName() {
    return "Do";
  }
}
