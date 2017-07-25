package icecite.semanticize.plain.modules;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for the heading of the appendix.
 * 
 * @author Claudius Korzen
 */
public class AppendixHeadingModule extends SectionHeadingModule {
  /**
   * The known appendix headings.
   */
  protected static final Set<String> KNOWN_APPENDIX_HEADINGS;

  static {
    KNOWN_APPENDIX_HEADINGS = new HashSet<>();
    KNOWN_APPENDIX_HEADINGS.add("appendix");
  }

  /**
   * Creates a new AppendixHeadingModule.
   * 
   * @param charListFactory
   *        The factory to create instance of PdfCharacterList.
   */
  @Inject
  public AppendixHeadingModule(PdfCharacterListFactory charListFactory) {
    super(charListFactory);
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    return super.test(block) && isKnownAppendixHeading(block);
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.APPENDIX_HEADING;
  }

  // ==========================================================================

  /**
   * Returns true if the given text block is a known appendix heading.
   * 
   * @param block
   *        The text block to check.
   * @return True if the given text block is a known appendix heading,
   *         false otherwise.
   */
  protected boolean isKnownAppendixHeading(PdfTextBlock block) {
    return KNOWN_APPENDIX_HEADINGS
        .contains(normalizeTextBlockText(block));
  }
}