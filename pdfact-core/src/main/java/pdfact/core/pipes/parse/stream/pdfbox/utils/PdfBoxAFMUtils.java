package pdfact.core.pipes.parse.stream.pdfbox.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.fontbox.afm.AFMParser;
import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.afm.FontMetrics;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import pdfact.core.util.PdfActUtils;
import pdfact.core.util.log.InjectLogger;

// TODO: This doesn't work, because the afm files are missing.

/**
 * A collection of utility methods to manage and process AFM files.
 * 
 * @author Claudius Korzen
 */
public class PdfBoxAFMUtils {
  /**
   * The logger.
   */
  @InjectLogger
  protected static Logger log;

  /**
   * The additional glyphs.
   */
  protected static final Map<String, PdfBoxFontMetricsWrapper> ADDITIONAL_AFM =
      new HashMap<>();

  // TODO
  // static {
  // ADDITIONAL_AFM = readAdditionalAFMFiles("afm/");
  // }

  /**
   * Returns the CharMetric for the glyph given by glyphName.
   * 
   * @param glyphName
   *        The name of glyph.
   * @param type1Font
   *        The font.
   * @return The CharMetric.
   */
  public static CharMetric getCharMetric(String glyphName,
      PDType1Font type1Font) {
    if (type1Font != null) {
      String basename = FontUtils.computeBasename(type1Font);

      PdfBoxFontMetricsWrapper fontMetrics = ADDITIONAL_AFM.get(basename);
      if (fontMetrics != null) {
        return fontMetrics.getCharMetricsMap().get(glyphName);
      }
    }
    return null;
  }

  /**
   * Reads additional AFM files in addition to the 14 standard fonts.
   * 
   * @param path
   *        The path to the directory of AFM files.
   * 
   * @return The map containing the read font metrics.
   */
  protected static Map<String, PdfBoxFontMetricsWrapper> readAdditionalAFMFiles(
      String path) {
    log.debug("Reading additional AFM files from path '" + path + "'.");

    Map<String, PdfBoxFontMetricsWrapper> result = new HashMap<>();
    try {
      Map<String, InputStream> files = PdfActUtils.readDirectory(path);

      for (Entry<String, InputStream> file : files.entrySet()) {
        String name = file.getKey();
        try (InputStream stream = file.getValue()) {
          String basename = PdfActUtils.getBasename(name);

          try {
            AFMParser parser = new AFMParser(stream);
            FontMetrics fontMetrics = parser.parse();

            // Put the result into map.
            result.put(basename, new PdfBoxFontMetricsWrapper(fontMetrics));
          } catch (IOException e) {
            continue;
          }
        }
      }
    } catch (IOException e) {
      log.warn("An error occurred while reading additional AFM files.", e);
    }

    log.debug("Reading additional AFM files from path done.");
    log.debug("# read additional AFM files: " + result.size());

    return result;
  }

}
