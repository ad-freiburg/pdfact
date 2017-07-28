package icecite.tokenize.lines;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.models.PdfTextLineList;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.tokenize.xycut.XYCut;
import icecite.utils.character.PdfCharacterUtils;
import icecite.utils.counter.FloatCounter;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Line.LineFactory;

// TODO: Rework.

/**
 * An implementation of {@link PdfTextLineTokenizer} based on XYCut.
 * 
 * @author Claudius Korzen
 */
public class XYCutPdfTextLineTokenizer extends XYCut<PdfTextLine>
    implements PdfTextLineTokenizer {
  /**
   * The factory to create instances of {@link PdfPositionFactory}.
   */
  protected PdfPositionFactory positionFactory;

  /**
   * The factory to create instances of PdfTextLineList.
   */
  protected PdfTextLineListFactory textLineListFactory;

  /**
   * The factory to create instances of PdfTextLine.
   */
  protected PdfTextLineFactory textLineFactory;

  /**
   * The factory to create instances of Line.
   */
  protected LineFactory lineFactory;

  /**
   * The overlapping characters of the previous sweep iteration.
   */
  protected PdfCharacterList prevOverlappingChars;

  /**
   * The flag to indicate whether the lane in the previous sweep iteration was
   * valid.
   */
  protected boolean prevIsValidHorizontalLane;

  /**
   * The flag to indicate whether the previous overlapping characters consist
   * only of ascenders, descenders, sub- or superscripts.
   */
  protected boolean prevContainsOnlyCriticalChars;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new text line tokenizer.
   * @param positionFactory 
   *        The factory to create instance of PdfPosition.
   * @param textLineListFactory
   *        The factory to create instance of {@link PdfTextLineList}.
   * @param textLineFactory
   *        The factory to create instance of {@link PdfTextLine}.
   * @param lineFactory
   *        The factory to create instances of {@link Line}.
   */
  @Inject
  public XYCutPdfTextLineTokenizer(PdfPositionFactory positionFactory,
      PdfTextLineListFactory textLineListFactory,
      PdfTextLineFactory textLineFactory, LineFactory lineFactory) {
    super();
    this.positionFactory = positionFactory;
    this.textLineListFactory = textLineListFactory;
    this.textLineFactory = textLineFactory;
    this.lineFactory = lineFactory;
  }

  // ==========================================================================

  @Override
  public PdfTextLineList tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList area) {
    PdfTextLineList textLines = this.textLineListFactory.create();
    cut(pdf, page, area, textLines);
    return textLines;
  }

  // ==========================================================================

  @Override
  public float assessVerticalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    return -1;
  }

  // ==========================================================================

  @Override
  public float assessHorizontalCut(PdfDocument pdf, PdfPage page,
      List<PdfCharacterList> halves) {
    PdfCharacterList u = halves.get(0);
    PdfCharacterList l = halves.get(1);

    float height = u.getRectangle().getMinY() - l.getRectangle().getMaxY();
    return height;
  }

  // ==========================================================================

  @Override
  public PdfTextLine pack(PdfPage page, PdfCharacterList chars) {
    PdfTextLine line = this.textLineFactory.create(chars);
    line.setPosition(this.positionFactory.create(page, chars.getRectangle()));
    line.setBaseline(computeBaseline(line));
    return line;
  }

  // ==========================================================================

  /**
   * Computes the baseline for the given text line.
   * 
   * @param textLine
   *        The text line to process.
   * @return The base line of the given text line.
   */
  protected Line computeBaseline(PdfTextLine textLine) {
    // Compute the most common minY value among all baseline characters.
    FloatCounter minYValuesOfBaselineCharacters = new FloatCounter();
    for (PdfCharacter character : textLine.getCharacters()) {
      if (PdfCharacterUtils.isBaselineCharacter(character)) {
        minYValuesOfBaselineCharacters.add(character.getRectangle().getMinY());
      }
    }
    float startX = textLine.getRectangle().getMinX();
    float endX = textLine.getRectangle().getMaxX();
    float y = minYValuesOfBaselineCharacters.getMostCommonFloat();

    return this.lineFactory.create(startX, y, endX, y);
  }
}
