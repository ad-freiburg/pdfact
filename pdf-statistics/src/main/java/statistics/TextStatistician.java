package statistics;

import java.util.Arrays;
import java.util.List;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.counter.IntCounter;
import de.freiburg.iif.counter.ObjectCounter;
import de.freiburg.iif.math.MathUtils;
import model.HasText;
import model.HasTextStatistics;
import model.PdfColor;
import model.PdfFont;
import model.PlainTextStatistics;
import model.TextStatistics;

/**
 * Some statistics about the text of an object.
 * 
 * @author Claudius Korzen
 */
public class TextStatistician {
  /**
   * Computes text statistics for the given object.
   */
  public static TextStatistics compute(HasText obj) {
    return compute(Arrays.asList(obj));
  }

  /**
   * Computes text statistics for the given list of objects.
   */
  public static TextStatistics compute(List<? extends HasText> objs) {
    PlainTextStatistics statistics = new PlainTextStatistics();

    FloatCounter fontsizesCounter = new FloatCounter();
    IntCounter asciiCounter = new IntCounter();
    IntCounter digitsCounter = new IntCounter();
    ObjectCounter<PdfFont> fontsCounter = new ObjectCounter<>();
    ObjectCounter<PdfColor> colorsCounter = new ObjectCounter<>();

    for (HasText obj : objs) {
      fontsizesCounter.add(MathUtils.round(obj.getFontsize(), 1));
      asciiCounter.add(obj.isAscii() ? 1 : 0);
      digitsCounter.add(obj.isDigit() ? 1 : 0);
      fontsCounter.add(obj.getFont());
      colorsCounter.add(obj.getColor());
    }

    statistics.setAvgFontsize(fontsizesCounter.getAverageValue());
    statistics.setAsciiRatio(asciiCounter.getAverageValue());
    statistics.setDigitsRatio(digitsCounter.getAverageValue());
    statistics.setDigitsCounter(digitsCounter);
    statistics.setAsciiCounter(asciiCounter);
    statistics.setMostCommonFontsize(fontsizesCounter.getMostFrequentFloat());
    statistics.setFontsizesCounter(fontsizesCounter);
    statistics.setMostCommonFont(fontsCounter.getMostFrequentObject());
    statistics.setFontsCounter(fontsCounter);
    statistics.setMostCommonColor(colorsCounter.getMostFrequentObject());
    statistics.setColorsCounter(colorsCounter);

    return statistics;
  }

  /**
   * Accumulates the given statistics to a single statistic.
   */
  public static TextStatistics accumulate(
      List<? extends HasTextStatistics> objs) {
    PlainTextStatistics statistics = new PlainTextStatistics();

    FloatCounter fontsizesCounter = new FloatCounter();
    IntCounter asciiCounter = new IntCounter();
    IntCounter digitsCounter = new IntCounter();
    ObjectCounter<PdfFont> fontsCounter = new ObjectCounter<>();
    ObjectCounter<PdfColor> colorsCounter = new ObjectCounter<>();

    for (HasTextStatistics obj : objs) {
      FloatCounter objFontsizesCounter =
          obj.getTextStatistics().getFontsizesCounter();
      ObjectCounter<PdfFont> objFontsCounter =
          obj.getTextStatistics().getFontsCounter();
      ObjectCounter<PdfColor> objColorsCounter =
          obj.getTextStatistics().getColorsCounter();
      IntCounter objAsciiCounter = obj.getTextStatistics().getAsciiCounter();
      IntCounter objDigitsCounter =
          obj.getTextStatistics().getDigitsCounter();

      fontsizesCounter.add(objFontsizesCounter);
      asciiCounter.add(objAsciiCounter);
      digitsCounter.add(objDigitsCounter);
      fontsCounter.add(objFontsCounter);
      colorsCounter.add(objColorsCounter);
    }

    statistics.setAvgFontsize(fontsizesCounter.getAverageValue());
    statistics.setAsciiRatio(asciiCounter.getAverageValue());
    statistics.setDigitsRatio(digitsCounter.getAverageValue());
    statistics.setMostCommonFontsize(fontsizesCounter.getMostFrequentFloat());
    statistics.setFontsizesCounter(fontsizesCounter);
    statistics.setMostCommonFont(fontsCounter.getMostFrequentObject());
    statistics.setFontsCounter(fontsCounter);
    statistics.setMostCommonColor(colorsCounter.getMostFrequentObject());
    statistics.setColorsCounter(colorsCounter);

    return statistics;
  }

  // ___________________________________________________________________________

  /**
   * Accumulates the average values of the given statistics to single values.
   */
  protected static void accumulateAverageValues(
      List<? extends HasTextStatistics> objs,
      PlainTextStatistics statistics) {

    float numObjects = objs.size();
    float sumFontsizes = 0;
    float sumAscii = 0;
    float sumDigits = 0;

    for (HasTextStatistics obj : objs) {
      sumFontsizes += obj.getTextStatistics().getAverageFontsize();
      sumAscii += obj.getTextStatistics().getAsciiRatio();
      sumDigits += obj.getTextStatistics().getDigitsRatio();
    }

    statistics.setAvgFontsize(sumFontsizes / numObjects);
    statistics.setAsciiRatio(sumAscii / numObjects);
    statistics.setDigitsRatio(sumDigits / numObjects);
  }
}
