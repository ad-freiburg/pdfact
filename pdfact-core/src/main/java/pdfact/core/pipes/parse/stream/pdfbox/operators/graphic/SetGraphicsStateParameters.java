package pdfact.core.pipes.parse.stream.pdfbox.operators.graphic;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * gs: Set the specified parameters in the graphics state. dictName shall be the
 * name of a graphics state parameter dictionary in the ExtGState sub-dictionary
 * of the current resource dictionary.
 * 
 * @author Claudius Korzen
 */
public class SetGraphicsStateParameters extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    // set parameters from graphics state parameter dictionary
    COSName dictName = (COSName) args.get(0);
    PDResources resources = this.engine.getResources();
    PDExtendedGraphicsState gs = resources.getExtGState(dictName);
    gs.copyIntoGraphicsState(this.engine.getGraphicsState());
  }

  @Override
  public String getName() {
    return "gs";
  }
}
