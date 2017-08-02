package icecite.models.plain;

import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterStatistician;
import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfCharacterStatistics.PdfCharacterStatisticsFactory;
import icecite.models.PdfColor;
import icecite.models.PdfFontFace;
import icecite.utils.counter.FloatCounter;
import icecite.utils.counter.ObjectCounter;
import icecite.utils.geometric.Rectangle;

/**
 * A plain implementation of {@link PdfCharacterStatistician}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfCharacterStatistician implements PdfCharacterStatistician {
  /**
   * The factory to create instances of {@link PdfCharacterStatistics}.
   */
  protected PdfCharacterStatisticsFactory statisticsFactory;

  /**
   * The default constructor.
   * 
   * @param statisticsFactory
   *        The factory to create instances of {@link PdfCharacterStatistics}.
   */
  @Inject
  public PlainPdfCharacterStatistician(
      PdfCharacterStatisticsFactory statisticsFactory) {
    this.statisticsFactory = statisticsFactory;
  }

  @Override
  public PdfCharacterStatistics compute(PdfCharacterList characters) {
    PdfCharacterStatistics statistics = this.statisticsFactory.create();

    // Count the heights, widths and font sizes of the characters.
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

      // Register the smallest minX value.
      if (rectangle.getMinX() < statistics.getSmallestMinX()) {
        statistics.setSmallestMinX(rectangle.getMinX());
      }

      // Register the smallest minY value.
      if (rectangle.getMinY() < statistics.getSmallestMinY()) {
        statistics.setSmallestMinY(rectangle.getMinY());
      }

      // Register the largest maxX value.
      if (rectangle.getMaxX() > statistics.getLargestMaxX()) {
        statistics.setLargestMaxX(rectangle.getMaxX());
      }

      // Register the largest maxY value.
      if (rectangle.getMaxY() > statistics.getLargestMaxY()) {
        statistics.setLargestMaxY(rectangle.getMaxY());
      }
    }

    // Fill the statistics.
    statistics.setHeightFrequencies(heightsFrequencies);
    statistics.setWidthFrequencies(widthsFrequencies);
    statistics.setFontSizeFrequencies(fontsizeFrequencies);
    statistics.setColorFrequencies(colorFrequencies);
    statistics.setFontFaceFrequencies(fontFaceFrequencies);

    return statistics;
  }

  // ==========================================================================

  @Override
  public PdfCharacterStatistics aggregate(PdfCharacterStatistics... stats) {
    return aggregate(Arrays.asList(stats));
  }

  @Override
  public PdfCharacterStatistics aggregate(List<PdfCharacterStatistics> stats) {
    PdfCharacterStatistics statistics = this.statisticsFactory.create();

    // Count the heights, widths and font sizes of the characters.
    FloatCounter heightsFrequencies = new FloatCounter();
    FloatCounter widthsFrequencies = new FloatCounter();
    FloatCounter fontsizeFrequencies = new FloatCounter();

    // Count the colors and font faces.
    ObjectCounter<PdfColor> colorFrequencies = new ObjectCounter<>();
    ObjectCounter<PdfFontFace> fontFaceFrequencies = new ObjectCounter<>();

    for (PdfCharacterStatistics stat : stats) {
      // Register the height.
      heightsFrequencies.add(stat.getHeightFrequencies());
      // Register the width.
      widthsFrequencies.add(stat.getWidthFrequencies());
      // Register the font face.
      fontFaceFrequencies.add(stat.getFontFaceFrequencies());
      // Register the font size.
      fontsizeFrequencies.add(stat.getFontSizeFrequencies());
      // Register the color.
      colorFrequencies.add(stat.getColorFrequencies());

      // Register the smallest minX value.
      if (stat.getSmallestMinX() < statistics.getSmallestMinX()) {
        statistics.setSmallestMinX(stat.getSmallestMinX());
      }

      // Register the smallest minY value.
      if (stat.getSmallestMinY() < statistics.getSmallestMinY()) {
        statistics.setSmallestMinY(stat.getSmallestMinY());
      }

      // Register the largest maxX value.
      if (stat.getLargestMaxX() > statistics.getLargestMaxX()) {
        statistics.setLargestMaxX(stat.getLargestMaxX());
      }

      // Register the largest maxY value.
      if (stat.getLargestMaxY() > statistics.getLargestMaxY()) {
        statistics.setLargestMaxY(stat.getLargestMaxY());
      }
    }

    // Fill the statistics.
    statistics.setHeightFrequencies(heightsFrequencies);
    statistics.setWidthFrequencies(widthsFrequencies);
    statistics.setFontSizeFrequencies(fontsizeFrequencies);
    statistics.setColorFrequencies(colorFrequencies);
    statistics.setFontFaceFrequencies(fontFaceFrequencies);

    return statistics;
  }
}
