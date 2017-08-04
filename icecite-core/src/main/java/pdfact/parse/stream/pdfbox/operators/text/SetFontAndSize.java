package pdfact.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.font.PDFont;

import pdfact.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * Tf: Set the text font to font and the text font size to size. font shall be
 * the name of a font resource in the Font sub-dictionary of the current
 * resource dictionary; size shall be a number representing a scale factor.
 * 
 * @author Claudius Korzen
 */
public class SetFontAndSize extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 2) {
      throw new MissingOperandException(op, args);
    }

    COSName fontName = (COSName) args.get(0);
    float fontSize = ((COSNumber) args.get(1)).floatValue();
    this.engine.getGraphicsState().getTextState().setFontSize(fontSize);
    // TODO: This needs a lot of time (> 400ms) when called the first time.
    // Reimplement it.
    PDFont font = this.engine.getResources().getFont(fontName);
    this.engine.getGraphicsState().getTextState().setFont(font);

  }

  @Override
  public String getName() {
    return "Tf";
  }
}
