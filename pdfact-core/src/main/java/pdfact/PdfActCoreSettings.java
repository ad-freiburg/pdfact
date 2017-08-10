package pdfact;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import pdfact.log.PdfActLogLevel;

/**
 * Some global settings to control the behavior of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCoreSettings {
  // ==========================================================================
  // General settings.
  
  /**
   * The default character encoding.
   */
  public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
  
  
  // ==========================================================================
  // Log settings.
  
  /**
   * The default log level.
   */
  public static final PdfActLogLevel DEFAULT_LOG_LEVEL = PdfActLogLevel.OFF;

}
