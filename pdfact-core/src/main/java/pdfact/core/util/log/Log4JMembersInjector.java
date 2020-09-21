package pdfact.core.util.log;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.MembersInjector;

/**
 * An injector that sets the log4 logger instance.
 * 
 * @author Claudius Korzen
 * 
 * @param <T>
 *        The type to inject members of.
 */
class Log4JMembersInjector<T> implements MembersInjector<T> {
  /**
   * The field to set.
   */
  protected final Field field;
  /**
   * The logger to create.
   */
  protected final Logger logger;

  /**
   * The default constructor.
   * 
   * @param field
   *        The field to set.
   */
  public Log4JMembersInjector(Field field) {
    this.field = field;
    this.field.setAccessible(true);
    this.logger = LogManager.getLogger(field.getDeclaringClass().getName());
  }

  @Override
  public void injectMembers(T t) {
    try {
      this.field.set(t, this.logger);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // ==========================================================================

  /**
   * Returns the created logger.
   * 
   * @return The created logger.
   */
  public Logger getLogger() {
    return this.logger;
  }
}
