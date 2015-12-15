package parser.pdfbox.core.operator.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import parser.pdfbox.core.operator.OperatorProcessor;

/**
 * Tr: Set the text rendering mode to render, which shall be an integer. 
 * 
 * @author Claudius Korzen
 */
public class SetTextRenderingMode extends OperatorProcessor {
  @Override
  public void process(Operator operator, List<COSBase> arguments)
    throws IOException {
    if (arguments.isEmpty()) {
      throw new MissingOperandException(operator, arguments);
    }
    
    COSNumber mode = (COSNumber) arguments.get(0);
    RenderingMode renderingMode = RenderingMode.fromInt(mode.intValue());
    PDTextState textState = context.getGraphicsState().getTextState();
    textState.setRenderingMode(renderingMode);
  }

  @Override
  public String getName() {
    return "Tr";
  }
}
