package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * W: Set the clipping path using non zero winding rule.
 * 
 * @author Claudius Korzen
 */
public class ClipNonZeroRule extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.setClippingWindingRule(Path2D.WIND_NON_ZERO);
  }

  @Override
  public String getName() {
    return "W";
  }
}
