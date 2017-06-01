package icecite.semanticize.plain.modules;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfFontFace;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A semantic role tester that tests for section headings.
 * 
 * @author Claudius Korzen
 */
public class SectionHeadingModule extends PdfTextSemanticizerModule {
  /**
   * The factory to create instance of {@link PdfCharacterList}.
   */
  protected PdfCharacterListFactory characterListFactory;

  /**
   * The font face of section headings.
   */
  protected PdfFontFace sectionHeadingFontFace;

  /**
   * The texts of some known section headings.
   */
  protected static final Set<String> KNOWN_SECTION_HEADINGS;

  static {
    KNOWN_SECTION_HEADINGS = new HashSet<>();
    KNOWN_SECTION_HEADINGS.add("abstract");
    KNOWN_SECTION_HEADINGS.add("introduction");
    KNOWN_SECTION_HEADINGS.add("contribution");
    KNOWN_SECTION_HEADINGS.add("contributions");
    KNOWN_SECTION_HEADINGS.add("related work");
    KNOWN_SECTION_HEADINGS.add("experiments");
    KNOWN_SECTION_HEADINGS.add("conclusion");
    KNOWN_SECTION_HEADINGS.add("conclusions");
    KNOWN_SECTION_HEADINGS.add("future work");
    KNOWN_SECTION_HEADINGS.add("acknowledgments");
    KNOWN_SECTION_HEADINGS.add("acknowledgements");
    KNOWN_SECTION_HEADINGS.add("references");
    KNOWN_SECTION_HEADINGS.add("bibliography");
  }

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new SectionHeadingRoleTester.
   * 
   * @param charListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   */
  @Inject
  public SectionHeadingModule(PdfCharacterListFactory charListFactory) {
    this.characterListFactory = charListFactory;
  }

  // ==========================================================================

  @Override
  public void setPdfDocument(PdfDocument pdf) {
    super.setPdfDocument(pdf);
    this.pdf = pdf;
    this.sectionHeadingFontFace = findSectionHeadingFontFace(pdf);
  }

  // ==========================================================================

  @Override
  public boolean test(PdfTextBlock block) {
    if (block == null) {
      return false;
    }

    PdfFontFace fontFace = block.getCharacters().getMostCommonFontFace();
    return fontFace == this.sectionHeadingFontFace;
  }

  @Override
  public PdfRole getRole() {
    return PdfRole.BODY_TEXT_HEADING;
  }

  // ==========================================================================

  /**
   * Searches the PDF document for known section headings and analyzes their
   * font faces.
   * 
   * @param pdf
   *        The PDF document.
   * 
   * @return The most common font face of all known section headings.
   */
  protected PdfFontFace findSectionHeadingFontFace(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }

    PdfFontFace pdfFontFace = pdf.getCharacters().getMostCommonFontFace();
    if (pdfFontFace == null) {
      return null;
    }

    // The characters of all known section headings.
    PdfCharacterList headingCharacters = this.characterListFactory.create();

    for (PdfPage page : pdf.getPages()) {
      for (PdfTextBlock block : page.getTextBlocks()) {
        if (!isKnownSectionHeading(block)) {
          continue;
        }

        PdfFontFace fontFace = block.getCharacters().getMostCommonFontFace();
        if (fontFace == null) {
          continue;
        }

        // TODO
        if (fontFace.getFontSize() - pdfFontFace.getFontSize() > 1) {
          headingCharacters.addCharacters(block.getCharacters());
        }
      }
    }
    return headingCharacters.getMostCommonFontFace();
  }

  /**
   * Checks if the text of the given text block is a known section heading.
   * 
   * @param block
   *        The block to check.
   * 
   * @return True if the text of the given text block is a known section
   *         heading.
   */
  protected boolean isKnownSectionHeading(PdfTextBlock block) {
    if (block == null) {
      return false;
    }

    String text = normalizeTextBlockText(block);
    for (String knownSectionHeading : KNOWN_SECTION_HEADINGS) {
      if (knownSectionHeading.equals(text)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Normalizes the text of the given text block.
   * 
   * @param block
   *        The text block to normalize.
   * @return The normalized text of the given text block.
   */
  protected static String normalizeTextBlockText(PdfTextBlock block) {
    if (block == null || block.getText() == null) {
      return null;
    }
    return block.getText().replaceAll("[^A-Za-z]", "").toLowerCase().trim();
  }
}
