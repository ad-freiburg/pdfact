package pdfact.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.models.PdfRole;
import pdfact.models.PdfElementType;

/**
 * A serializer to serialize a PDF document in TXT format.
 *
 * @author Claudius Korzen
 */
public interface PdfTxtSerializer extends PdfSerializer {
  /**
   * The factory to create instances of PdfTxtSerializer.
   * 
   * @author Claudius Korzen
   */
  public interface PdfTxtSerializerFactory extends PdfSerializerFactory {
    /**
     * Creates a new PdfTxtSerializer.
     * 
     * @return An instance of PdfTxtSerializer.
     */
    PdfTxtSerializer create();

    /**
     * Creates a new PdfTxtSerializer.
     * 
     * @param units
     *        The text units to extract.
     * @param roles
     *        The semantic roles of text units to extract.
     * 
     * @return An instance of PdfTxtSerializer.
     */
    PdfTxtSerializer create(@Assisted Set<PdfElementType> units,
        @Assisted Set<PdfRole> roles);
  }
}
