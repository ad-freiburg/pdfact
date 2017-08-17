package pdfact.log;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;

/**
 * An enumeration of log levels.
 * 
 * @author Claudius Korzen
 */
public enum PdfActLogLevel {
  /**
   * The level where logging is switched off.
   */
  OFF(0, Level.OFF),
  /**
   * The level for debug messages.
   */
  DEBUG(1, Level.DEBUG),
  /**
   * The level for info messages.
   */
  INFO(2, Level.INFO),
  /**
   * The level for warn messages.
   */
  WARN(3, Level.WARN),
  /**
   * The level for error messages.
   */
  ERROR(4, Level.ERROR);

  // ==========================================================================

  /**
   * The int level.
   */
  protected int intLevel;

  /**
   * The log4jEquivalent.
   */
  protected Level log4jEquivalent;

  /**
   * Creates a new log level..
   * 
   * @param intLevel
   *        The int level of the log level.
   * @param log4jEquivalent
   *        The log4j equaivalent.
   */
  PdfActLogLevel(int intLevel, Level log4jEquivalent) {
    this.intLevel = intLevel;
    this.log4jEquivalent = log4jEquivalent;
  }

  /**
   * Returns the int level of this log level.
   * 
   * @return The int level of this log level.
   */
  public int getIntLevel() {
    return this.intLevel;
  }

  /**
   * Returns the log4j equivalent of this log level.
   * 
   * @return The log4j equivalent.
   */
  public Level getLog4jEquivalent() {
    return this.log4jEquivalent;
  }

  /**
   * Checks if this log level implies the given other log level.
   * 
   * @param level
   *        The other log level.
   * @return True, if this log level implies the given log level, False
   *         otherwise.
   */
  public boolean implies(PdfActLogLevel level) {
    if (this.intLevel == 0) {
      return false;
    }

    if (level == null) {
      return false;
    }

    return this.intLevel >= level.getIntLevel();
  }

  // ==========================================================================

  /**
   * The log levels by their int levels.
   */
  protected static final Map<Integer, PdfActLogLevel> LOG_LEVELS;

  static {
    LOG_LEVELS = new HashMap<>();

    // Fill the map of roles per name.
    for (PdfActLogLevel logLevel : values()) {
      LOG_LEVELS.put(logLevel.getIntLevel(), logLevel);
    }
  }

  /**
   * Returns the int levels of all available log levels in a set.
   * 
   * @return The int levels of all available log levels in a set.
   */
  public static Set<Integer> getIntLevels() {
    return LOG_LEVELS.keySet();
  }

  /**
   * Returns all available log levels in a set.
   * 
   * @return All available log levels in a set.
   */
  public static Collection<PdfActLogLevel> getLogLevels() {
    return LOG_LEVELS.values();
  }

  /**
   * Checks if there is a log level for the given int level.
   * 
   * @param level
   *        The level to check.
   *
   * @return True, if there is a log level for the given int level.
   */
  public static boolean isValidLogLevel(int level) {
    return LOG_LEVELS.containsKey(level);
  }

  /**
   * Returns the log levels that are associated with the given int levels.
   * 
   * @param levels
   *        The int levels of the log levels to fetch.
   * @return A set of the fetched levels.
   */
  public static Set<PdfActLogLevel> getLogLevels(int... levels) {
    if (levels == null || levels.length == 0) {
      return null;
    }

    Set<PdfActLogLevel> logLevels = new HashSet<>();
    for (int level : levels) {
      PdfActLogLevel logLevel = getLogLevel(level);
      if (logLevel != null) {
        logLevels.add(logLevel);
      }
    }
    return logLevels;
  }

  /**
   * Returns the log level that is associated with the given int level.
   * 
   * @param level
   *        The int level of the log level to fetch.
   * @return The log level that is associated with the given int level.
   */
  public static PdfActLogLevel getLogLevel(int level) {
    if (!isValidLogLevel(level)) {
      throw new IllegalArgumentException(level + " isn't a valid log level.");
    }
    return LOG_LEVELS.get(level);
  }
}
