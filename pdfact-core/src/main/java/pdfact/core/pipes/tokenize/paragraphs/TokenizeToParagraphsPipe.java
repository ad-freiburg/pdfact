package pdfact.core.pipes.tokenize.paragraphs;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that tokenizes the text blocks of a PDF document into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface TokenizeToParagraphsPipe extends Pipe {
  /**
   * The factory to create instances of {@link TokenizeToParagraphsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface TokenizeToParagraphsPipeFactory {
    /**
     * Creates A new instance of {@link TokenizeToParagraphsPipe}.
     * 
     * @return A new instance of {@link TokenizeToParagraphsPipe}.
     */
    TokenizeToParagraphsPipe create();
  }
}

