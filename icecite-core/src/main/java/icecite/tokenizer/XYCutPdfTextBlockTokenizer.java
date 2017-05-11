package icecite.tokenizer;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.tokenizer.xycut.XYCut;
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
  public List<PdfTextBlock> tokenize(PdfCharacterSet characters) {
    return cut(characters);
  }

  // ==========================================================================

  @Override
  public float getVerticalLaneWidth(PdfCharacterSet chars) {
    return 1f;
    // return 2f * chars.getMostCommonWidth();
  }

  @Override
  public boolean isValidVerticalLane(PdfCharacterSet overlapping) {
    return overlapping.isEmpty();
  }

  // ==========================================================================

  @Override
  public float getHorizontalLaneHeight(PdfCharacterSet chars) {
    return 10f;
    // return 2f * chars.getMostCommonHeight();
  }

  @Override
  public boolean isValidHorizontalLane(PdfCharacterSet overlapping) {
    return overlapping.isEmpty();
  }

  // ==========================================================================

  @Override
  public PdfTextBlock pack(PdfCharacterSet characters) {
    // FIXME
    PdfTextBlock block = this.textBlockFactory.create(characters);
    block.setBoundingBox(PlainRectangle.fromBoundingBoxOf(characters));
    return block;
  }
}
