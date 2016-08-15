package model;

import java.util.Arrays;
import java.util.List;
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
  
//  /** 
//   * A pattern to find the start of an itemize. For example, this pattern finds 
//   * "I.", "IV.", "1." or "•".
//   */
//  public static final Pattern ITEMIZE_START_PATTERN = 
//      Pattern.compile(
//          "^([i|ii|iii|iv|v|vi|vii|viii|ix|x]\\.|[0-9]{1,2}\\.|•)(\\s|$)", 
//          Pattern.CASE_INSENSITIVE);
  
  /** 
   * A pattern to find the start of an itemize. 
   * This pattern will find "•".
   */
  public static final Pattern ITEMIZE_START_PATTERN_1 =
      Pattern.compile("^(•)(\\s+\\w|$)", Pattern.CASE_INSENSITIVE);

  /** 
   * A pattern to find the start of an itemize. 
   * This pattern will find "I.", "II.", "III.", "IV.", etc.
   */
  public static final Pattern ITEMIZE_START_PATTERN_2 =
      Pattern.compile("^(X{0,1}(IX|IV|V?I{0,3}))\\.(\\s+\\w|$)",
          Pattern.CASE_INSENSITIVE);

  /** 
   * A pattern to find the start of an itemize. 
   * This pattern will find "(I)", "(II)", "(III)", "(IV)", etc.
   */
  public static final Pattern ITEMIZE_START_PATTERN_3 =
      Pattern.compile("^\\((X{0,1}(IX|IV|V?I{0,3}))\\)(\\s+\\w|$)",
          Pattern.CASE_INSENSITIVE);

  /** 
   * A pattern to find the start of an itemize. 
   * This pattern will find "A.", "B.", "C.", "D.", 0., 1., 2., etc.
   */
  public static final Pattern ITEMIZE_START_PATTERN_4 =
      Pattern.compile("^([a-z0-9])\\.(\\s+\\w|$)"); 
      // Don't use Pattern.CASE_INSENSITIVE here to avoid to match author names
      // like "S. Okamato".

  /** 
   * A pattern to find the start of an itemize. For example, this pattern finds 
   * "I.", "IV.", "1." or "•".
   */
  public static final Pattern ITEMIZE_START_PATTERN_5 =
      Pattern.compile("^\\(([A-Z0-9])\\)(\\s+\\w|$)",
          Pattern.CASE_INSENSITIVE);

  /** 
   * A pattern same to #4 but with Uppercase letters. Needed to distinguish 
   * between section headings and author names like "A. Meyer".
   */
  public static final Pattern ITEMIZE_START_PATTERN_6 =
      Pattern.compile("^([A-Z0-9])\\.(\\s+\\w|$)");
  
  public static final List<Pattern> ITEMIZE_START_PATTERNS = Arrays.asList(
      ITEMIZE_START_PATTERN_1,
      ITEMIZE_START_PATTERN_2,
      ITEMIZE_START_PATTERN_3,
      ITEMIZE_START_PATTERN_4,
      ITEMIZE_START_PATTERN_5);
}
