package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import model.PdfDocument;
import model.PdfFeature;
import model.PdfRole;

/**
 * The interface for all concrete implementations to output a pdf text document
 * in a specific format.
 *
 * @author Claudius Korzen
 */
public interface PdfSerializer {
  /**
   * Returns the produced format of this serializer.
   */
  public String getOutputFormat();
  
  /**
   * Sets the features to serialize.
   */
  public void setFeatures(List<PdfFeature> features);

  /**
   * Sets the roles to serialize.
   */
  public void setRoles(List<PdfRole> roles);
  
  /**
   * Sets the flag whether to serialize punctuation marks.
   */
  public void setSerializePunctuationMarks(boolean include);
  
  /**
   * Sets the flag whether to serialize subscripts.
   */
  public void setSerializeSubscripts(boolean include);
  
  /**
   * Sets the flag whether to serialize superscripts.
   */
  public void setSerializeSuperscripts(boolean include);
  
  /**
   * Serializes the given document to the given stream in a specific format.
   */
  public void serialize(PdfDocument doc, OutputStream os) throws IOException;
}
