package pdfact.util.statistician.plain;

import java.util.List;

import com.google.inject.Inject;

import pdfact.model.Position;
import pdfact.model.Rectangle;
import pdfact.model.Character;
import pdfact.model.CharacterStatistic;
import pdfact.model.CharacterStatistic.CharacterStatisticFactory;
import pdfact.util.counter.FloatCounter;
import pdfact.util.counter.ObjectCounter;
import pdfact.util.list.CharacterList;
import pdfact.util.statistician.CharacterStatistician;
import pdfact.model.Color;
import pdfact.model.FontFace;
import pdfact.model.HasCharacterStatistic;
import pdfact.model.HasCharacters;

/**
 * A plain implementation of {@link CharacterStatistician}.
 * 
 * @author Claudius Korzen
 */
public class PlainCharacterStatistician implements CharacterStatistician {
  /**
   * The factory to create instances of {@link CharacterStatistic}.
   */
  protected CharacterStatisticFactory factory;

  /**
   * Creates a new statistician to compute statistics about characters.
   * 
   * @param factory
   *        The factory to create instances of {@link CharacterStatistic}.
   */
  @Inject
  public PlainCharacterStatistician(CharacterStatisticFactory factory) {
    this.factory = factory;
  }

  // ==========================================================================

  @Override
  public CharacterStatistic compute(HasCharacters hasCharacters) {
    return compute(hasCharacters.getCharacters());
  }

  @Override
  public CharacterStatistic compute(CharacterList characters) {
    // Create a new statistic object.
    CharacterStatistic statistic = this.factory.create();

    // Initialize counters for the heights, widths and font sizes.
    FloatCounter heightsFrequencies = new FloatCounter();
    FloatCounter widthsFrequencies = new FloatCounter();
    FloatCounter fontsizeFrequencies = new FloatCounter();

    // Initialize counters for the colors and font faces.
    ObjectCounter<Color> colorFrequencies = new ObjectCounter<>();
    ObjectCounter<FontFace> fontFaceFrequencies = new ObjectCounter<>();

    for (Character character : characters) {
      Position position = character.getPosition(); 
      Rectangle rectangle = position.getRectangle();

      heightsFrequencies.add(rectangle.getHeight());
      widthsFrequencies.add(rectangle.getWidth());
      fontFaceFrequencies.add(character.getFontFace());
      fontsizeFrequencies.add(character.getFontFace().getFontSize());
      colorFrequencies.add(character.getColor());

      // TODO: Move the computation of smallest/largest values into counters.
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
    statistic.setColorFrequencies(colorFrequencies);
    statistic.setFontFaceFrequencies(fontFaceFrequencies);

    return statistic;
  }

  // ==========================================================================

  @Override
  public CharacterStatistic aggregate(
      List<? extends HasCharacterStatistic> stats) {
    // Create new statistic object.
    CharacterStatistic statistic = this.factory.create();

    // Initialize counters for the heights, widths and font sizes.
    FloatCounter heightsFrequencies = new FloatCounter();
    FloatCounter widthsFrequencies = new FloatCounter();
    FloatCounter fontsizeFrequencies = new FloatCounter();

    // Initialize counters for the colors and font faces.
    ObjectCounter<Color> colorFrequencies = new ObjectCounter<>();
    ObjectCounter<FontFace> fontFaceFrequencies = new ObjectCounter<>();

    // Aggregate the given statistics.
    for (HasCharacterStatistic s : stats) {
      CharacterStatistic stat = s.getCharacterStatistic();

      heightsFrequencies.add(stat.getHeightFrequencies());
      widthsFrequencies.add(stat.getWidthFrequencies());
      fontFaceFrequencies.add(stat.getFontFaceFrequencies());
      fontsizeFrequencies.add(stat.getFontSizeFrequencies());
      colorFrequencies.add(stat.getColorFrequencies());

      // TODO: Move the computation of smallest/largest values into counters.
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
    statistic.setColorFrequencies(colorFrequencies);
    statistic.setFontFaceFrequencies(fontFaceFrequencies);

    return statistic;
  }
}
