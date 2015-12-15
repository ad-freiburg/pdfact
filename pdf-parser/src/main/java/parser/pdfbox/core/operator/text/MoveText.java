package parser.pdfbox.core.operator.text;

import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.Matrix;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Td: Move to the start of the next line, offset from the start of the current
 * line by (tx, ty). tx and ty shall denote numbers expressed in unscaled text 
 * space units.
 * 
 * @author Claudius Korzen
 */
public class MoveText extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws MissingOperandException {
    if (arguments.size() < 2) {
      throw new MissingOperandException(operator, arguments);
    }
    Matrix tlm = context.getTextLineMatrix();
    if (tlm == null) {
      return;
    }

    COSNumber tx = (COSNumber) arguments.get(0);
    COSNumber ty = (COSNumber) arguments.get(1);

    Matrix matrix = new Matrix(1, 0, 0, 1, tx.floatValue(), ty.floatValue());
    tlm.concatenate(matrix);
    context.setTextMatrix(tlm.clone());
  }

  @Override
  public String getName() {
    return "Td";
  }
}
