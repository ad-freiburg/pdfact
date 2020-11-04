package pdfact.core.pipes.semanticize.modules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.statistician.CharacterStatistician;

/**
 * A module that identifies the text blocks with the semantic role "heading".
 * 
 * @author Claudius Korzen
 */
public class HeadingModule implements PdfTextSemanticizerModule {
  /**
   * The character statistician.
   */
  protected CharacterStatistician charStatistician;

  /**
   * The texts of known headings, per *secondary* role.
   */
  protected static final Map<SemanticRole, Set<String>> KNOWN_HEADINGS;

  static {
    KNOWN_HEADINGS = new HashMap<>();

    Set<String> abstractHeadings = new HashSet<>();
    abstractHeadings.add("abstract");

    KNOWN_HEADINGS.put(SemanticRole.ABSTRACT, abstractHeadings);

    Set<String> acknowledgmentsHeadings = new HashSet<>();
    acknowledgmentsHeadings.add("acknowledgments");
    acknowledgmentsHeadings.add("acknowledgements");

    KNOWN_HEADINGS.put(SemanticRole.ACKNOWLEDGMENTS, acknowledgmentsHeadings);

    Set<String> appendixHeadings = new HashSet<>();
    appendixHeadings = new HashSet<>();
    appendixHeadings.add("appendix");

    KNOWN_HEADINGS.put(SemanticRole.APPENDIX, appendixHeadings);

    Set<String> keywordsHeadings = new HashSet<>();
    keywordsHeadings.add("keywords");

    KNOWN_HEADINGS.put(SemanticRole.KEYWORDS, keywordsHeadings);

    Set<String> referencesHeadings = new HashSet<>();
    referencesHeadings.add("references");
    referencesHeadings.add("bibliography");

    KNOWN_HEADINGS.put(SemanticRole.REFERENCE, referencesHeadings);

    Set<String> generalTermsHeadings = new HashSet<>();
    generalTermsHeadings.add("generalterms");

    KNOWN_HEADINGS.put(SemanticRole.GENERAL_TERMS, generalTermsHeadings);

    Set<String> categoriesHeadings = new HashSet<>();
    categoriesHeadings.add("categories");
    categoriesHeadings.add("categoriesandsubjectdescriptors");

    KNOWN_HEADINGS.put(SemanticRole.CATEGORIES, categoriesHeadings);

    Set<String> otherHeadings = new HashSet<>();
    otherHeadings.add("introduction");
    otherHeadings.add("contribution");
    otherHeadings.add("contributions");
    otherHeadings.add("related work");
    otherHeadings.add("experiments");
    otherHeadings.add("conclusion");
    otherHeadings.add("conclusions");
    otherHeadings.add("future work");

    KNOWN_HEADINGS.put(SemanticRole.BODY_TEXT, otherHeadings);
  }

  // ==============================================================================================
  // Constructors.

  /**
   * Creates a new HeadingModule.
   *
   */
  public HeadingModule() {
    this.charStatistician = new CharacterStatistician();
  }

  // ==============================================================================================

  @Override
  public void semanticize(PdfDocument pdf) {
    if (pdf == null) {
      return;
    }

    List<Page> pages = pdf.getPages();
    if (pages == null) {
      return;
    }

    // Compute the expected font face of section headings.
    FontFace headingFontFace = findSectionHeadingFontFace(pdf);

    for (Page page : pages) {
      if (page == null) {
        continue;
      }

      for (TextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }

        // Don't overwrite existing roles.
        if (block.getSemanticRole() != null) {
          continue;
        }

        CharacterStatistic blockCharStats = block.getCharacterStatistic();
        FontFace fontFace = blockCharStats.getMostCommonFontFace();
        String text = toNormalizedText(block);

        // The text block is a heading if its font face is equal to the
        // computed section heading font face.
        if (headingFontFace == fontFace) {
          block.setSemanticRole(SemanticRole.HEADING);
          // Iterate through the known headings to obtain the secondary role.
          for (SemanticRole role : KNOWN_HEADINGS.keySet()) {
            Set<String> headings = KNOWN_HEADINGS.get(role);
            if (headings.contains(text)) {
              block.setSecondarySemanticRole(role);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Searches the PDF document for known section headings and analyzes their
   * font faces.
   * 
   * @param pdf
   *        The PDF document.
   * 
   * @return The most common font face of all known section headings.
   */
  protected FontFace findSectionHeadingFontFace(PdfDocument pdf) {
    if (pdf == null) {
      return null;
    }

    CharacterStatistic pdfCharStats = pdf.getCharacterStatistic();
    FontFace pdfFontFace = pdfCharStats.getMostCommonFontFace();

    if (pdfFontFace == null) {
      return null;
    }

    // The characters of all known section headings.
    ElementList<Character> headingChars = new ElementList<>();

    for (Page page : pdf.getPages()) {
      for (TextBlock block : page.getTextBlocks()) {
        if (!hasKnownSectionHeadingText(block)) {
          continue;
        }

        CharacterStatistic blockCharStats = block.getCharacterStatistic();
        FontFace fontFace = blockCharStats.getMostCommonFontFace();

        if (fontFace == null) {
          continue;
        }

        // TODO: Find a reliable criteria to distinguish headings from the
        // rest.

        if (fontFace.getFontSize() - pdfFontFace.getFontSize() > 1) {
          for (TextLine line : block.getTextLines()) {
            for (Word word : line.getWords()) {
              headingChars.addAll(word.getCharacters());
            }
          }
        }
      }
    }

    CharacterStatistic stats = this.charStatistician.compute(headingChars);
    return stats.getMostCommonFontFace();
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
  protected boolean hasKnownSectionHeadingText(TextBlock block) {
    String text = toNormalizedText(block);
    for (SemanticRole role : KNOWN_HEADINGS.keySet()) {
      Set<String> headings = KNOWN_HEADINGS.get(role);
      if (headings.contains(text)) {
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
  @Deprecated
  protected static String toNormalizedText(TextBlock block) {
    if (block == null || block.getText() == null) {
      return null;
    }
    return block.getText().replaceAll("[^A-Za-z]", "").toLowerCase().trim();
  }
}
