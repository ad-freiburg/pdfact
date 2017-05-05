package icecite.parser.pdfbox.core.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.Matrix;

import icecite.parser.pdfbox.core.operators.OperatorProcessor;

/**
 * cm: Modify the current transformation matrix (CTM) by concatenating the
 * specified matrix. Although the operands specify a matrix, they shall be
 * written as six separate numbers, not as an array.
 * 
 * @author Claudius Korzen.
 */
public class ModifyCurrentTransformationMatrix extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 6) {
      throw new MissingOperandException(op, args);
    }

    // Concatenate matrix to current transformation matrix
    COSNumber a = (COSNumber) args.get(0);
    COSNumber b = (COSNumber) args.get(1);
    COSNumber c = (COSNumber) args.get(2);
    COSNumber d = (COSNumber) args.get(3);
    COSNumber e = (COSNumber) args.get(4);
    COSNumber f = (COSNumber) args.get(5);

    Matrix matrix = new Matrix(a.floatValue(), b.floatValue(), c.floatValue(),
        d.floatValue(), e.floatValue(), f.floatValue());

    this.engine.getCurrentTransformationMatrix().concatenate(matrix);
  }

  @Override
  public String getName() {
    return "cm";
  }
}
