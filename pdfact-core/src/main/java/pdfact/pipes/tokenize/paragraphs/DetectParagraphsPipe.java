package pdfact.pipes.tokenize.paragraphs;

import pdfact.util.pipeline.Pipe;

/**
 * A pipe to tokenize the text blocks of a PDF page into paragraphs.
 * 
 * @author Claudius Korzen
 */
public interface DetectParagraphsPipe extends Pipe {
  /**
   * The factory to create instances of {@link DetectParagraphsPipe}.
   * 
   * @author Claudius Korzen
   */
  public interface DetectParagraphsPipeFactory {
    /**
     * Creates a new TokenizeParagraphsPipe.
     * 
     * @return An instance of {@link DetectParagraphsPipe}.
     */
    DetectParagraphsPipe create();
  }
}

