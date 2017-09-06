package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * ET: End a text object, discarding the text matrix.
 * 
 * @author Claudius Korzen.
 */
public class EndText extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.setTextMatrix(null);
    this.engine.setTextLineMatrix(null);
    // context.endText();
  }

  @Override
  public String getName() {
    return "ET";
  }
}