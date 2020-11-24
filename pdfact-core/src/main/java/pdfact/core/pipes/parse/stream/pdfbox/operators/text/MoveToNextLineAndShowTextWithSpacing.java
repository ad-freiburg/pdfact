package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * ": Move to the next line and show a text string, using aw as the word spacing
 * and ac as the character spacing (setting the corresponding parameters in the
 * text state). aw and ac shall be numbers expressed in unscaled text space
 * units. This operator shall have the same effect as this code: aw Tw ac Tc
 * string '
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLineAndShowTextWithSpacing extends OperatorProcessor {
  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    if (args.size() < 3) {
      throw new MissingOperandException(op, args);
    }

    this.engine.processOperator(pdf, page, "Tw", args.subList(0, 1));
    this.engine.processOperator(pdf, page, "Tc", args.subList(1, 2));
    this.engine.processOperator(pdf, page, "'", args.subList(2, 3));
  }

  @Override
  public String getName() {
    return "\"";
  }
}
