package pdfact.core.util.statistician;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.FontFace;
import pdfact.core.model.HasTextLineStatistic;
import pdfact.core.model.HasTextLines;
import pdfact.core.model.Line;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextLine;
import pdfact.core.model.TextLineStatistic;
import pdfact.core.model.Word;
import pdfact.core.util.counter.FloatCounter;
import pdfact.core.util.list.ElementList;

/**
 * A class that computes statistics about text line.
 * 
 * @author Claudius Korzen
 */
public class TextLineStatistician {
  /**
   * Computes the text line statistic for the given text lines.
   * 
   * @param hasTextLines An element that has text lines.
   * 
   * @return The computed text line statistics.
   */
  public TextLineStatistic compute(HasTextLines hasTextLines) {
    return compute(hasTextLines.getTextLines());
  }

  /**
   * Computes the text line statistic for the given text lines.
   * 
   * @param textLines The text lines to process.
   * 
   * @return The computed text line statistics.
   */
  public TextLineStatistic compute(ElementList<TextLine> textLines) {
    // Create new statistic object.
    TextLineStatistic statistic = new TextLineStatistic();

    // Initialize counters for the line pitches and whitespace widths.
    Map<FontFace, FloatCounter> linePitches = new HashMap<>();
    FloatCounter whitespaceWidthFreqs = new FloatCounter();

    for (int i = 1; i < textLines.size(); i++) {
      TextLine prevLine = textLines.get(i - 1);
      TextLine line = textLines.get(i);

      if (prevLine == null || line == null) {
        continue;
      }

      // Compute the line pitch between the current line and the previous line.
      float linePitch = computeLinePitch(prevLine, line);
      if (!Float.isNaN(linePitch)) {
        // Compute the font face of the current line.
        CharacterStatistic charStatistics = line.getCharacterStatistic();
        FontFace fontFace = charStatistics.getMostCommonFontFace();
        // Add a new float counter if there is none for the given font face.
        linePitches.putIfAbsent(fontFace, new FloatCounter());
        linePitches.get(fontFace).add(linePitch);
      }

      // Compute the whitespace widths in the line.
      ElementList<Word> words = line.getWords();
      if (words != null) {
        for (int j = 1; j < words.size(); j++) {
          Word prevWord = words.get(j - 1);
          Word word = words.get(j);
          if (prevWord != null && word != null) {
            Rectangle prevRectangle = prevWord.getLastPosition().getRectangle();
            Rectangle rectangle = word.getFirstPosition().getRectangle();
            if (prevRectangle != null && rectangle != null) {
              float prevMaxX = prevRectangle.getMaxX();
              float minX = rectangle.getMinX();
              whitespaceWidthFreqs.add(minX - prevMaxX);
            }
          }
        }
      }
    }

    statistic.setLinePitchFrequencies(linePitches);
    statistic.setWhitespaceWidthFrequencies(whitespaceWidthFreqs);

    return statistic;
  }

  /**
   * Combines the given list of text line statistics to a single statistic.
   * 
   * @param stats The statistics to combine.
   * 
   * @return The combined statistics.
   */
  public TextLineStatistic aggregate(List<? extends HasTextLineStatistic> hasStats) {
    // Create new statistic object.
    TextLineStatistic statistic = new TextLineStatistic();

    // Initialize counters for the line pitches and whitespace widths.
    Map<FontFace, FloatCounter> linePitches = new HashMap<>();
    FloatCounter whitespaceWidthFreqs = new FloatCounter();

    // Aggregate the given statistics.
    for (HasTextLineStatistic hasStat : hasStats) {
      TextLineStatistic stat = hasStat.getTextLineStatistic();
      for (FontFace fontFace : stat.getLinePitchFrequencies().keySet()) {
        FloatCounter linePitchFreqs;
        if (linePitches.containsKey(fontFace)) {
          linePitchFreqs = linePitches.get(fontFace);
        } else {
          linePitchFreqs = new FloatCounter();
          linePitches.put(fontFace, linePitchFreqs);
        }
        linePitchFreqs.add(stat.getLinePitchFrequencies().get(fontFace));
      }
      whitespaceWidthFreqs.add(stat.getWhitespaceWidthFrequencies());
    }

    // Fill the statistic object.
    statistic.setLinePitchFrequencies(linePitches);
    statistic.setWhitespaceWidthFrequencies(whitespaceWidthFreqs);

    return statistic;
  }

  // ==============================================================================================

  /**
   * Computes the line pitch between the given lines. Both lines must share the
   * same page.
   * 
   * @param firstLine  The first text line.
   * @param secondLine The second text line.
   * @return The line pitch between the given lines or Float.NaN if the lines do
   *         not share the same page.
   */
  public static float computeLinePitch(TextLine firstLine, TextLine secondLine) {
    if (firstLine == null || secondLine == null) {
      return Float.NaN;
    }

    if (firstLine.getPosition().getPage() != secondLine.getPosition().getPage()) {
      return Float.NaN;
    }

    Line firstBaseLine = firstLine.getBaseline();
    Line secondBaseLine = secondLine.getBaseline();
    if (firstBaseLine == null || secondBaseLine == null) {
      return Float.NaN;
    }

    return Math.abs(firstBaseLine.getStartY() - secondBaseLine.getStartY());
  }
}