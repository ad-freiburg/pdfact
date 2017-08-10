package pdfact.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

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
  public interface PdfJsonSerializerFactory extends PdfSerializerFactory {
    /**
     * Creates a new PdfJsonSerializer.
     * 
     * @return An instance of PdfJsonSerializer.
     */
    PdfJsonSerializer create();

    /**
     * Creates a new PdfJsonSerializer.
     * 
     * @param units
     *        The text units to extract.
     * @param roles
     *        The semantic roles of text units to extract.
     * 
     * @return An instance of PdfJsonSerializer.
     */
    PdfJsonSerializer create(@Assisted Set<PdfElementType> units,
        @Assisted Set<PdfRole> roles);
  }
}
