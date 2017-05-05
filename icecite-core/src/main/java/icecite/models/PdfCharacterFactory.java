package icecite.models;

/**
 * The factory to create instances of {@link PdfCharacter}.
 * 
 * @author Claudius Korzen
 */
public interface PdfCharacterFactory {
  /**
   * Creates a PdfCharacter.
   * 
   * @return An instance of {@link PdfCharacter}.
   */
  PdfCharacter create();
}
