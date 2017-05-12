package icecite.models.plain;

import java.util.Collection;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterSet;
import icecite.models.PdfColor;
import icecite.models.PdfElementSet;
import icecite.models.PdfFont;
import icecite.utils.counter.FloatCounter;
import icecite.utils.counter.ObjectCounter;
import icecite.utils.geometric.Rectangle.RectangleFactory;

/**
 * An implementation of {@link PdfElementSet} based on
 * {@link PlainPdfElementSet}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterSet extends PlainPdfElementSet<PdfCharacter>
    implements PdfCharacterSet {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -8634251754322444722L;

  /**
   * The counter for the colors of the characters.
   */
  protected ObjectCounter<PdfColor> colorCounter;

  /**
   * The counter for the fonts of the characters.
   */
  protected ObjectCounter<PdfFont> fontCounter;

  /**
   * The counter for the font sizes of the characters.
   */
  protected FloatCounter fontsizeCounter;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new set of characters.
   * 
   * @param rectangleFactory
   *        The factory to create instance of {@Rectangle}.
   */
  @AssistedInject
  public PlainPdfCharacterSet(RectangleFactory rectangleFactory) {
    super(rectangleFactory);
    this.colorCounter = new ObjectCounter<>();
    this.fontCounter = new ObjectCounter<>();
    this.fontsizeCounter = new FloatCounter();
  }

  /**
   * Creates a new set of characters.
   * 
   * @param rectangleFactory
   *        The factory to create instance of {@Rectangle}.
   * @param characters
   *        The characters of the set to create.
   */
  @AssistedInject
  public PlainPdfCharacterSet(RectangleFactory rectangleFactory,
      @Assisted Collection<PdfCharacter> characters) {
    this(rectangleFactory);
    addAll(characters);
  }

  // ==========================================================================

  /**
   * Adds the given character to all counters of this set.
   * 
   * @param e
   *        The character to add.
   */
  protected void addToCounters(PdfCharacter e) {
    super.addToCounters(e);
    this.colorCounter.add(e.getColor());
    this.fontCounter.add(e.getFont());
    this.fontsizeCounter.add(e.getFontSize());
  }

  /**
   * Removes the given object from all counters of this set.
   * 
   * @param o
   *        The object to remove.
   */
  protected void removeFromCounters(Object o) {
    super.removeFromCounters(o);
    if (o instanceof PdfCharacter) {
      PdfCharacter c = (PdfCharacter) o;
      this.colorCounter.discount(c.getColor());
      this.fontCounter.discount(c.getFont());
      this.fontsizeCounter.discount(c.getFontSize());
    }
  }

  /**
   * Clears the counters of this set.
   */
  protected void clearCounters() {
    this.colorCounter.clear();
    this.fontCounter.clear();
    this.fontsizeCounter.clear();
  }

  // ==========================================================================
  // Getter methods.

  @Override
  public PdfColor getMostCommonColor() {
    return this.colorCounter.getMostFrequentObject();
  }

  @Override
  public PdfFont getMostCommonFont() {
    return this.fontCounter.getMostFrequentObject();
  }

  @Override
  public float getMostCommonFontsize() {
    return this.fontsizeCounter.getMostFrequentFloat();
  }

  @Override
  public float getAverageFontsize() {
    return this.fontsizeCounter.getAverageValue();
  }
}
