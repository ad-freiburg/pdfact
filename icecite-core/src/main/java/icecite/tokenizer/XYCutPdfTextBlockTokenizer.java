package icecite.tokenizer;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
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
  public List<PdfTextBlock> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    return cut(pdf, page, characters);
  }

  // ==========================================================================

  @Override
  public float getVerticalLaneWidth(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars) {
    return 1;
    // return Math.max(pdf.getMostCommonWidth(), chars.getMostCommonWidth());
  }

  @Override
  public boolean isValidVerticalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet left, PdfCharacterSet overlap, PdfCharacterSet right) {
    return overlap.isEmpty();
  }

  // ==========================================================================

  @Override
  public float getHorizontalLaneHeight(PdfDocument pdf, PdfPage page,
      PdfCharacterSet chars) {
    return Math.max(pdf.getMostCommonHeight(), chars.getMostCommonHeight());
  }

  @Override
  public boolean isValidHorizontalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet upper, PdfCharacterSet overlap, PdfCharacterSet lower) {
    return overlap.isEmpty();
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
