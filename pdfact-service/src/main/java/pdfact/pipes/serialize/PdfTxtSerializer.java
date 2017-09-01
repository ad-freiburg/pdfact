package pdfact.pipes.serialize;

import java.util.Set;

import com.google.inject.assistedinject.Assisted;

import pdfact.model.SemanticRole;
import pdfact.model.TextUnit;

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
  public interface TxtSerializerFactory extends SerializerFactory {
    /**
     * Creates a new PdfTxtSerializer.
     * 
     * @return An instance of PdfTxtSerializer.
     */
    PdfTxtSerializer create();

    /**
     * Creates a new PdfTxtSerializer.
     * 
     * @param textUnit
     *        The text unit.
     * @param roles
     *        The semantic roles filter.
     * 
     * @return An instance of PdfTxtSerializer.
     */
    PdfTxtSerializer create(@Assisted TextUnit textUnit,
        @Assisted Set<SemanticRole> roles);
  }
}
