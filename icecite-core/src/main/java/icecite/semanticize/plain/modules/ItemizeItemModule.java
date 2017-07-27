package icecite.semanticize.plain.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfRole;
import icecite.models.PdfTextBlock;

/**
 * A module that identifies the text blocks with the semantic role "caption".
 * 
 * @author Claudius Korzen
 */
public class ItemizeItemModule implements PdfTextSemanticizerModule {
  /**
   * A list of patterns to identify items of itemizes.
   */
  public static final List<Pattern> ITEMIZE_ITEM_PATTERNS;

  static {
    ITEMIZE_ITEM_PATTERNS = new ArrayList<>();

    // A pattern to find items that start with "•".
    Pattern pattern1 = Pattern.compile(
        "^(•)(\\s+\\w|$)", Pattern.CASE_INSENSITIVE);
    ITEMIZE_ITEM_PATTERNS.add(pattern1);

    // A pattern to find items that start with "I.", "II.", "III.", "IV.", etc.
    Pattern pattern2 = Pattern.compile(
        "^(X{0,1}(IX|IV|V?I{0,3}))\\.(\\s+\\w|$)", Pattern.CASE_INSENSITIVE);
    ITEMIZE_ITEM_PATTERNS.add(pattern2);

    // A pattern to find items that start with "(I)", "(II)", "(III)", etc.
    Pattern pattern3 = Pattern.compile(
        "^\\((X{0,1}(IX|IV|V?I{0,3}))\\)(\\s+\\w|$)", Pattern.CASE_INSENSITIVE);
    ITEMIZE_ITEM_PATTERNS.add(pattern3);

    // A pattern to find items that start with "A.", "B.", "C.", 0., 1., etc.
    // Don't use Pattern.CASE_INSENSITIVE here to avoid to match author names
    // like "S. Okamato".
    Pattern pattern4 = Pattern.compile("^([a-z0-9])\\.(\\s+\\w|$)");
    ITEMIZE_ITEM_PATTERNS.add(pattern4);

    // A more general pattern to find further items: (A), (1), (C1), etc.
    Pattern pattern5 = Pattern.compile("^\\(([A-Z0-9][0-9]{0,1})\\)(\\s+\\w|$)",
        Pattern.CASE_INSENSITIVE);
    ITEMIZE_ITEM_PATTERNS.add(pattern5);

    // A pattern similar to #4 but with Uppercase letters. Needed to
    // distinguish between section headings and author names like "A. Meyer".
    Pattern pattern6 = Pattern.compile("^([A-Z0-9])\\.(\\s+\\w|$)");
    ITEMIZE_ITEM_PATTERNS.add(pattern6);
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

        // The text block is a itemize item if its text matches to one of the
        // given patterns.
        for (Pattern pattern : ITEMIZE_ITEM_PATTERNS) {
          Matcher matcher = pattern.matcher(block.getText());
          if (matcher.find() && !matcher.group(1).isEmpty()) {
            block.setRole(PdfRole.ITEMIZE_ITEM);
            break;
          }
        }
      }
    }
  }
}
