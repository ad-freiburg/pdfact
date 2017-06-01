package icecite.semanticize.plain.modules;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for the heading of the references.
 * 
 * @author Claudius Korzen
 */
public class ReferencesHeadingModule extends SectionHeadingModule {
  /**
   * The known references headings.
   */
  protected static final Set<String> KNOWN_REFERENCES_HEADINGS;

  static {
    KNOWN_REFERENCES_HEADINGS = new HashSet<>();
    KNOWN_REFERENCES_HEADINGS.add("reference");
    KNOWN_REFERENCES_HEADINGS.add("references");
    KNOWN_REFERENCES_HEADINGS.add("bibliography");
  }

  /**
   * Creates a new ReferencesHeadingModule.
   * 
   * @param charListFactory
   *        The factory to create instance of {@link PdfCharacterList}.
   */
  @Inject
  public ReferencesHeadingModule(PdfCharacterListFactory charListFactory) {
    super(charListFactory);
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    return super.test(block) && isKnownReferencesHeading(block);
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.REFERENCES_HEADING;
  }

  // ==========================================================================

  /**
   * Returns true if the given text block is a known references heading.
   * 
   * @param block
   *        The text block to check.
   * @return True if the given text block is a known references heading,
   *         false otherwise.
   */
  protected boolean isKnownReferencesHeading(PdfTextBlock block) {
    return KNOWN_REFERENCES_HEADINGS
        .contains(normalizeTextBlockText(block));
  }
}