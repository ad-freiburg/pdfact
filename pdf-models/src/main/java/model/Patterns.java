package model;

import java.util.regex.Pattern;

/**
 * Collection of patterns.
 * 
 * @author Claudius Korzen
 *
 */
public class Patterns {
  /**
   * Pattern to find figure captions.
   */
  public static final Pattern FIGURE_CAPTION_PATTERN = Pattern.compile(
      "^(fig(\\.?|ure)|abbildung)\\s*\\d+", Pattern.CASE_INSENSITIVE);

  /**
   * Pattern to find table captions.
   */
  public static final Pattern TABLE_CAPTION_PATTERN = Pattern.compile(
      "^(table|tabelle)\\s*\\d+(\\.|:)", Pattern.CASE_INSENSITIVE);
  
  /**
   * Pattern to find lables of formulas.
   */
  public static final Pattern FORMULA_LABEL_PATTERN = Pattern.compile(
      "\\([0-9]{1,3}\\)$", Pattern.CASE_INSENSITIVE);
  
  /** 
   * A pattern to find roman numerals, like "I." or IV." or "1.".
   */
  public static final Pattern SECTION_HEADING_START_PATTERN = 
      Pattern.compile("^([i|ii|iii|iv|v|vi|vii|viii|ix|x]|[0-9]{1,2})\\.\\s", 
          Pattern.CASE_INSENSITIVE);
}
