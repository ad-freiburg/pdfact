package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.Matrix;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * Td: Move to the start of the next line, offset from the start of the current
 * line by (tx, ty). tx and ty shall denote numbers expressed in unscaled text
 * space units.
 * 
 * @author Claudius Korzen
 */
public class MoveText extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    if (args.size() < 2) {
      throw new MissingOperandException(op, args);
    }
    Matrix tlm = this.engine.getTextLineMatrix();
    if (tlm == null) {
      return;
    }

    COSNumber tx = (COSNumber) args.get(0);
    COSNumber ty = (COSNumber) args.get(1);

    Matrix matrix = new Matrix(1, 0, 0, 1, tx.floatValue(), ty.floatValue());
    tlm.concatenate(matrix);

    this.engine.setTextMatrix(tlm.clone());
  }

  @Override
  public String getName() {
    return "Td";
  }
}
