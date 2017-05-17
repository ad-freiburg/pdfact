package icecite.tokenizer;

import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterSet;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.tokenizer.xycut.XYCut;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.plain.PlainRectangle;

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
   * @param characterSetFactory
   *        The factory to create instances of {@link PdfCharacterSet} (needed
   *        for XYCut).
   * @param textBlockFactory
   *        The factory to create instance of {@link PdfTextBlock}.
   */
  @Inject
  public XYCutPdfTextBlockTokenizer(PdfCharacterSetFactory characterSetFactory,
      PdfTextBlockFactory textBlockFactory) {
    super(characterSetFactory);
    this.textBlockFactory = textBlockFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    return cut(pdf, page, characters);
  }

  // ==========================================================================

  @Override
  public float getVerticalLaneWidth(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars) {
    float docWidths = pdf.getCharacters().getMostCommonWidth();
    float pageWidths = page.getCharacters().getMostCommonWidth();
    return Math.max(docWidths, pageWidths);
  }

  @Override
  public boolean isValidVerticalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet left, PdfCharacterSet overlap, PdfCharacterSet right) {
    if (!overlap.isEmpty()) {
      return false;
    }
    return !separatesConsecutiveCharacters(left, right);
  }

  // ==========================================================================

  @Override
  public float getHorizontalLaneHeight(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars) {
    float docHeights = pdf.getCharacters().getMostCommonHeight();
    float pageHeights = page.getCharacters().getMostCommonHeight();
    return Math.min(docHeights, pageHeights);
  }

  @Override
  public boolean isValidHorizontalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet upper, PdfCharacterSet overlap, PdfCharacterSet lower) {
    return overlap.isEmpty();
  }

  // ==========================================================================

  @Override
  public PdfTextBlock pack(PdfPage page, PdfCharacterSet characters) {
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
  protected boolean separatesConsecutiveCharacters(PdfCharacterSet left,
      PdfCharacterSet right) {
    Set<PdfCharacter> leftChars = left.getRightMostCharacters();
    Set<PdfCharacter> rightChars = right.getLeftMostCharacters();

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
