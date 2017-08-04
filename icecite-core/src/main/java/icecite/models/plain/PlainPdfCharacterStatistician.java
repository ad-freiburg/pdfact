package icecite.models.plain;

import java.util.List;

import com.google.inject.Inject;

import icecite.models.HasCharacterStatistic;
import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistic;
import icecite.models.PdfCharacterStatistic.PdfCharacterStatisticFactory;
import icecite.models.PdfColor;
import icecite.models.PdfFontFace;
import icecite.utils.counter.FloatCounter;
import icecite.utils.counter.ObjectCounter;
import icecite.utils.geometric.Rectangle;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfCharacterStatistician}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterStatistician implements PdfCharacterStatistician {
  /**
   * The factory to create instances of {@link PdfCharacterStatistic}.
   */
  protected PdfCharacterStatisticFactory statisticFactory;

  /**
   * Creates a new statistician to compute statistics about characters.
   * 
   * @param factory
   *        The factory to create instances of {@link PdfCharacterStatistic}.
   */
  @Inject
  public PlainPdfCharacterStatistician(PdfCharacterStatisticFactory factory) {
    this.statisticFactory = factory;
  }

  @Override
  public PdfCharacterStatistic compute(PdfCharacterList characters) {
    // Create new statistic object.
    PdfCharacterStatistic statistic = this.statisticFactory.create();

    // Count the heights, widths and font sizes.
    FloatCounter heightsFrequencies = new FloatCounter();
    FloatCounter widthsFrequencies = new FloatCounter();
    FloatCounter fontsizeFrequencies = new FloatCounter();

    // Count the colors and font faces.
    ObjectCounter<PdfColor> colorFrequencies = new ObjectCounter<>();
    ObjectCounter<PdfFontFace> fontFaceFrequencies = new ObjectCounter<>();

    for (PdfCharacter character : characters) {
      Rectangle rectangle = character.getRectangle();

      // Register the height.
      heightsFrequencies.add(rectangle.getHeight());
      // Register the width.
      widthsFrequencies.add(rectangle.getWidth());
      // Register the font face.
      fontFaceFrequencies.add(character.getFontFace());
      // Register the font size.
      fontsizeFrequencies.add(character.getFontFace().getFontSize());
      // Register the color.
      colorFrequencies.add(character.getColor());

      // Register the minX value.
      if (rectangle.getMinX() < statistic.getSmallestMinX()) {
        statistic.setSmallestMinX(rectangle.getMinX());
      }

      // Register the minY value.
      if (rectangle.getMinY() < statistic.getSmallestMinY()) {
        statistic.setSmallestMinY(rectangle.getMinY());
      }

      // Register the maxX value.
      if (rectangle.getMaxX() > statistic.getLargestMaxX()) {
        statistic.setLargestMaxX(rectangle.getMaxX());
      }

      // Register the maxY value.
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
  public PdfCharacterStatistic combine(
      List<? extends HasCharacterStatistic> stats) {
    // Create new statistic object.
    PdfCharacterStatistic statistic = this.statisticFactory.create();

    // Count the heights, widths and font sizes.
    FloatCounter heightsFrequencies = new FloatCounter();
    FloatCounter widthsFrequencies = new FloatCounter();
    FloatCounter fontsizeFrequencies = new FloatCounter();

    // Count the colors and font faces.
    ObjectCounter<PdfColor> colorFrequencies = new ObjectCounter<>();
    ObjectCounter<PdfFontFace> fontFaceFrequencies = new ObjectCounter<>();

    for (HasCharacterStatistic s : stats) {
      PdfCharacterStatistic stat = s.getCharacterStatistic();

      // Register the full height frequencies.
      heightsFrequencies.add(stat.getHeightFrequencies());
      // Register the full width frequencies.
      widthsFrequencies.add(stat.getWidthFrequencies());
      // Register the full font face frequencies.
      fontFaceFrequencies.add(stat.getFontFaceFrequencies());
      // Register the full font size frequencies.
      fontsizeFrequencies.add(stat.getFontSizeFrequencies());
      // Register the full color frequencies.
      colorFrequencies.add(stat.getColorFrequencies());

      // Register the minX value.
      if (stat.getSmallestMinX() < statistic.getSmallestMinX()) {
        statistic.setSmallestMinX(stat.getSmallestMinX());
      }

      // Register the minY value.
      if (stat.getSmallestMinY() < statistic.getSmallestMinY()) {
        statistic.setSmallestMinY(stat.getSmallestMinY());
      }

      // Register the maxX value.
      if (stat.getLargestMaxX() > statistic.getLargestMaxX()) {
        statistic.setLargestMaxX(stat.getLargestMaxX());
      }

      // Register the maxY value.
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
