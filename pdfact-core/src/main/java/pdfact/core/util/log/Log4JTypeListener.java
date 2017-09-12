package pdfact.core.util.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import pdfact.core.PdfActCoreSettings;
import pdfact.core.model.LogLevel;

/**
 * A listener that scans through a type's fields, looking for Log4J loggers. For
 * each logger field that's encountered, Log4JMembersInjector is registered.
 * 
 * @author Claudius Korzen
 */
public class Log4JTypeListener implements TypeListener {
  /**
   * The current log level.
   */
  protected static LogLevel logLevel = PdfActCoreSettings.DEFAULT_LOG_LEVEL;

  /**
   * The registered loggers.
   */
  protected static Set<Logger> loggers = new HashSet<>();

  // ==========================================================================

  @Override
  public <T> void hear(TypeLiteral<T> literal, TypeEncounter<T> encounter) {
    Class<?> clazz = literal.getRawType();
    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.getType() == Logger.class
            && field.isAnnotationPresent(InjectLogger.class)) {
          if (Modifier.isStatic(field.getModifiers())) {
            field.setAccessible(true);
            Logger logger = Logger.getLogger(field.getDeclaringClass());
            try {
              field.set(null, logger);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
            logger.setLevel(logLevel.getLog4jEquivalent());
            loggers.add(logger);
          } else {
            Log4JMembersInjector<T> inj = new Log4JMembersInjector<T>(field);
            encounter.register(inj);
            Logger logger = inj.getLogger();
            logger.setLevel(logLevel.getLog4jEquivalent());
            loggers.add(logger);
          }
        }
      }
      clazz = clazz.getSuperclass();
    }
  }

  // ==========================================================================

  /**
   * Sets the log level.
   * 
   * @param level
   *        The log level.
   */
  public static void setLogLevel(LogLevel level) {
    if (level != null) {
      logLevel = level;
      // Set the log level for all loggers already registered.
      for (Logger logger : loggers) {
        logger.setLevel(level.getLog4jEquivalent());
      }
    }
  }

  /**
   * Returns the log level.
   * 
   * @return The log level.
   */
  public static LogLevel getLogLevel() {
    return logLevel;
  }

  /**
   * Returns true if the given log level is implied by the current log level.
   * 
   * @param level
   *        The level to check.
   * 
   * @return True if the given log level is implied by the current log level;
   *         False otherwise.
   */
  public static boolean hasLogLevel(LogLevel level) {
    return level != null && logLevel.implies(level);
  }
}
