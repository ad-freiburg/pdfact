package pdfact.pipes.parse.stream.pdfbox.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * f: Fill path using non zero winding rule.
 * 
 * @author Claudius Korzen
 */
public class FillNonZeroRule extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    // Call operation "stroke path" with given windingRule.
    // Use COSFloat, because COSInteger is private.
    args.add(new COSFloat(Path2D.WIND_NON_ZERO));

    this.engine.processOperator(pdf, page, "S", args); // Stroke path
  }

  @Override
  public String getName() {
    return "f";
  }
}