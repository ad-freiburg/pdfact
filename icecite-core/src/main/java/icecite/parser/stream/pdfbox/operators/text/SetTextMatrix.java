package icecite.parser.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.Matrix;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;

/**
 * Tm: Set text matrix and the text line matrix.
 * 
 * @author Claudius Korzen
 */
public class SetTextMatrix extends OperatorProcessor {
  @Override
  public void process(Operator op, List<COSBase> args) throws IOException {
    if (args.size() < 6) {
      throw new MissingOperandException(op, args);
    }

    COSNumber a = (COSNumber) args.get(0);
    COSNumber b = (COSNumber) args.get(1);
    COSNumber c = (COSNumber) args.get(2);
    COSNumber d = (COSNumber) args.get(3);
    COSNumber e = (COSNumber) args.get(4);
    COSNumber f = (COSNumber) args.get(5);

    // Set both matrices to
    // [ a b 0
    // c d 0
    // e f 1 ]

    Matrix matrix = new Matrix(a.floatValue(), b.floatValue(), c.floatValue(),
        d.floatValue(), e.floatValue(), f.floatValue());

    this.engine.setTextMatrix(matrix);
    this.engine.setTextLineMatrix(matrix.clone());
  }

  @Override
  public String getName() {
    return "Tm";
  }
}
