package pdfact.pipes.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.model.SemanticRole;
import pdfact.model.TextUnit;

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
  public interface XmlSerializerFactory extends SerializerFactory {
    /**
     * Creates a new PdfXmlSerializer.
     * 
     * @return An instance of PdfXmlSerializer.
     */
    PdfXmlSerializer create();

    /**
     * Creates a new PdfXmlSerializer.
     * 
     * @param textUnit
     *        The text unit.
     * @param roles
     *        The semantic roles filter.
     * 
     * @return An instance of PdfXmlSerializer.
     */
    PdfXmlSerializer create(@Assisted TextUnit textUnit,
        @Assisted Set<SemanticRole> roles);
  }
}