package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * B*: Fill and then stroke the path, using the even-odd rule to determine the
 * region to fill.
 * 
 * @author Claudius Korzen
 */
public class FillEvenOddAndStrokePath extends OperatorProcessor {
  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    GeneralPath currentPath = (GeneralPath) this.engine.getLinePath().clone();

    this.engine.processOperator(pdf, page, "f*", args);
    this.engine.setLinePath(currentPath);
    this.engine.processOperator(pdf, page, "S", args);
  }

  @Override
  public String getName() {
    return "B*";
  }
}
