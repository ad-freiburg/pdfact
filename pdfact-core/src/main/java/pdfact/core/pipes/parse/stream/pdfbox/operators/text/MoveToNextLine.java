package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * T*: Move to the start of the next line. This operator has the same effect as
 * the code 0 -Tl Td, where Tl denotes the current leading parameter in the text
 * state. The negative of Tl is used here because Tl is the text leading
 * expressed as a positive number. Going to the next line entails decreasing the
 * y coordinate.
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLine extends OperatorProcessor {
  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    ArrayList<COSBase> otherArgs = new ArrayList<COSBase>();

    PDTextState textState = this.engine.getGraphicsState().getTextState();
    otherArgs.add(new COSFloat(0f));
    otherArgs.add(new COSFloat(-1 * textState.getLeading()));
    this.engine.processOperator(pdf, page, "Td", otherArgs);
  }

  @Override
  public String getName() {
    return "T*";
  }
}
