package icecite.semanticize.plain.modules;

import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for body text.
 * 
 * @author Claudius Korzen
 */
public class BodyTextModule extends PdfTextSemanticizerModule {
  @Override
  public boolean test(PdfTextBlock block) {
    if (block == null) {
      return false;
    }

    if (block.getRole() != null) {
      return false;
    }

    return this.pdf.getCharacters().getMostCommonFontFace() == block
        .getCharacters().getMostCommonFontFace();
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.BODY_TEXT;
  }
}
