package pdfact.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * W*: set clipping path using even odd rule.
 * 
 * @author Claudius Korzen
 */
public class ClipEvenOddRule extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.setClippingWindingRule(Path2D.WIND_EVEN_ODD);
  }

  @Override
  public String getName() {
    return "W*";
  }
}
