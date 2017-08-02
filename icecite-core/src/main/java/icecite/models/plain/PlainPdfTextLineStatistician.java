package icecite.models.plain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import icecite.models.PdfCharacterStatistics;
import icecite.models.PdfFontFace;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineStatistician;
import icecite.models.PdfTextLineStatistics;
import icecite.models.PdfTextLineStatistics.PdfTextLineStatisticsFactory;
import icecite.models.PdfWord;
import icecite.models.PdfWordList;
import icecite.utils.counter.FloatCounter;
import icecite.utils.textlines.PdfTextLineUtils;

/**
 * A plain implementation of {@link PdfTextLineStatistician}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextLineStatistician implements PdfTextLineStatistician {
  /**
   * The factory to create instances of {@link PdfTextLineStatistics}.
   */
  protected PdfTextLineStatisticsFactory statisticsFactory;

  /**
   * The default constructor.
   * 
   * @param statisticsFactory
   *        The factory to create instances of {@link PdfTextLineStatistics}.
   */
  @Inject
  public PlainPdfTextLineStatistician(
      PdfTextLineStatisticsFactory statisticsFactory) {
    this.statisticsFactory = statisticsFactory;
  }

  @Override
  public PdfTextLineStatistics compute(PdfTextLineList textLines) {
    PdfTextLineStatistics statistics = this.statisticsFactory.create();

    Map<PdfFontFace, FloatCounter> linePitches = new HashMap<>();
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
      PdfCharacterStatistics charStatistics = line.getCharacterStatistics();
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
        float whitespaceWidth = wordMinX - prevWordMaxX;
        whitespaceWidthFrequencies.add(whitespaceWidth);
      }
    }

    statistics.setLinePitchFrequencies(linePitches);
    statistics.setWhitespaceWidthFrequencies(whitespaceWidthFrequencies);

    return statistics;
  }

  @Override
  public PdfTextLineStatistics aggregate(PdfTextLineStatistics... stats) {
    return aggregate(Arrays.asList(stats));
  }

  @Override
  public PdfTextLineStatistics aggregate(List<PdfTextLineStatistics> stats) {
    PdfTextLineStatistics statistics = this.statisticsFactory.create();

    Map<PdfFontFace, FloatCounter> linePitches = new HashMap<>();
    FloatCounter whitespaceWidthFrequencies = new FloatCounter();

    for (PdfTextLineStatistics stat : stats) {
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

    statistics.setLinePitchFrequencies(linePitches);
    statistics.setWhitespaceWidthFrequencies(whitespaceWidthFrequencies);

    return statistics;
  }

}
