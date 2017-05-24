package icecite.tokenizer;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.tokenizer.xycut.XYCut;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.plain.PlainRectangle;

// TODO: Rework

/**
 * An implementation of {@link PdfTextBlockTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfTextBlockTokenizer extends XYCut<PdfTextBlock>
    implements PdfTextBlockTokenizer {
  /**
   * The factory to create instances of PdfTextBlock.
   */
  protected PdfTextBlockFactory textBlockFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text block tokenizer.
   * 
   * @param textBlockFactory
   *        The factory to create instance of {@link PdfTextBlock}.
   */
  @Inject
  public XYCutPdfTextBlockTokenizer(PdfTextBlockFactory textBlockFactory) {
    super();
    this.textBlockFactory = textBlockFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList characters) {    
    return cut(pdf, page, characters);
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList l = halves.get(0);
    PdfCharacterList r = halves.get(1);
            
    float width = r.getBoundingBox().getMinX() - l.getBoundingBox().getMaxX();

    float docWidths = pdf.getCharacters().getMostCommonWidth();
    float pageWidths = page.getCharacters().getMostCommonWidth();
    if (width < Math.max(docWidths, pageWidths)) {
      return -1;
    }
    
    if (separatesConsecutiveCharacters(l, r)) {
      return -1;
    }
    return Math.abs(width);
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList u = halves.get(0);
    PdfCharacterList l = halves.get(1);
        
    float height = u.getBoundingBox().getMinY() - l.getBoundingBox().getMaxY();
    
    if (height < 0) {
      return -1;
    }
    
    float docHeights = pdf.getCharacters().getMostCommonHeight();
    float pageHeights = page.getCharacters().getMostCommonHeight();
    if (height < Math.min(docHeights, pageHeights)) {
      return -1;
    }
    return height;
  }

  // ==========================================================================

  @Override
  public PdfTextBlock pack(PdfPage page, PdfCharacterList characters) {
    // FIXME
    PdfTextBlock block = this.textBlockFactory.create(characters);
    // TODO: use Guice here.
    block.setBoundingBox(PlainRectangle.fromBoundingBoxOf(characters));
    block.setPage(page);
    return block;
  }

  // ==========================================================================
  // Utility methods.

  /**
   * Checks if there is a character in the first character set with extraction
   * order number i and a characters in the second character set with
   * extraction order number i + 1, where both characters overlap vertically.
   * 
   * @param left
   *        The first character set.
   * @param right
   *        The second character set.
   * @return True if there is such a character pair, false otherwise.
   */
  protected boolean separatesConsecutiveCharacters(PdfCharacterList left,
      PdfCharacterList right) {
    Set<PdfCharacter> leftChars = left.getElementsWithLargestMaxX();
    Set<PdfCharacter> rightChars = right.getElementsWithSmallestMinX();
         
    for (PdfCharacter leftChar : leftChars) {
      int leftCharNum = leftChar.getExtractionOrderNumber();
      Rectangle leftCharBox = leftChar.getBoundingBox();
      for (PdfCharacter rightChar : rightChars) {
        int rightCharNum = rightChar.getExtractionOrderNumber();
        Rectangle rightCharNox = rightChar.getBoundingBox();

        // Check if the characters are consecutive.
        if (rightCharNum != leftCharNum + 1) {
          continue;
        }
        // Check if the characters overlap.
        if (!leftCharBox.overlapsVertically(rightCharNox)) {
          continue;
        }
        return true;
      }
    }
    return false;
  }
}
