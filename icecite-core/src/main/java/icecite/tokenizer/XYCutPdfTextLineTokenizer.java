package icecite.tokenizer;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacterList;
import icecite.models.PdfDocument;
import icecite.models.PdfPage;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.tokenizer.xycut.XYCut;

// TODO: Rework.

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
   * 
   * @param textLineFactory
   *        The factory to create instance of {@link PdfTextLine}.
   */
  @Inject
  public XYCutPdfTextLineTokenizer(PdfTextLineFactory textLineFactory) {
    super();
    this.textLineFactory = textLineFactory;
  }

  // ==========================================================================

  @Override
  public List<PdfTextLine> tokenize(PdfDocument pdf, PdfPage page,
      PdfCharacterList characters) {
    return cut(pdf, page, characters);
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

    float height = u.getBoundingBox().getMinY() - l.getBoundingBox().getMaxY();
    return height;
  }

  // ==========================================================================

  @Override
  public PdfTextLine pack(PdfCharacterList characters) {
    return this.textLineFactory.create(characters);
  }

  // ==========================================================================

  // /**
  // * Decides if the given lane is valid, given that the set of overlapping
  // * characters is equal to the previous overlapping characters.
  // *
  // * @param pdf
  // * The PDF document.
  // * @param page
  // * The page in which the characters are located.
  // * @param upper
  // * The characters above the lane.
  // * @param overlap
  // * The characters that overlap the lane.
  // * @param lower
  // * The characters below the lane.
  // * @return True if the lane that overlaps the given characters is valid.
  // */
  // protected boolean handleEqualOverlappingChars(PdfDocument pdf, PdfPage
  // page,
  // PdfCharacterList upper, PdfCharacterList overlap, PdfCharacterList lower)
  // {
  // PdfCharacterList pageCharacters = page.getCharacters();
  // boolean onlyAscDesc = containsOnlyAscendersDescenders(overlap);
  // boolean onlyScripts = containsOnlyScriptCharacters(overlap,
  // pageCharacters);
  // // Obtain if the elements contains only ascenders, descenders, sub- and
  // // super-scripts (because they prefer to exceed line boundaries).
  // this.prevContainsOnlyCriticalChars = onlyAscDesc || onlyScripts;
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  //
  // return this.prevIsValidHorizontalLane;
  // }
  //
  // /**
  // * Decides if the given lane is valid, given that the lane does *not*
  // overlap
  // * any characters.
  // *
  // * @param pdf
  // * The PDF document.
  // * @param page
  // * The page in which the characters are located.
  // * @param upper
  // * The characters above the lane.
  // * @param overlap
  // * The characters that overlap the lane.
  // * @param lower
  // * The characters below the lane.
  // * @return True if the lane that overlaps the given characters is valid.
  // */
  // protected boolean handleEmptyOverlappingChars(PdfDocument pdf, PdfPage
  // page,
  // PdfCharacterList upper, PdfCharacterList overlap, PdfCharacterList lower)
  // {
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  // this.prevContainsOnlyCriticalChars = true; // (elements is empty)
  // // The lane is valid if it *doesn't* split associated elements.
  // this.prevIsValidHorizontalLane = !separatesConsecutiveCharacters(upper,
  // lower);
  //
  // return this.prevIsValidHorizontalLane;
  // }
  //
  // /**
  // * Decides if the given lane is valid, given that the lane *does* overlap
  // any
  // * characters.
  // *
  // * @param pdf
  // * The PDF document.
  // * @param page
  // * The page in which the characters are located.
  // * @param upper
  // * The characters above the lane.
  // * @param overlap
  // * The characters that overlap the lane.
  // * @param lower
  // * The characters below the lane.
  // * @return True if the lane that overlaps the given characters is valid.
  // */
  // protected boolean handleNonEmptyOverlappingChars(PdfDocument pdf,
  // PdfPage page, PdfCharacterList upper, PdfCharacterList overlap,
  // PdfCharacterList lower) {
  // PdfCharacterList pageChars = page.getCharacters();
  // // Obtain if the elements contains only ascenders, descenders and small
  // // characters (because they prefer to exceed line boundaries).
  // boolean onlyAscDesc = containsOnlyAscendersDescenders(overlap);
  // boolean onlyScripts = containsOnlyScriptCharacters(overlap, pageChars);
  //
  // if (onlyAscDesc || onlyScripts) {
  // // The current overlapping elements consist only of ascenders and
  // // descenders. If also the previous overlapping elements consist only of
  // // ascenders/descenders and the set of current elements is a subset
  // // of the previous overlapping elements, return the previous computed
  // // result.
  //
  // if (CollectionUtils.isNullOrEmpty(this.prevOverlappingChars)) {
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  // this.prevIsValidHorizontalLane = false;
  // this.prevContainsOnlyCriticalChars = true;
  // return this.prevIsValidHorizontalLane;
  // }
  //
  // if (this.prevContainsOnlyCriticalChars) {
  // if (isSubSet(overlap, this.prevOverlappingChars)) {
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  // this.prevContainsOnlyCriticalChars = true;
  // return this.prevIsValidHorizontalLane;
  // }
  // if (haveNoElementsInCommon(this.prevOverlappingChars, overlap)) {
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  // this.prevIsValidHorizontalLane = !separatesConsecutiveCharacters(
  // upper, lower);
  // this.prevContainsOnlyCriticalChars = true;
  // return this.prevIsValidHorizontalLane;
  // }
  // }
  //
  // // The current overlapping elements consist only of ascenders and
  // // descenders. If the previous overlapping elements *don't* consist only
  // // of ascenders/descenders, the lane is valid if it doesn't split
  // // associated elements.
  // if (!this.prevContainsOnlyCriticalChars) {
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  // this.prevIsValidHorizontalLane = !separatesConsecutiveCharacters(upper,
  // lower);
  // this.prevContainsOnlyCriticalChars = true;
  // return this.prevIsValidHorizontalLane;
  // }
  // }
  //
  // // The current overlapping elements consists of at least one
  // // non-ascender/descender. The lane is not valid.
  // this.prevOverlappingChars = this.characterListFactory.create(overlap);
  // this.prevIsValidHorizontalLane = false;
  // this.prevContainsOnlyCriticalChars = onlyAscDesc || onlyScripts;
  // return false;
  // }
  //
  // //
  // ==========================================================================
  // // Utility methods.
  //
  // /**
  // * Checks if the two given character sets contain the same characters.
  // *
  // * @param chars1
  // * The first character set.
  // * @param chars2
  // * The second character set.
  // * @return True, if the two given character sets contain the same
  // characters;
  // * False otherwise.
  // */
  // protected boolean equals(PdfCharacterList chars1, PdfCharacterList chars2)
  // {
  // if (chars1 == null || chars2 == null) {
  // return false;
  // }
  //
  // if (chars1.size() != chars2.size()) {
  // return false;
  // }
  //
  // for (PdfCharacter character : chars1) {
  // if (!chars2.contains(character)) {
  // return false;
  // }
  // }
  //
  // return true;
  // }
  //
  // /**
  // * Checks if the first set of characters is a subset of the second set of
  // * characters.
  // *
  // * @param chars1
  // * The first set of characters.
  // * @param chars2
  // * The second set of characters.
  // * @return True, if chars1 is a subset of chars2; False otherwise.
  // */
  // protected boolean isSubSet(PdfCharacterList chars1, PdfCharacterList
  // chars2) {
  // if (chars1 == null || chars2 == null) {
  // return false;
  // }
  //
  // for (PdfCharacter character : chars1) {
  // if (!chars2.contains(character)) {
  // return false;
  // }
  // }
  // return true;
  // }
  //
  // /**
  // * Returns true, if the two given set of characters have no characters in
  // * common.
  // *
  // * @param chars1
  // * The first set of characters.
  // * @param chars2
  // * The second set of characters.
  // * @return True if the two given set of characters have no characters in
  // * common; False otherwise.
  // */
  // protected boolean haveNoElementsInCommon(PdfCharacterList chars1,
  // PdfCharacterList chars2) {
  // if (chars1 == null || chars2 == null) {
  // return false;
  // }
  //
  // if (chars1.isEmpty() && chars2.isEmpty()) {
  // return true;
  // }
  //
  // PdfCharacterList smallerSet = chars1;
  // PdfCharacterList largerSet = chars2;
  // if (chars1.size() > chars2.size()) {
  // smallerSet = chars2;
  // largerSet = chars1;
  // }
  //
  // for (PdfCharacter character : smallerSet) {
  // if (largerSet.contains(character)) {
  // return false;
  // }
  // }
  // return true;
  // }
  //
  // /**
  // * Checks if the given set of characters consists only of ascenders (like
  // * "h") and descenders (like "g").
  // *
  // * @param chars
  // * The set of characters to check.
  // * @return True if the given set of characters consists only of ascenders
  // and
  // * descenders.
  // */
  // protected boolean containsOnlyAscendersDescenders(PdfCharacterList chars)
  // {
  // for (PdfCharacter character : chars) {
  // if (!PdfCharacterUtils.isAscenderOrDescender(character)) {
  // return false;
  // }
  // }
  // return true;
  // }
  //
  // /**
  // * Returns true, if the given set of characters consists only of sub- or
  // * superscripts.
  // *
  // * @param chars
  // * The set of characters to process.
  // * @param referenceChars
  // * The set of characters to be used for reference.
  // * @return true, if the given set of characters consists only of sub- or
  // * superscripts; False otherwise.
  // */
  // protected boolean containsOnlyScriptCharacters(PdfCharacterList chars,
  // PdfCharacterList referenceChars) {
  // float mostCommonFontsize = referenceChars.getMostCommonFontsize();
  // mostCommonFontsize = MathUtils.round(mostCommonFontsize, 0);
  // for (PdfCharacter character : chars) {
  // float fontsize = MathUtils.round(character.getFontSize(), 0);
  // if (fontsize >= mostCommonFontsize) {
  // return false;
  // }
  // }
  // return true;
  // }
  //
  // /**
  // * Returns true if the given lane splits associated elements in the given
  // * area.
  // *
  // * @param upper
  // * The first character set.
  // * @param lower
  // * The second character set.
  // * @return True if there is such a character pair, false otherwise.
  // */
  // // TODO: Check correctness.
  // protected boolean separatesConsecutiveCharacters(PdfCharacterList upper,
  // PdfCharacterList lower) {
  //
  // // Find the element with highest extraction order number in upper half.
  // int highestUpperElementNum = -Integer.MAX_VALUE;
  // for (PdfCharacter character : upper) {
  // if (character.getExtractionOrderNumber() > highestUpperElementNum) {
  // highestUpperElementNum = character.getExtractionOrderNumber();
  // }
  // }
  //
  // // Find the element with highest extraction order number in lower half.
  // int lowestLowerElementNum = Integer.MAX_VALUE;
  // for (PdfCharacter character : lower) {
  // if (character.getExtractionOrderNumber() < lowestLowerElementNum) {
  // lowestLowerElementNum = character.getExtractionOrderNumber();
  // }
  // }
  //
  // return highestUpperElementNum > lowestLowerElementNum;
  // }
}
