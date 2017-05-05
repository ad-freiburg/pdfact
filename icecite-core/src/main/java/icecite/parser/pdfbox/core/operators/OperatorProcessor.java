package icecite.parser.pdfbox.core.operators;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import icecite.parser.pdfbox.core.PdfStreamEngine;

/**
 * A class to process a specific operator in a PDF content stream.
 * 
 * @author Claudius Korzen
 */
public abstract class OperatorProcessor {
  /**
   * The page stream engine.
   */
  protected PdfStreamEngine engine;

  /**
   * Sets the stream engine for this operator processor.
   * 
   * @param engine
   *        The stream engine to set.
   */
  public void setStreamEngine(PdfStreamEngine engine) {
    this.engine = engine;
  }

  /**
   * Processes the given operator.
   * 
   * @param op
   *        The operator to process
   * @param args
   *        The operands to use when processing
   * @throws IOException
   *         if the operator cannot be processed
   */
  public abstract void process(Operator op, List<COSBase> args)
      throws IOException;

  /**
   * Returns the name of this operator, e.g. "BI".
   * 
   * @return The name of of this operator.
   */
  public abstract String getName();
}
