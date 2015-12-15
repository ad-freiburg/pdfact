package parser.pdfbox.core.operator.graphics;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * gs: Set the specified parameters in the graphics state.
 * dictName shall be the name of a graphics state parameter
 * dictionary in the ExtGState subdictionary of the current resource
 * dictionary. 
 * 
 * @author Claudius Korzen
 */
public class SetGraphicsStateParameters extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    // set parameters from graphics state parameter dictionary
    COSName dictName = (COSName) arguments.get(0);
    PDExtendedGraphicsState gs = context.getResources().getExtGState(dictName);
    gs.copyIntoGraphicsState(context.getGraphicsState());
  }

  @Override
  public String getName() {
    return "gs";
  }
}
