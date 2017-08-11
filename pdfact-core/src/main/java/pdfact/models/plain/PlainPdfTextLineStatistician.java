package pdfact.models.plain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import pdfact.models.HasTextLineStatistic;
import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextLineList;
import pdfact.models.PdfTextLineStatistic;
import pdfact.models.PdfTextLineStatistician;
import pdfact.models.PdfWord;
import pdfact.models.PdfWordList;
import pdfact.models.PdfTextLineStatistic.PdfTextLineStatisticFactory;
import pdfact.utils.counter.FloatCounter;
import pdfact.utils.textlines.PdfTextLineUtils;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfTextLineStatistician}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineStatistician implements PdfTextLineStatistician {
  /**
   * The factory to create instances of {@link PdfTextLineStatistic}.
   */
  protected PdfTextLineStatisticFactory statisticFactory;

  /**
   * The default constructor.
   * 
   * @param factory
   *        The factory to create instances of {@link PdfTextLineStatistic}.
   */
  @Inject
  public PlainPdfTextLineStatistician(PdfTextLineStatisticFactory factory) {
    this.statisticFactory = factory;
  }

  @Override
  public PdfTextLineStatistic compute(PdfTextLineList textLines) {
    // Create new statistic object.
    PdfTextLineStatistic statistic = this.statisticFactory.create();

    // Count the line pitches.
    Map<PdfFontFace, FloatCounter> linePitches = new HashMap<>();
    // Count the whitespace widths.
    FloatCounter whitespaceWidthFrequencies = new FloatCounter();

    for (int i = 1; i < textLines.size(); i++) {
      PdfTextLine prevLine = textLines.get(i - 1);
      PdfTextLine line = textLines.get(i);

      if (prevLine == null || line == null) {
        continue;
      }

      // Compute the line pitch between the current line and the previous line.
      float linePitch = PdfTextLineUtils.computeLinePitch(prevLine, line);
      if (Float.isNaN(linePitch)) {
        continue;
      }

      // Compute the font face of the current line.
      PdfCharacterStatistic charStatistics = line.getCharacterStatistic();
      PdfFontFace fontFace = charStatistics.getMostCommonFontFace();
      // Add a new float counter if there is none for the given font face.
      linePitches.putIfAbsent(fontFace, new FloatCounter());
      linePitches.get(fontFace).add(linePitch);

      // Compute the average whitespace width.
      PdfWordList words = line.getWords();
      if (words == null) {
        continue;
      }
      for (int j = 1; j < words.size(); j++) {
        PdfWord prevWord = words.get(j - 1);
        PdfWord word = words.get(j);
        if (prevWord == null || word == null) {
          continue;
        }
        float wordMinX = word.getRectangle().getMinX();
        float prevWordMaxX = prevWord.getRectangle().getMaxX();
        whitespaceWidthFrequencies.add(wordMinX - prevWordMaxX);
      }
    }

    statistic.setLinePitchFrequencies(linePitches);
    statistic.setWhitespaceWidthFrequencies(whitespaceWidthFrequencies);

    return statistic;
  }

  @Override
  public PdfTextLineStatistic aggregate(
      List<? extends HasTextLineStatistic> hasStats) {
    // Create new statistic object.
    PdfTextLineStatistic statistic = this.statisticFactory.create();

    // Count the line pitches.
    Map<PdfFontFace, FloatCounter> linePitches = new HashMap<>();
    // Count the whitespace widths.
    FloatCounter whitespaceWidthFrequencies = new FloatCounter();

    // Combine the statistics.
    for (HasTextLineStatistic hasStat : hasStats) {
      PdfTextLineStatistic stat = hasStat.getTextLineStatistic();
      for (PdfFontFace fontFace : stat.getLinePitchFrequencies().keySet()) {
        FloatCounter linePitchFreqs;
        if (linePitches.containsKey(fontFace)) {
          linePitchFreqs = linePitches.get(fontFace);
        } else {
          linePitchFreqs = new FloatCounter();
          linePitches.put(fontFace, linePitchFreqs);
        }
        linePitchFreqs.add(stat.getLinePitchFrequencies().get(fontFace));
      }
      whitespaceWidthFrequencies.add(stat.getWhitespaceWidthFrequencies());
    }

    statistic.setLinePitchFrequencies(linePitches);
    statistic.setWhitespaceWidthFrequencies(whitespaceWidthFrequencies);

    return statistic;
  }
}
