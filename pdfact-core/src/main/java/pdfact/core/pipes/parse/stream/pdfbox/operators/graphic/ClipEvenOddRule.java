package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * W*: set clipping path using even odd rule.
 * 
 * @author Claudius Korzen
 */
public class ClipEvenOddRule extends OperatorProcessor {
  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.setClippingWindingRule(Path2D.WIND_EVEN_ODD);
  }

  @Override
  public String getName() {
    return "W*";
  }
}
