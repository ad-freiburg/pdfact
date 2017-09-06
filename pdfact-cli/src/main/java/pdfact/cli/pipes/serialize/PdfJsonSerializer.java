package pdfact.cli.pipes.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.cli.model.TextUnit;
import pdfact.core.model.SemanticRole;

/**
 * A serializer to serialize a PDF document in JSON format.
 *
 * @author Claudius Korzen
 */
public interface PdfJsonSerializer extends PdfSerializer {
  /**
   * The factory to create instances of PdfJsonSerializer.
   * 
   * @author Claudius Korzen
   */
  public interface JsonSerializerFactory extends SerializerFactory {
    /**
     * Creates a new PdfJsonSerializer.
     * 
     * @return An instance of PdfJsonSerializer.
     */
    PdfJsonSerializer create();

    /**
     * Creates a new PdfJsonSerializer.
     * 
     * @param textUnit
     *        The text unit.
     * @param roles
     *        The semantic roles filter.
     * 
     * @return An instance of PdfJsonSerializer.
     */
    PdfJsonSerializer create(@Assisted TextUnit textUnit,
        @Assisted Set<SemanticRole> roles);
  }
}
