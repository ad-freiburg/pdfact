package pdfact.core.util.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * An annotation to inject a logger.
 * 
 * @author Claudius Korzen
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RUNTIME)
public @interface InjectLogger {
  
}
