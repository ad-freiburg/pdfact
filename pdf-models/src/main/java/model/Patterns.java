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
      "\\(([A-Z])?\\d{1,3}([a-z]?)(\\.\\d{1,2})?\\)$");
  
  /** 
   * A pattern to find the start of an itemize. For example, this pattern finds 
   * "I.", "IV.", "1." or "•".
   */
  public static final Pattern ITEMIZE_START_PATTERN = 
      Pattern.compile(
          "^([i|ii|iii|iv|v|vi|vii|viii|ix|x]\\.|[0-9]{1,2}\\.|•)(\\s|$)", 
          Pattern.CASE_INSENSITIVE);
}
