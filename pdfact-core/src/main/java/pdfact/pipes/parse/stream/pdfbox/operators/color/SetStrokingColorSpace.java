package pdfact.pipes.parse.stream.pdfbox.operators.color;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import pdfact.model.Page;
import pdfact.model.PdfDocument;
import pdfact.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * CS: Set color space for stroking operations.
 *
 * @author Claudius Korzen
 */
public class SetStrokingColorSpace extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    COSName name = (COSName) args.get(0);

    // TODO: Use PdfColor here.
    PDColorSpace cs = this.engine.getResources().getColorSpace(name);
    this.engine.getGraphicsState().setStrokingColorSpace(cs);
    this.engine.getGraphicsState().setStrokingColor(cs.getInitialColor());
  }

  @Override
  public String getName() {
    return "CS";
  }
}