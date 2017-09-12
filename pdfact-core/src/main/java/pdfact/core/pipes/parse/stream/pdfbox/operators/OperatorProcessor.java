package pdfact.core.pipes.parse.stream.pdfbox.operators;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;

import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;

/**
 * A class to process a specific operator in a content stream of a PDF file.
 * 
 * @author Claudius Korzen
 */
public abstract class OperatorProcessor {
  /**
   * The page stream engine.
   */
  protected PdfBoxPdfStreamsParser engine;

  // ==========================================================================

  /**
   * Sets the stream engine for this operator processor.
   * 
   * @param engine
   *        The stream engine to set.
   */
  public void setStreamEngine(PdfBoxPdfStreamsParser engine) {
    this.engine = engine;
  }

  // ==========================================================================

  /**
   * Processes the given operator.
   * 
   * @param pdf
   *        The PDF document to which the given operator belongs to.
   * @param page
   *        The PDF page to which the given operator belongs to.
   * @param op
   *        The operator to process
   * @param args
   *        The operands to use when processing
   * @throws IOException
   *         if the operator cannot be processed
   */
  public abstract void process(PdfDocument pdf, Page page, Operator op,
      List<COSBase> args) throws IOException;

  /**
   * Returns the name of this operator, e.g. "BI".
   * 
   * @return The name of of this operator.
   */
  public abstract String getName();
}
