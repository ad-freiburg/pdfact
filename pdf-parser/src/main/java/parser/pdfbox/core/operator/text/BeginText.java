package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.Matrix;

import parser.pdfbox.core.operator.OperatorProcessor;


/**
 * BT: Begin a text object, initializing the text matrix, Tm, and the text 
 * line matrix to the identity matrix.
 *
 * @author Claudius Korzen
 */
public class BeginText extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments) 
      throws IOException {
    context.setTextMatrix(new Matrix());
    context.setTextLineMatrix(new Matrix());
    // context.beginText();
  }

  @Override
  public String getName() {
    return "BT";
  }
}
