package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * BMC: begin marked content.
 * 
 * @author Claudius Korzen
 */
public class BeginMarkedContent extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    // context.beginMarkedContent();
  }

  @Override
  public String getName() {
    return "BMC";
  }
}
