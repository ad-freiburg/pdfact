package pdfact.core.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;

/**
 * An enumeration of all available log levels.
 * 
 * @author Claudius Korzen
 */
public enum LogLevel {
  /**
   * The "off" level.
   */
  OFF(0, Level.OFF),
  /**
   * The "debug" level.
   */
  DEBUG(1, Level.DEBUG),
  /**
   * The "info" level.
   */
  INFO(2, Level.INFO),
  /**
   * The "warn" level.
   */
  WARN(3, Level.WARN),
  /**
   * The "error" level.
   */
  ERROR(4, Level.ERROR);

  // ==========================================================================

  /**
   * The level given as an integer value.
   */
  protected int intLevel;

  /**
   * The equivalent log4j level.
   */
  protected Level log4jEquivalent;

  /**
   * Creates a new log level.
   * 
   * @param intLevel
   *        The level given as an integer value.
   * @param log4jEquivalent
   *        The log4j level that is equivalent to this level.
   */
  LogLevel(int intLevel, Level log4jEquivalent) {
    this.intLevel = intLevel;
    this.log4jEquivalent = log4jEquivalent;
  }

  /**
   * Returns the level as an integer value.
   * 
   * @return The level as an integer value.
   */
  public int getIntLevel() {
    return this.intLevel;
  }

  /**
   * Returns the log4j level that is equivalent to this level.
   * 
   * @return The log4j level that is equivalent to this level.
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
  public boolean implies(LogLevel level) {
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
   * The log levels by their integer values.
   */
  protected static final TIntObjectMap<LogLevel> LOG_LEVELS;

  static {
    LOG_LEVELS = new TIntObjectHashMap<>();

    // Fill the map of levels by their integer values.
    for (LogLevel logLevel : values()) {
      LOG_LEVELS.put(logLevel.getIntLevel(), logLevel);
    }
  }

  /**
   * Returns a set of the integer values of the available log levels.
   * 
   * @return A set of the integer values of the available log levels.
   */
  public static TIntSet getIntValues() {
    return LOG_LEVELS.keySet();
  }

  /**
   * Returns a collection of all available log levels.
   * 
   * @return A collection of all available log levels.
   */
  public static Collection<LogLevel> getLogLevels() {
    return LOG_LEVELS.valueCollection();
  }

  /**
   * Checks if there exists a log level with the given integer value.
   * 
   * @param value
   *        The integer value to check.
   *
   * @return True, if there exists a log level with the given integer value;
   *         false otherwise.
   */
  public static boolean isValidLogLevel(int value) {
    return LOG_LEVELS.containsKey(value);
  }

  /**
   * Returns the log levels that relate to the given integer values.
   * 
   * @param values
   *        The integer values to process.
   *
   * @return A set of the log levels that relate to the given integer values.
   */
  public static Set<LogLevel> getLogLevels(int... values) {
    Set<LogLevel> logLevels = new HashSet<>();
    if (values != null) {
      for (int value : values) {
        LogLevel logLevel = getLogLevel(value);
        if (logLevel != null) {
          logLevels.add(logLevel);
        }
      }
    }
    return logLevels;
  }

  /**
   * Returns the log level that relates to the given integer value.
   * 
   * @param value
   *        The integer value to process.
   * 
   * @return The log level that relates to the given integer value.
   */
  public static LogLevel getLogLevel(int value) {
    // TODO: Throw an exception or not? If yes, throw a PdfActException.
    if (!isValidLogLevel(value)) {
      throw new IllegalArgumentException(value + " isn't a valid log level.");
    }
    return LOG_LEVELS.get(value);
  }
}