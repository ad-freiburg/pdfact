package icecite.semanticize.plain.modules;

import icecite.models.PdfDocument;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests text blocks if they match a specific
 * semantic role.
 * 
 * @author Claudius Korzen
 */
public abstract class PdfTextSemanticizerModule {
  /**
   * The PDF document.
   */
  protected PdfDocument pdf;

  /**
   * Sets the PDF document for this tester.
   * 
   * @param pdf
   *        The PDF document.
   */
  public void setPdfDocument(PdfDocument pdf) {
    this.pdf = pdf;
  }

  /**
   * Checks if the given text block matches the role defined by getRole().
   * 
   * @param block
   *        The text block to test.
   * @return True, if the given text block matches the role defined by
   *         getRole(), false otherwise.
   */
  public abstract boolean test(PdfTextBlock block);

  /**
   * Returns the specific role of this tester.
   * 
   * @return The role of this tester.
   */
  public abstract PdfRole getRole();
}