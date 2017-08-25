package pdfact.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * h: Close path.
 * 
 * @author Claudius Korzen
 */
public class ClosePath extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.getLinePath().closePath();
  }

  @Override
  public String getName() {
    return "h";
  }
}
