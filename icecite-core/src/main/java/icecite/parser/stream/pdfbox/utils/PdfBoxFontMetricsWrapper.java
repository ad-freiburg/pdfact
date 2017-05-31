package icecite.parser.stream.pdfbox.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.afm.FontMetrics;

// TODO: Check if this works as suggested.

/**
 * Wrapper for FontMetrics class to get access to the included CharMetrics.
 *
 * @author Claudius Korzen
 */
public class PdfBoxFontMetricsWrapper {
  /** The wrapped FontMetrics object. */
  protected FontMetrics fontMetrics;
  /** The map to map character names to their CharMetrics. */
  protected Map<String, CharMetric> charMetricsMap;

  /**
   * Wraps the given FontMetrics object.
   * 
   * @param fontMetrics
   *        The FontMetrics object to wrap.
   */
  public PdfBoxFontMetricsWrapper(FontMetrics fontMetrics) {
    this.charMetricsMap = new HashMap<String, CharMetric>();
    this.fontMetrics = fontMetrics;
    
    if (fontMetrics != null) {
      for (CharMetric charMetric : fontMetrics.getCharMetrics()) {
        this.charMetricsMap.put(charMetric.getName(), charMetric);
      }
    }
  }

  /**
   * Returns the wrapped FontMetrics object.
   * 
   * @return The wrapped FontMetrics object.
   */
  public FontMetrics getFontMetrics() {
    return this.fontMetrics;
  }

  /**
   * Returns the CharMetrics as map.
   * 
   * @return The CharMetrics as map.
   */
  public Map<String, CharMetric> getCharMetricsMap() {
    return this.charMetricsMap;
  }
}
