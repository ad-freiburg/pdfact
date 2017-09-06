package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import static pdfact.core.PdfActCoreSettings.FLOATING_NUMBER_PRECISION;

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

import pdfact.core.model.Color;
import pdfact.core.model.Figure;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Shape;
import pdfact.core.model.Color.ColorFactory;
import pdfact.core.model.Figure.FigureFactory;
import pdfact.core.model.Point.PointFactory;
import pdfact.core.model.Position.PositionFactory;
import pdfact.core.model.Shape.ShapeFactory;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.core.pipes.parse.stream.pdfbox.utils.ColorUtils;
import pdfact.core.util.PdfActUtils;

/**
 * BI: Begin inline image.
 * 
 * @author Claudius Korzen
 */
public class BeginInlineImage extends OperatorProcessor {
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
   * Creates a new OperatorProcessor to process the operation
   * "BeginInlineImage".
   * 
   * @param figureFactory
   *        The factory to create instances of {@link Figure}.
   * @param colorFactory
   *        The factory to create instances of {@link Color}.
   * @param shapeFactory
   *        The factory to create instances of {@link Shape}.
   * @param pointFactory
   *        The factory to create instances of {@link Point}.
   * @param positionFactory
   *        The factory to create instances of {@link Position}.
   */
  @Inject
  public BeginInlineImage(FigureFactory figureFactory,
      ColorFactory colorFactory, ShapeFactory shapeFactory,
      PointFactory pointFactory, PositionFactory positionFactory) {
    this.figureFactory = figureFactory;
    this.colorFactory = colorFactory;
    this.shapeFactory = shapeFactory;
    this.pointFactory = pointFactory;
    this.positionFactory = positionFactory;
  }

  // ==========================================================================

  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
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
    minX = PdfActUtils.round(minX, FLOATING_NUMBER_PRECISION);
    minY = PdfActUtils.round(minY, FLOATING_NUMBER_PRECISION);
    maxX = PdfActUtils.round(maxX, FLOATING_NUMBER_PRECISION);
    maxY = PdfActUtils.round(maxY, FLOATING_NUMBER_PRECISION);

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
      Position position = this.positionFactory.create(page, ll, ur);

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
    return "BI";
  }
}