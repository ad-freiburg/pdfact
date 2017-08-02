package icecite.parse.stream.pdfbox.operators.graphic;

import static icecite.parse.PdfParserSettings.FLOATING_NUMBER_PRECISION;

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
import icecite.models.PdfColor.PdfColorFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfFigure.PdfFigureFactory;
import icecite.models.PdfPage;
import icecite.models.PdfPosition;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShape.PdfShapeFactory;
import icecite.parse.stream.pdfbox.operators.OperatorProcessor;
import icecite.utils.color.ColorUtils;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Point.PointFactory;
import icecite.utils.math.MathUtils;

/**
 * BI: Begin inline image.
 * 
 * @author Claudius Korzen
 */
public class BeginInlineImage extends OperatorProcessor {
  /**
   * The factory to create instances of {@link PdfFigure}.
   */
  protected PdfFigureFactory figureFactory;

  /**
   * The factory to create instances of {@link PdfColor}.
   */
  protected PdfColorFactory colorFactory;

  /**
   * The factory to create instances of {@link PdfShape}.
   */
  protected PdfShapeFactory shapeFactory;

  /**
   * The factory to create instances of {@link Point}.
   */
  protected PointFactory pointFactory;

  /**
   * The factory to create instances of {@link PdfPosition}.
   */
  protected PdfPositionFactory positionFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new OperatorProcessor to process the operation
   * "BeginInlineImage".
   * 
   * @param figureFactory
   *        The factory to create instances of {@link PdfFigure}.
   * @param colorFactory
   *        The factory to create instances of {@link PdfColor}.
   * @param shapeFactory
   *        The factory to create instances of {@link PdfShape}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param positionFactory
   *        The factory to create instances of {@link PdfPosition}.
   */
  @Inject
  public BeginInlineImage(PdfFigureFactory figureFactory,
      PdfColorFactory colorFactory, PdfShapeFactory shapeFactory,
      PointFactory pointFactory, PdfPositionFactory positionFactory) {
    this.figureFactory = figureFactory;
    this.colorFactory = colorFactory;
    this.shapeFactory = shapeFactory;
    this.pointFactory = pointFactory;
    this.positionFactory = positionFactory;
  }

  // ==========================================================================

  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    PdfPage pdfPage = this.engine.getCurrentPdfPage();

    Matrix ctm = this.engine.getCurrentTransformationMatrix();
    COSDictionary params = op.getImageParameters();

    int width = params.getInt(COSName.W, COSName.WIDTH, -1);
    int height = params.getInt(COSName.H, COSName.HEIGHT, -1);

    // TODO: use engine.transform().
    float minX = ctm.getTranslateX();
    float maxX = minX + (width * ctm.getScaleX());
    float minY = ctm.getTranslateY();
    float maxY = minY + (height * ctm.getScaleY());

    // Round the values.
    minX = MathUtils.round(minX, FLOATING_NUMBER_PRECISION);
    minY = MathUtils.round(minY, FLOATING_NUMBER_PRECISION);
    maxX = MathUtils.round(maxX, FLOATING_NUMBER_PRECISION);
    maxY = MathUtils.round(maxY, FLOATING_NUMBER_PRECISION);

    // Type3 streams may contain BI operands, but we don't want to consider
    // those.
    if (!this.engine.isType3Stream()) {
      PDImage image = new PDInlineImage(op.getImageParameters(),
          op.getImageData(), this.engine.getResources());

      // If the image consists of only one color, consider it as a shape.
      // TODO: Manage the colors.
      float[] exclusiveColor = ColorUtils.getExclusiveColor(image.getImage());

      Point ll = this.pointFactory.create(minX, minY);
      Point ur = this.pointFactory.create(maxX, maxY);
      // TODO: Check if we have to check if ur is indeed the upper right.
      PdfPosition position = this.positionFactory.create(pdfPage, ll, ur);

      if (exclusiveColor != null) {
        PdfColor color = this.colorFactory.create();
        color.setRGB(exclusiveColor);
        PdfShape shape = this.shapeFactory.create();
        shape.setPosition(position);
        shape.setColor(color);
        this.engine.handlePdfShape(shape);
      } else {
        PdfFigure figure = this.figureFactory.create();
        figure.setPosition(position);
        this.engine.handlePdfFigure(figure);
      }
    }
  }

  @Override
  public String getName() {
    return "BI";
  }
}