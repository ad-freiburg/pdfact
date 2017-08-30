package pdfact.pipes.tokenize.words;

import pdfact.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe;
import pdfact.util.pipeline.Pipe;

/**
 * A pipe that tokenize the text lines of a PDF page into words.
 * 
 * @author Claudius Korzen
 */
public interface TokenizeToWordsPipe extends Pipe {
  /**
   * The factory to create instances of {@link TokenizeToParagraphsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface TokenizeToWordsPipeFactory {
    /**
     * Creates a new instance of {@link TokenizeToParagraphsPipe}.
     * 
     * @return A new instance of {@link TokenizeToParagraphsPipe}.
     */
    TokenizeToWordsPipe create();
  }
}
