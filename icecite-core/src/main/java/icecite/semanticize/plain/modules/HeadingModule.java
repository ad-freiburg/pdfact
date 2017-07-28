package icecite.semanticize.plain.modules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
 * A module that identifies the text blocks with the semantic role 
 * "heading".
 * 
 * @author Claudius Korzen
 */
public class HeadingModule implements PdfTextSemanticizerModule {
  /**
   * The factory to create instance of {@link PdfCharacterList}.
   */
  protected PdfCharacterListFactory characterListFactory;

  /**
   * The texts of known headings, per *secondary* role.
   */
  protected static final Map<PdfRole, Set<String>> KNOWN_HEADINGS;

  static {
    KNOWN_HEADINGS = new HashMap<>();
    
    Set<String> abstractHeadings = new HashSet<>();
    abstractHeadings.add("abstract");
    
    KNOWN_HEADINGS.put(PdfRole.ABSTRACT, abstractHeadings);
    
    Set<String> acknowledgmentsHeadings = new HashSet<>();
    acknowledgmentsHeadings.add("acknowledgments");
    acknowledgmentsHeadings.add("acknowledgements");
    
    KNOWN_HEADINGS.put(PdfRole.ACKNOWLEDGMENTS, acknowledgmentsHeadings);
    
    Set<String> appendixHeadings = new HashSet<>();
    appendixHeadings = new HashSet<>();
    appendixHeadings.add("appendix");
    
    KNOWN_HEADINGS.put(PdfRole.APPENDIX, appendixHeadings);
    
    Set<String> keywordsHeadings = new HashSet<>();
    keywordsHeadings.add("keywords");
    
    KNOWN_HEADINGS.put(PdfRole.KEYWORDS, keywordsHeadings);
    
    Set<String> referencesHeadings = new HashSet<>();
    referencesHeadings.add("references");
    referencesHeadings.add("bibliography");
    
    KNOWN_HEADINGS.put(PdfRole.REFERENCE, referencesHeadings);
    
    Set<String> generalTermsHeadings = new HashSet<>();
    generalTermsHeadings.add("generalterms");
    
    KNOWN_HEADINGS.put(PdfRole.GENERAL_TERMS, generalTermsHeadings);
    
    Set<String> categoriesHeadings = new HashSet<>();
    categoriesHeadings.add("categories");
    categoriesHeadings.add("categoriesandsubjectdescriptors");
    
    KNOWN_HEADINGS.put(PdfRole.CATEGORIES, categoriesHeadings);
    
    Set<String> otherHeadings = new HashSet<>();
    otherHeadings.add("introduction");
    otherHeadings.add("contribution");
    otherHeadings.add("contributions");
    otherHeadings.add("related work");
    otherHeadings.add("experiments");
    otherHeadings.add("conclusion");
    otherHeadings.add("conclusions");
    otherHeadings.add("future work");
    
    KNOWN_HEADINGS.put(PdfRole.BODY_TEXT, otherHeadings);
  }

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new HeadingModule.
   * 
   * @param charListFactory
   *        The factory to create instances of {@link PdfCharacterList}.
   */
  @Inject
  public HeadingModule(PdfCharacterListFactory charListFactory) {
    this.characterListFactory = charListFactory;
  }

  // ==========================================================================

  @Override
  public void semanticize(PdfDocument pdf) {
    if (pdf == null) {
      return;
    }
    
    List<PdfPage> pages = pdf.getPages();
    if (pages == null) {
      return;
    }
    
    // Compute the expected font face of section headings.
    PdfFontFace headingFontFace = findSectionHeadingFontFace(pdf);
    
    for (PdfPage page : pages) {
      if (page == null) {
        continue;
      }
      
      for (PdfTextBlock block : page.getTextBlocks()) {
        if (block == null) {
          continue;
        }
        
        // Don't overwrite existing roles.
        if (block.getRole() != null) {
          continue;
        }
        
        PdfFontFace fontFace = block.getCharacters().getMostCommonFontFace();
        String text = toNormalizedText(block);
        
        // The text block is a heading if its font face is equal to the
        // computed section heading font face.
        if (headingFontFace == fontFace) {
          block.setRole(PdfRole.HEADING);
          // Iterate through the known headings to obtain the secondary role.
          for (PdfRole role : KNOWN_HEADINGS.keySet()) {
            Set<String> headings = KNOWN_HEADINGS.get(role);
            if (headings.contains(text)) {
              block.setSecondaryRole(role);
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
        if (!hasKnownSectionHeadingText(block)) {
          continue;
        }

        PdfFontFace fontFace = block.getCharacters().getMostCommonFontFace();
        if (fontFace == null) {
          continue;
        }

        // TODO:Find a reliable criteria to distinguish headings from the rest.
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
  protected boolean hasKnownSectionHeadingText(PdfTextBlock block) {
    String text = toNormalizedText(block);
    for (PdfRole role : KNOWN_HEADINGS.keySet()) {
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
  protected static String toNormalizedText(PdfTextBlock block) {
    if (block == null || block.getText() == null) {
      return null;
    }
    return block.getText().replaceAll("[^A-Za-z]", "").toLowerCase().trim();
  }
}
