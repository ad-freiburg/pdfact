package pdfact.core.util.statistician;

import java.util.List;

import com.google.inject.Inject;

import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.CharacterStatistic.CharacterStatisticFactory;
import pdfact.core.model.Color;
import pdfact.core.model.FontFace;
import pdfact.core.model.HasCharacterStatistic;
import pdfact.core.model.HasCharacters;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.util.counter.FloatCounter;
import pdfact.core.util.counter.FloatCounter.FloatCounterFactory;
import pdfact.core.util.counter.ObjectCounter;
import pdfact.core.util.counter.ObjectCounter.ObjectCounterFactory;
import pdfact.core.util.list.ElementList;

/**
 * A plain implementation of {@link CharacterStatistician}.
 * 
 * @author Claudius Korzen
 */
public class PlainCharacterStatistician implements CharacterStatistician {
  /**
   * The factory to create instances of {@link CharacterStatistic}.
   */
  protected CharacterStatisticFactory charStatisticFactory;

  /**
   * The factory to create instances of {@link FloatCounterFactory}.
   */
  protected FloatCounterFactory floatCounterFactory;

  /**
   * The factory to create instances of {@link ObjectCounterFactory}.
   */
  protected ObjectCounterFactory<Color> colorCounterFactory;

  /**
   * The factory to create instances of {@link ObjectCounterFactory}.
   */
  protected ObjectCounterFactory<FontFace> fontFaceCounterFactory;

  /**
   * Creates a new statistician to compute statistics about characters.
   * 
   * @param charStatisticFactory
   *        The factory to create instances of {@link CharacterStatistic}.
   * @param floatCounterFactory
   *        The factory to create instances of {@link FloatCounterFactory}.
   * @param colorCounterFactory
   *        The factory to create instances of {@link ObjectCounterFactory}.
   * @param fontFaceCounterFactory
   *        The factory to create instances of {@link ObjectCounterFactory}.
   */
  @Inject
  public PlainCharacterStatistician(
      CharacterStatisticFactory charStatisticFactory,
      FloatCounterFactory floatCounterFactory,
      ObjectCounterFactory<Color> colorCounterFactory,
      ObjectCounterFactory<FontFace> fontFaceCounterFactory) {
    this.charStatisticFactory = charStatisticFactory;
    this.floatCounterFactory = floatCounterFactory;
    this.colorCounterFactory = colorCounterFactory;
    this.fontFaceCounterFactory = fontFaceCounterFactory;
  }

  // ==========================================================================

  @Override
  public CharacterStatistic compute(HasCharacters hasCharacters) {
    return compute(hasCharacters.getCharacters());
  }

  @Override
  public CharacterStatistic compute(ElementList<Character> characters) {
    // Create a new statistic object.
    CharacterStatistic statistic = this.charStatisticFactory.create();

    // Initialize counters for the heights, widths and font sizes.
    FloatCounter heightsFrequencies = this.floatCounterFactory.create();
    FloatCounter widthsFrequencies = this.floatCounterFactory.create();
    FloatCounter fontsizeFrequencies = this.floatCounterFactory.create();

    // Initialize counters for the colors and font faces.
    ObjectCounter<Color> colorFreqs = this.colorCounterFactory.create();
    ObjectCounter<FontFace> fontFreqs = this.fontFaceCounterFactory.create();

    for (Character character : characters) {
      Position position = character.getPosition();
      Rectangle rectangle = position.getRectangle();

      heightsFrequencies.add(rectangle.getHeight());
      widthsFrequencies.add(rectangle.getWidth());
      fontFreqs.add(character.getFontFace());
      fontsizeFrequencies.add(character.getFontFace().getFontSize());
      colorFreqs.add(character.getColor());

      if (rectangle.getMinX() < statistic.getSmallestMinX()) {
        statistic.setSmallestMinX(rectangle.getMinX());
      }

      if (rectangle.getMinY() < statistic.getSmallestMinY()) {
        statistic.setSmallestMinY(rectangle.getMinY());
      }

      if (rectangle.getMaxX() > statistic.getLargestMaxX()) {
        statistic.setLargestMaxX(rectangle.getMaxX());
      }

      if (rectangle.getMaxY() > statistic.getLargestMaxY()) {
        statistic.setLargestMaxY(rectangle.getMaxY());
      }
    }

    // Fill the statistic object.
    statistic.setHeightFrequencies(heightsFrequencies);
    statistic.setWidthFrequencies(widthsFrequencies);
    statistic.setFontSizeFrequencies(fontsizeFrequencies);
    statistic.setColorFrequencies(colorFreqs);
    statistic.setFontFaceFrequencies(fontFreqs);

    return statistic;
  }

  // ==========================================================================

  @Override
  public CharacterStatistic aggregate(
      List<? extends HasCharacterStatistic> stats) {
    // Create new statistic object.
    CharacterStatistic statistic = this.charStatisticFactory.create();

    // Initialize counters for the heights, widths and font sizes.
    FloatCounter heightsFrequencies = this.floatCounterFactory.create();
    FloatCounter widthsFrequencies = this.floatCounterFactory.create();
    FloatCounter fontsizeFrequencies = this.floatCounterFactory.create();

    // Initialize counters for the colors and font faces.
    ObjectCounter<Color> colorFreqs = this.colorCounterFactory.create();
    ObjectCounter<FontFace> fontFreqs = this.fontFaceCounterFactory.create();

    // Aggregate the given statistics.
    for (HasCharacterStatistic s : stats) {
      CharacterStatistic stat = s.getCharacterStatistic();

      heightsFrequencies.add(stat.getHeightFrequencies());
      widthsFrequencies.add(stat.getWidthFrequencies());
      fontFreqs.add(stat.getFontFaceFrequencies());
      fontsizeFrequencies.add(stat.getFontSizeFrequencies());
      colorFreqs.add(stat.getColorFrequencies());

      if (stat.getSmallestMinX() < statistic.getSmallestMinX()) {
        statistic.setSmallestMinX(stat.getSmallestMinX());
      }

      if (stat.getSmallestMinY() < statistic.getSmallestMinY()) {
        statistic.setSmallestMinY(stat.getSmallestMinY());
      }

      if (stat.getLargestMaxX() > statistic.getLargestMaxX()) {
        statistic.setLargestMaxX(stat.getLargestMaxX());
      }

      if (stat.getLargestMaxY() > statistic.getLargestMaxY()) {
        statistic.setLargestMaxY(stat.getLargestMaxY());
      }
    }

    // Fill the statistic object.
    statistic.setHeightFrequencies(heightsFrequencies);
    statistic.setWidthFrequencies(widthsFrequencies);
    statistic.setFontSizeFrequencies(fontsizeFrequencies);
    statistic.setColorFrequencies(colorFreqs);
    statistic.setFontFaceFrequencies(fontFreqs);

    return statistic;
  }
}
