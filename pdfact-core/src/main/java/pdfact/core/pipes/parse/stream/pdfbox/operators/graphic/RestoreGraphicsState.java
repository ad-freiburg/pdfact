package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * Q: Restore the graphics state by removing the most recently saved state from
 * the stack and making it the current state.
 * 
 * @author Claudius Korzen
 */
public class RestoreGraphicsState extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    if (this.engine.getGraphicsStackSize() > 1) {
      this.engine.restoreGraphicsState();
    }
    // else {
    // this shouldn't happen but it does, see PDFBOX-161
    // throw new EmptyGraphicsStackException();
    // }
  }

  @Override
  public String getName() {
    return "Q";
  }
}
