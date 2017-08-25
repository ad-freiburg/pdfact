package pdfact.pipes.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.model.ElementType;
import pdfact.model.SemanticRole;

/**
 * A serializer to serialize a PDF document in XML format.
 *
 * @author Claudius Korzen
 */
public interface PdfXmlSerializer extends PdfSerializer {
  /**
   * The factory to create instances of PdfXmlSerializer.
   * 
   * @author Claudius Korzen
   */
  public interface PdfXmlSerializerFactory extends PdfSerializerFactory {
    /**
     * Creates a new PdfXmlSerializer.
     * 
     * @return An instance of PdfXmlSerializer.
     */
    PdfXmlSerializer create();

    /**
     * Creates a new PdfXmlSerializer.
     * 
     * @param types
     *        The element types filter.
     * @param roles
     *        The semantic roles filter.
     * 
     * @return An instance of PdfXmlSerializer.
     */
    PdfXmlSerializer create(@Assisted Set<ElementType> types,
        @Assisted Set<SemanticRole> roles);
  }
}