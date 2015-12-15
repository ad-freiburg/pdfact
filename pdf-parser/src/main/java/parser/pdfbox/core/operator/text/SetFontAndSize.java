package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.font.PDFont;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Tf: Set the text font to font and the text font size to size. font shall be
 * the name of a font resource in the Font subdictionary of the current
 * resource dictionary; size shall be a number representing a scale factor. 
 * 
 * @author Claudius Korzen
 */
public class SetFontAndSize extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.size() < 2) {
      throw new MissingOperandException(operator, arguments);
    }
     
    COSName fontName = (COSName) arguments.get(0);
    float fontSize = ((COSNumber) arguments.get(1)).floatValue();
    context.getGraphicsState().getTextState().setFontSize(fontSize);
    PDFont font = context.getResources().getFont(fontName);    
    context.getGraphicsState().getTextState().setFont(font);
  }

  @Override
  public String getName() {
    return "Tf";
  }
}
