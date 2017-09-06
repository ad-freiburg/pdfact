package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * q: Save the current graphics state on the graphics state stack.
 * 
 * @author Claudius Korzen
 */
public class SaveGraphicsState extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.saveGraphicsState();
  }

  @Override
  public String getName() {
    return "q";
  }
}
