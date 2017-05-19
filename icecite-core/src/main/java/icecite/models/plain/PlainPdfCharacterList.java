package icecite.models.plain;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfColor;
import icecite.models.PdfFont;
import icecite.utils.counter.FloatCounter;
import icecite.utils.counter.ObjectCounter;
import icecite.utils.geometric.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link PdfCharacterList}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterList extends PlainPdfElementList<PdfCharacter>
    implements PdfCharacterList {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -7582729069033134941L;

  /**
   * The counter for the fonts.
   */
  protected ObjectCounter<PdfFont, PdfCharacter> fontCounter;

  /**
   * The counter for the colors.
   */
  protected ObjectCounter<PdfColor, PdfCharacter> colorCounter;

  /**
   * The counter for the font sizes.
   */
  protected FloatCounter<PdfCharacter> fontsizeCounter;

  /**
   * The index of the previous split.
   */
  protected int currentSplitIndex;

  /**
   * The previous portion between index 0 and previousSplitIndex.
   */
  protected PdfCharacterListView currentLeftHalf;

  /**
   * The previous portion between previousSplitIndex and index this.size().
   */
  protected PdfCharacterListView currentRightHalf;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PdfCharacterList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@Rectangle}.
   */
  @AssistedInject
  public PlainPdfCharacterList(RectangleFactory rectangleFactory) {
    super(rectangleFactory);
  }

  /**
   * Creates a new PdfCharacterList.
   * 
   * @param rectangleFactory
   *        The factory to create instances of {@Rectangle}.
   * @param initialCapacity
   *        The initial capacity of this list.
   */
  @AssistedInject
  public PlainPdfCharacterList(RectangleFactory rectangleFactory,
      @Assisted int initialCapacity) {
    super(rectangleFactory, initialCapacity);
  }

  // ==========================================================================

  @Override
  public void clear() {
    super.clear();
    this.fontCounter.clear();
    this.colorCounter.clear();
    this.fontsizeCounter.clear();
  }

  // ==========================================================================
  // Split methods.

  @Override
  public List<? extends PdfCharacterList> split(int splitIndex) {
    PdfCharacterListView leftHalf = new PdfCharacterListView(this, 0,
        splitIndex);
    PdfCharacterListView rightHalf = new PdfCharacterListView(this, splitIndex,
        this.size());
    split(splitIndex, leftHalf, rightHalf);
    return Arrays.asList(leftHalf, rightHalf);
  }

  /**
   * Splits this list at the given index into two halves. Both halves are views
   * of the related portion of the list, that is (1) the portion between index
   * 0, inclusive, and splitIndex, exclusive; and (2) the portion between
   * splitIndex, inclusive, and this.size(), exclusive.
   * 
   * @param splitIndex
   *        The index where to split this list.
   * @param leftHalfToFill
   *        The left half to populate with the counters.
   * @param rightHalfToFill
   *        The left half to populate with the counters.
   */
  public void split(int splitIndex, PdfCharacterListView leftHalfToFill,
      PdfCharacterListView rightHalfToFill) {
    super.split(splitIndex, leftHalfToFill, rightHalfToFill);

    ObjectCounter<PdfFont, PdfCharacter> leftFontCounter, rightFontCounter;
    ObjectCounter<PdfColor, PdfCharacter> leftColorCounter, rightColorCounter;
    FloatCounter<PdfCharacter> leftFontsizeCounter, rightFontsizeCounter;

    if (this.currentLeftHalf != null && this.currentRightHalf != null) {
      leftFontCounter = this.currentLeftHalf.getFontCounter();
      rightFontCounter = this.currentRightHalf.getFontCounter();
      leftColorCounter = this.currentLeftHalf.getColorCounter();
      rightColorCounter = this.currentRightHalf.getColorCounter();
      leftFontsizeCounter = this.currentLeftHalf.getFontsizeCounter();
      rightFontsizeCounter = this.currentRightHalf.getFontsizeCounter();
    } else {
      int initialCapacity = 16; // TODO
      leftFontCounter = new ObjectCounter<>(initialCapacity);
      rightFontCounter = new ObjectCounter<>(initialCapacity);
      leftColorCounter = new ObjectCounter<>(initialCapacity);
      rightColorCounter = new ObjectCounter<>(initialCapacity);
      leftFontsizeCounter = new FloatCounter<>(initialCapacity);
      rightFontsizeCounter = new FloatCounter<>(initialCapacity);
    }

    // Update the counters.
    if (splitIndex < this.currentSplitIndex) {
      for (int i = splitIndex; i < this.currentSplitIndex; i++) {
        PdfCharacter character = get(i);
        if (character == null) {
          continue;
        }

        // Remove elements from the left half.
        leftFontCounter.remove(character.getFont(), character);
        leftColorCounter.remove(character.getColor(), character);
        leftFontsizeCounter.remove(character.getFontSize(), character);

        // Add elements to the right half.
        rightFontCounter.add(character.getFont(), character);
        rightColorCounter.add(character.getColor(), character);
        rightFontsizeCounter.add(character.getFontSize(), character);
      }
    } else if (splitIndex > this.currentSplitIndex) {
      for (int i = this.currentSplitIndex; i < splitIndex; i++) {
        PdfCharacter character = get(i);
        if (character == null) {
          continue;
        }

        // Add elements to the right half.
        leftFontCounter.add(character.getFont(), character);
        leftColorCounter.add(character.getColor(), character);
        leftFontsizeCounter.add(character.getFontSize(), character);

        // Remove elements from the right half.
        rightFontCounter.remove(character.getFont(), character);
        rightColorCounter.remove(character.getColor(), character);
        rightFontsizeCounter.remove(character.getFontSize(), character);
      }
    }

    // Set the counters of the left half.
    leftHalfToFill.setFontCounter(leftFontCounter);
    leftHalfToFill.setColorCounter(leftColorCounter);
    leftHalfToFill.setFontsizeCounter(leftFontsizeCounter);

    // Set the counters of the right half.
    rightHalfToFill.setFontCounter(rightFontCounter);
    rightHalfToFill.setColorCounter(rightColorCounter);
    rightHalfToFill.setFontsizeCounter(rightFontsizeCounter);

    this.currentLeftHalf = leftHalfToFill;
    this.currentRightHalf = rightHalfToFill;
    this.currentSplitIndex = splitIndex;
  }

  // ==========================================================================
  // Register methods.

  /**
   * Registers the given element to this list.
   * 
   * @param character
   *        The element to register.
   */
  @Override
  protected void registerPdfElement(PdfCharacter character) {
    super.registerPdfElement(character);
    if (character == null) {
      return;
    }

    this.fontCounter.add(character.getFont(), character);
    this.colorCounter.add(character.getColor(), character);
    this.fontsizeCounter.add(character.getFontSize(), character);
  }

  @Override
  protected void unregisterPdfElement(Object object) {
    if (object == null) {
      return;
    }

    if (object instanceof PdfCharacter) {
      PdfCharacter character = (PdfCharacter) object;

      this.fontCounter.remove(character.getFont(), object);
      this.colorCounter.remove(character.getColor(), object);
      this.fontsizeCounter.remove(character.getFontSize(), object);
    }
  }

  // ==========================================================================

  @Override
  public PdfFont getMostCommonFont() {
    return this.fontCounter.getMostCommonObject();
  }

  @Override
  public Set<PdfCharacter> getCharactersWithMostCommonFont() {
    return this.fontCounter.getElementsWithMostCommonObject();
  }

  // ==========================================================================

  @Override
  public PdfColor getMostCommonColor() {
    return this.colorCounter.getMostCommonObject();
  }

  @Override
  public Set<PdfCharacter> getCharactersWithMostCommonColor() {
    return this.colorCounter.getElementsWithMostCommonObject();
  }

  // ==========================================================================

  @Override
  public float getMostCommonFontsize() {
    return this.fontsizeCounter.getMostCommonFloat();
  }

  @Override
  public Set<PdfCharacter> getCharactersWithMostCommonFontsize() {
    return this.fontsizeCounter.getElementsWithMostCommonFloat();
  }

  @Override
  public float getAverageFontsize() {
    return this.fontsizeCounter.getAverageFloat();
  }

  /**
   * A sublist of PDF characters.
   * 
   * @author Claudius Korzen
   */
  class PdfCharacterListView extends PdfElementListView<PdfCharacter>
      implements PdfCharacterList {
    /**
     * The serial id.
     */
    private static final long serialVersionUID = 4550840178348993069L;

    /**
     * The counter for fonts.
     */
    protected ObjectCounter<PdfFont, PdfCharacter> fontCounter;

    /**
     * The counter for colors.
     */
    protected ObjectCounter<PdfColor, PdfCharacter> colorCounter;

    /**
     * The counter for font sizes.
     */
    protected FloatCounter<PdfCharacter> fontsizeCounter;

    /**
     * The index of the previous split.
     */
    protected int currentSplitIndex;

    /**
     * The previous portion between index 0 and previousSplitIndex.
     */
    protected PdfCharacterListView currentLeftHalf;

    /**
     * The previous portion between previousSplitIndex and index this.size().
     */
    protected PdfCharacterListView currentRightHalf;

    // ========================================================================
    // Constructors.

    /**
     * Creates a new sublist based on the given parent list.
     * 
     * @param parent
     *        The parent list.
     * @param fromIndex
     *        The start index.
     * @param toIndex
     *        The end index.
     */
    PdfCharacterListView(PdfCharacterList parent, int fromIndex, int toIndex) {
      super(parent, fromIndex, toIndex);
    }

    // ========================================================================
    // Custom methods.

    /**
     * Returns the font counter of this view.
     * 
     * @return The font counter of this view.
     */
    public ObjectCounter<PdfFont, PdfCharacter> getFontCounter() {
      return this.fontCounter;
    }

    /**
     * Sets the font counter of this view.
     * 
     * @param counter
     *        The font counter of this view.
     */
    public void setFontCounter(ObjectCounter<PdfFont, PdfCharacter> counter) {
      this.fontCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the color counter of this view.
     * 
     * @return The color counter of this view.
     */
    public ObjectCounter<PdfColor, PdfCharacter> getColorCounter() {
      return this.colorCounter;
    }

    /**
     * Sets the color counter of this view.
     * 
     * @param counter
     *        The color counter of this view.
     */
    public void setColorCounter(ObjectCounter<PdfColor, PdfCharacter> counter) {
      this.colorCounter = counter;
    }

    // ========================================================================

    /**
     * Returns the font size counter of this view.
     * 
     * @return The font size counter of this view.
     */
    public FloatCounter<PdfCharacter> getFontsizeCounter() {
      return this.fontsizeCounter;
    }

    /**
     * Sets the font size counter of this view.
     * 
     * @param counter
     *        The font size counter of this view.
     */
    public void setFontsizeCounter(FloatCounter<PdfCharacter> counter) {
      this.fontsizeCounter = counter;
    }

    // ========================================================================

    @Override
    public List<? extends PdfCharacterList> split(int splitIndex) {
      PdfCharacterListView leftHalf = new PdfCharacterListView(this, 0,
          splitIndex);
      PdfCharacterListView rightHalf = new PdfCharacterListView(this,
          splitIndex, this.size());
      split(splitIndex, leftHalf, rightHalf);
      return Arrays.asList(leftHalf, rightHalf);
    }

    /**
     * Splits this list at the given index into two halves. Both halves are
     * views of the related portion of the list, that is (1) the portion
     * between index 0, inclusive, and splitIndex, exclusive; and (2) the
     * portion between splitIndex, inclusive, and this.size(), exclusive.
     * 
     * @param splitIndex
     *        The index where to split this list.
     * @param leftHalfToFill
     *        The left half to populate with the counters.
     * @param rightHalfToFill
     *        The left half to populate with the counters.
     */
    public void split(int splitIndex, PdfCharacterListView leftHalfToFill,
        PdfCharacterListView rightHalfToFill) {
      super.split(splitIndex, leftHalfToFill, rightHalfToFill);

      ObjectCounter<PdfFont, PdfCharacter> leftFontCounter, rightFontCounter;
      ObjectCounter<PdfColor, PdfCharacter> leftColorCounter, rightColorCounter;
      FloatCounter<PdfCharacter> leftFontsizeCounter, rightFontsizeCounter;

      if (this.currentLeftHalf != null && this.currentRightHalf != null) {
        leftFontCounter = this.currentLeftHalf.getFontCounter();
        rightFontCounter = this.currentRightHalf.getFontCounter();
        leftColorCounter = this.currentLeftHalf.getColorCounter();
        rightColorCounter = this.currentRightHalf.getColorCounter();
        leftFontsizeCounter = this.currentLeftHalf.getFontsizeCounter();
        rightFontsizeCounter = this.currentRightHalf.getFontsizeCounter();
      } else {
        int initialCapacity = 16; // TODO
        leftFontCounter = new ObjectCounter<>(initialCapacity);
        rightFontCounter = new ObjectCounter<>(initialCapacity);
        leftColorCounter = new ObjectCounter<>(initialCapacity);
        rightColorCounter = new ObjectCounter<>(initialCapacity);
        leftFontsizeCounter = new FloatCounter<>(initialCapacity);
        rightFontsizeCounter = new FloatCounter<>(initialCapacity);
      }

      // Update the counters.
      if (splitIndex < this.currentSplitIndex) {
        for (int i = splitIndex; i < this.currentSplitIndex; i++) {
          PdfCharacter character = get(i);
          if (character == null) {
            continue;
          }

          // Remove elements from the left half.
          leftFontCounter.remove(character.getFont(), character);
          leftColorCounter.remove(character.getColor(), character);
          leftFontsizeCounter.remove(character.getFontSize(), character);

          // Add elements to the right half.
          rightFontCounter.add(character.getFont(), character);
          rightColorCounter.add(character.getColor(), character);
          rightFontsizeCounter.add(character.getFontSize(), character);
        }
      } else if (splitIndex > this.currentSplitIndex) {
        for (int i = this.currentSplitIndex; i < splitIndex; i++) {
          PdfCharacter character = get(i);
          if (character == null) {
            continue;
          }

          // Add elements to the right half.
          leftFontCounter.add(character.getFont(), character);
          leftColorCounter.add(character.getColor(), character);
          leftFontsizeCounter.add(character.getFontSize(), character);

          // Remove elements from the right half.
          rightFontCounter.remove(character.getFont(), character);
          rightColorCounter.remove(character.getColor(), character);
          rightFontsizeCounter.remove(character.getFontSize(), character);
        }
      }

      // Set the counters of the left half.
      leftHalfToFill.setFontCounter(leftFontCounter);
      leftHalfToFill.setColorCounter(leftColorCounter);
      leftHalfToFill.setFontsizeCounter(leftFontsizeCounter);

      // Set the counters of the right half.
      rightHalfToFill.setFontCounter(rightFontCounter);
      rightHalfToFill.setColorCounter(rightColorCounter);
      rightHalfToFill.setFontsizeCounter(rightFontsizeCounter);

      this.currentLeftHalf = leftHalfToFill;
      this.currentRightHalf = rightHalfToFill;
      this.currentSplitIndex = splitIndex;
    }

    // ========================================================================

    @Override
    public PdfFont getMostCommonFont() {
      return this.fontCounter.getMostCommonObject();
    }

    @Override
    public Set<PdfCharacter> getCharactersWithMostCommonFont() {
      return this.fontCounter.getElementsWithMostCommonObject();
    }

    // ========================================================================

    @Override
    public PdfColor getMostCommonColor() {
      return this.colorCounter.getMostCommonObject();
    }

    @Override
    public Set<PdfCharacter> getCharactersWithMostCommonColor() {
      return this.colorCounter.getElementsWithMostCommonObject();
    }

    // ========================================================================

    @Override
    public float getMostCommonFontsize() {
      return this.fontsizeCounter.getMostCommonFloat();
    }

    @Override
    public Set<PdfCharacter> getCharactersWithMostCommonFontsize() {
      return this.fontsizeCounter.getElementsWithMostCommonFloat();
    }

    @Override
    public float getAverageFontsize() {
      return this.fontsizeCounter.getAverageFloat();
    }
  }
}
