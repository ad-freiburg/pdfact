package pdfact.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import org.apache.pdfbox.util.Matrix;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * TJ: Show one or more text strings, allowing individual glyph positioning.
 * Each element of array shall be either a string or a number. If the element is
 * a string, this operator shall show the string. If it is a number, the
 * operator shall adjust the text position by that amount; that is, it shall
 * translate the text matrix, Tm. The number shall be expressed in thousandths
 * of a unit of text space (see 9.4.4, "Text Space Details"). This amount shall
 * be subtracted from the current horizontal or vertical coordinate, depending
 * on the writing mode. In the default coordinate system, a positive adjustment
 * has the effect of moving the next glyph painted either to the left or down by
 * the given amount. Figure 46 shows an example of the effect of passing offsets
 * to TJ.
 * 
 * @author Claudius Korzen
 */
public class ShowTextWithIndividualGlyphPositioning extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    if (args.isEmpty()) {
      throw new MissingOperandException(op, args);
    }

    PDTextState textState = this.engine.getGraphicsState().getTextState();
    float fontSize = textState.getFontSize();
    float horizontalScaling = textState.getHorizontalScaling() / 100f;
    boolean isVertical = textState.getFont().isVertical();

    COSArray array = (COSArray) args.get(0);
    for (COSBase obj : array) {
      if (obj instanceof COSNumber) {
        float tj = ((COSNumber) obj).floatValue();

        // calculate the combined displacements
        float tx, ty;
        if (isVertical) {
          tx = 0;
          ty = -tj / 1000 * fontSize;
        } else {
          tx = -tj / 1000 * fontSize * horizontalScaling;
          ty = 0;
        }

        Matrix translate = Matrix.getTranslateInstance(tx, ty);
        this.engine.getTextMatrix().concatenate(translate);
      } else
        if (obj instanceof COSString) {
          List<COSBase> otherArgs = new ArrayList<COSBase>();
          otherArgs.add(obj);
          this.engine.processOperator(pdf, page, "Tj", otherArgs);
        } else {
          throw new IOException(
              "Unknown type in array for TJ operation:" + obj);
        }
    }
  }

  @Override
  public String getName() {
    return "TJ";
  }
}
