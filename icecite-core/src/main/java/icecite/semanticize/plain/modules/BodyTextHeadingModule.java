package icecite.semanticize.plain.modules;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfFontFace;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for the headings in the body text.
 * 
 * @author Claudius Korzen
 */
public class BodyTextHeadingModule extends SectionHeadingModule {
  /**
   * Creates a new BodyTextHeadingModule.
   * 
   * @param charListFactory
   *        The factory to create instance of {@link PdfCharacterList}.
   */
  @Inject
  public BodyTextHeadingModule(PdfCharacterListFactory charListFactory) {
    super(charListFactory);
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    if (block == null) {
      return false;
    }

    PdfFontFace fontFace = block.getCharacters().getMostCommonFontFace();
        
    return fontFace == this.sectionHeadingFontFace && block.getRole() == null;
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.BODY_TEXT_HEADING;
  }
}