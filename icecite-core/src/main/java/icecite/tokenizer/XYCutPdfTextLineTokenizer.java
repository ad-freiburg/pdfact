package icecite.tokenizer;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.tokenizer.xycut.XYCut;

/**
 * An implementation of {@link PdfTextLineTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfTextLineTokenizer extends XYCut<PdfTextLine>
    implements PdfTextLineTokenizer {
  /**
   * The factory to create instances of PdfTextLine.
   */
  protected PdfTextLineFactory textLineFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text line tokenizer.
   * 
   * @param characterSetFactory
   *        The factory to create instances of {@link PdfCharacterSet} (needed
   *        for XYCut).
   * @param textLineFactory
   *        The factory to create instance of {@link PdfTextLine}.
   */
  @Inject
  public XYCutPdfTextLineTokenizer(PdfCharacterSetFactory characterSetFactory,
      PdfTextLineFactory textLineFactory) {
    super(characterSetFactory);
    this.textLineFactory = textLineFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfTextLine> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    return cut(pdf, page, characters);
  }

  // ==========================================================================

  @Override
  public float getVerticalLaneWidth(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public float getHorizontalLaneHeight(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return 0;
  }

  // ==========================================================================

  @Override
  public boolean isValidVerticalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet left, PdfCharacterSet overlap, PdfCharacterSet right) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isValidHorizontalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet upper, PdfCharacterSet overlap, PdfCharacterSet lower) {
    // TODO Auto-generated method stub
    return false;
  }

  // ==========================================================================

  @Override
  public PdfTextLine pack(PdfCharacterSet characters) {
    return this.textLineFactory.create(characters);
  }
}
