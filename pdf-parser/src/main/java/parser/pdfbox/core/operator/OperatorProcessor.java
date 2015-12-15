package parser.pdfbox.core.operator;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import parser.pdfbox.core.PdfStreamEngine;

/**
 * Processes a PDF operator.
 * 
 * @author Claudius Korzen
 */
public abstract class OperatorProcessor {
  /** The processing context. */
  protected PdfStreamEngine context;

  /**
   * Returns the processing context.
   * 
   * @return the processing context
   */
  protected final PdfStreamEngine getContext() {
    return context;
  }

  /**
   * Sets the processing context.
   * 
   * @param context
   *          the processing context.
   */
  public void setContext(PdfStreamEngine context) {
    this.context = context;
  }

  /**
   * Processes the given operator.
   * 
   * @param operator
   *          the operator to process
   * @param operands
   *          the operands to use when processing
   * @throws IOException
   *           if the operator cannot be processed
   */
  public abstract void process(Operator operator, List<COSBase> operands)
    throws IOException;

  /**
   * Returns the name of this operator, e.g. "BI".
   * 
   * @return the name of of this operator. 
   */
  public abstract String getName();
}
