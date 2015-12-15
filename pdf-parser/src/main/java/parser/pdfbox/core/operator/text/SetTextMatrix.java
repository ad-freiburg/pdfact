package parser.pdfbox.core.operator.text;

import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.Matrix;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Tm: Set text matrix and the text line matrix.
 * 
 * @author Claudius Korzen
 */
public class SetTextMatrix extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws MissingOperandException {
    if (arguments.size() < 6) {
      throw new MissingOperandException(operator, arguments);
    }
    
    COSNumber a = (COSNumber) arguments.get(0);
    COSNumber b = (COSNumber) arguments.get(1);
    COSNumber c = (COSNumber) arguments.get(2);
    COSNumber d = (COSNumber) arguments.get(3);
    COSNumber e = (COSNumber) arguments.get(4);
    COSNumber f = (COSNumber) arguments.get(5);

    // Set both matrices to 
    // [ a b 0
    //   c d 0
    //   e f 1 ]
    
    Matrix matrix = new Matrix(a.floatValue(), b.floatValue(), 
        c.floatValue(), d.floatValue(), e.floatValue(), f.floatValue());

    context.setTextMatrix(matrix);
    context.setTextLineMatrix(matrix.clone());
  }

  @Override
  public String getName() {
    return "Tm";
  }
}
