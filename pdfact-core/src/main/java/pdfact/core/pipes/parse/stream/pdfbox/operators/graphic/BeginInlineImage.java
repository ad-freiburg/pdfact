package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import static pdfact.core.PdfActCoreSettings.FLOATING_NUMBER_PRECISION;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.apache.pdfbox.util.Matrix;

import pdfact.core.model.Color;
import pdfact.core.model.Figure;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Shape;
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
   * The logger.
   */
  protected static Logger log = LogManager.getLogger(BeginInlineImage.class);

  // ==============================================================================================

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

      Point ll = new Point(minX, minY);
      Point ur = new Point(maxX, maxY);
      // TODO: Check if we have to check if ur is indeed the upper right.
      Position position = new Position(page, ll, ur);

      if (exclusiveColor != null) {
        Color color = new Color();
        color.setRGB(exclusiveColor);

        log.debug("The inline image consists only of the color " + color + ". "
            + "Considering it as a shape.");

        Shape shape = new Shape();
        shape.setPosition(position);
        shape.setColor(color);
        this.engine.handlePdfShape(pdf, page, shape);
      } else {
        Figure figure = new Figure();
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