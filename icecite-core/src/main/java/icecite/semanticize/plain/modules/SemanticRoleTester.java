package icecite.semanticize.plain.modules;

import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests text blocks if they match a specific
 * semantic role.
 * 
 * @author Claudius Korzen
 */
public interface SemanticRoleTester {
  /**
   * Checks if the given text block matches the role defined by getRole().
   * 
   * @param block
   *        The text block to test.
   * @return True, if the given text block matches the role defined by
   *         getRole(), false otherwise.
   */
  boolean test(PdfTextBlock block);

  /**
   * Returns the specific role of this tester.
   * 
   * @return The role of this tester.
   */
  PdfRole getRole();
}
