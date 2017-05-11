package icecite.tokenizer;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterSet;
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
  public List<PdfWord> tokenize(PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return null;
  }

  // ==========================================================================

  @Override
  public float getVerticalLaneWidth(PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isValidVerticalLane(PdfCharacterSet overlapping) {
    return false;
  }

  // ==========================================================================

  @Override
  public float getHorizontalLaneHeight(PdfCharacterSet characters) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isValidHorizontalLane(PdfCharacterSet overlapping) {
    // TODO Auto-generated method stub
    return false;
  }

  // ==========================================================================

  @Override
  public PdfWord pack(PdfCharacterSet characters) {
    return this.wordFactory.create(characters);
  }
}
