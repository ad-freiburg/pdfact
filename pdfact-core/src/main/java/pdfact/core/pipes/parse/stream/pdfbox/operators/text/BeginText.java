package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.Matrix;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * BT: Begin a text object, initializing the text matrix, Tm, and the text line
 * matrix to the identity matrix.
 *
 * @author Claudius Korzen
 */
public class BeginText extends OperatorProcessor {
  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.setTextMatrix(new Matrix());
    this.engine.setTextLineMatrix(new Matrix());
    // context.beginText();
  }

  @Override
  public String getName() {
    return "BT";
  }
}
