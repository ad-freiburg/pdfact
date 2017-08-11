package pdfact.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

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
    PdfXmlSerializer create(@Assisted Set<PdfElementType> types,
        @Assisted Set<PdfRole> roles);
  }
}