package icecite.semanticize.plain.modules;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for the heading of the acknowledgments.
 * 
 * @author Claudius Korzen
 */
public class AcknowledgmentsHeadingModule extends SectionHeadingModule {
  /**
   * The known acknowledgments headings.
   */
  protected static final Set<String> KNOWN_ACKNOWLEDGMENTS_HEADINGS;

  static {
    KNOWN_ACKNOWLEDGMENTS_HEADINGS = new HashSet<>();
    KNOWN_ACKNOWLEDGMENTS_HEADINGS.add("acknowledgment");
    KNOWN_ACKNOWLEDGMENTS_HEADINGS.add("acknowledgments");
    KNOWN_ACKNOWLEDGMENTS_HEADINGS.add("acknowledgement");
    KNOWN_ACKNOWLEDGMENTS_HEADINGS.add("acknowledgements");
  }

  /**
   * Creates a new AcknowledgmentsHeadingModule.
   * 
   * @param charListFactory
   *        The factory to create instance of {@link PdfCharacterList}.
   */
  @Inject
  public AcknowledgmentsHeadingModule(PdfCharacterListFactory charListFactory) {
    super(charListFactory);
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    return super.test(block) && isKnownAcknowledgmentsHeading(block);
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.ACKNOWLEDGMENTS_HEADING;
  }

  // ==========================================================================

  /**
   * Returns true if the given text block is a known acknowledgments heading.
   * 
   * @param block
   *        The text block to check.
   * @return True if the given text block is a known acknowledgments heading,
   *         false otherwise.
   */
  protected boolean isKnownAcknowledgmentsHeading(PdfTextBlock block) {
    return KNOWN_ACKNOWLEDGMENTS_HEADINGS
        .contains(normalizeTextBlockText(block));
  }
}
