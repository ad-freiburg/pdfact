package pdfact.core.pipes.parse.stream.pdfbox.operators.text;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.Document;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;

/**
 * ': Move to the next line and show a text string. This operator shall have the
 * same effect as the code T* string T
 * 
 * @author Claudius Korzen
 */
public class MoveToNextLineAndShowText extends OperatorProcessor {
  @Override
  public void process(Document pdf, Page page, Operator op,
      List<COSBase> args) throws IOException {
    this.engine.processOperator(pdf, page, "T*", null);
    this.engine.processOperator(pdf, page, "Tj", args);
  }

  @Override
  public String getName() {
    return "'";
  }
}
