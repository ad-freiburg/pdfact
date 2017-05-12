package icecite.tokenizer;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterSet;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfCharacterSet.PdfCharacterSetFactory;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.tokenizer.xycut.XYCut;

/**
 * An implementation of {@link PdfWordTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfWordTokenizer extends XYCut<PdfWord>
    implements PdfWordTokenizer {
  /**
   * The factory to create instances of PdfWord.
   */
  protected PdfWordFactory wordFactory;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new word tokenizer.
   * 
   * @param characterSetFactory
   *        The factory to create instances of {@link PdfCharacterSet} (needed
   *        for XYCut).
   * @param wordFactory
   *        The factory to create instance of {@link PdfWord}.
   */
  @Inject
  public XYCutPdfWordTokenizer(PdfCharacterSetFactory characterSetFactory,
      PdfWordFactory wordFactory) {
    super(characterSetFactory);
    this.wordFactory = wordFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfWord> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return null;
  }

  // ==========================================================================

  @Override
  public float getVerticalLaneWidth(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isValidVerticalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet left, PdfCharacterSet overlap, PdfCharacterSet right) {
    return false;
  }

  // ==========================================================================

  @Override
  public float getHorizontalLaneHeight(PdfDocument pdf, PdfPage page,
      PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isValidHorizontalLane(PdfDocument pdf, PdfPage page,
      PdfCharacterSet upper, PdfCharacterSet overlap, PdfCharacterSet lower) {
    // TODO Auto-generated method stub
    return false;
  }

  // ==========================================================================

  @Override
  public PdfWord pack(PdfCharacterSet characters) {
    return this.wordFactory.create(characters);
  }
}
