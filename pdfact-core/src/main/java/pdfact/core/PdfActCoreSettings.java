package pdfact.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Some global settings to control the behavior of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCoreSettings {
  // ==============================================================================================
  // General settings.

  /**
   * The default character encoding.
   */
  public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

  // ==============================================================================================
  // Debug settings.

  /**
   * The default boolean flag indicating whether or not to print debug info about the parsed PDF
   * operators.
   */
  public static final boolean DEFAULT_IS_DEBUG_PDF_OPERATORS = false;

  /**
   * The default boolean flag indicating whether or not to print debug info about the characters
   * extraction.
   */
  public static final boolean DEFAULT_IS_DEBUG_CHARS_EXTRACTION = false;

  /**
   * The default boolean flag indicating whether or not to print debug info about the text line
   * detection.
   */
  public static final boolean DEFAULT_IS_DEBUG_LINE_DETECTION = false;

  /**
   * The default boolean flag indicating whether or not to print debug info about the word
   * detection.
   */
  public static final boolean DEFAULT_IS_DEBUG_WORD_DETECTION = false;

  /**
   * The default boolean flag indicating whether or not to print debug info about the text block
   * detection.
   */
  public static final boolean DEFAULT_IS_DEBUG_BLOCK_DETECTION = false;

  // ==============================================================================================

  /**
   * The number of decimal places for floating numbers.
   */
  public static final int FLOATING_NUMBER_PRECISION = 1;

  // ==============================================================================================

  /**
   * The path to the AFM file.
   */
  public static final String AFM_FILE_PATH = "afm.map";

  /**
   * The field delimiter in the AFM file.
   */
  public static final String AFM_FILE_FIELD_DELIMITER = "\t";
}
