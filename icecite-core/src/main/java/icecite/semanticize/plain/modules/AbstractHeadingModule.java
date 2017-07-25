package icecite.semanticize.plain.modules;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for the heading of the abstract.
 * 
 * @author Claudius Korzen
 */
public class AbstractHeadingModule extends SectionHeadingModule {
  /**
   * The known abstract headings.
   */
  protected static final Set<String> KNOWN_ABSTRACT_HEADINGS;

  static {
    KNOWN_ABSTRACT_HEADINGS = new HashSet<>();
    KNOWN_ABSTRACT_HEADINGS.add("abstract");
  }

  /**
   * Creates a new AbstractHeadingModule.
   * 
   * @param charListFactory
   *        The factory to create instance of PdfCharacterList.
   */
  @Inject
  public AbstractHeadingModule(PdfCharacterListFactory charListFactory) {
    super(charListFactory);
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    return super.test(block) && isKnownAbstractHeading(block);
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.ABSTRACT_HEADING;
  }

  // ==========================================================================

  /**
   * Returns true if the given text block is a known abstract heading.
   * 
   * @param block
   *        The text block to check.
   * @return True if the given text block is a known abstract heading, false
   *         otherwise.
   */
  protected boolean isKnownAbstractHeading(PdfTextBlock block) {
    return KNOWN_ABSTRACT_HEADINGS.contains(normalizeTextBlockText(block));
  }
}
