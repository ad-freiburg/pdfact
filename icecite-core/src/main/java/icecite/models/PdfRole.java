package icecite.models;

/**
 * An enumeration of roles for PDF elements.
 * 
 * @author Claudius Korzen
 */
public enum PdfRole {
  /**
   * The title.
   */
  TITLE("title"),
  /**
   * An author.
   */
  AUTHOR("author");

  /**
   * The name of this role.
   */
  protected String name;

  /**
   * Creates a new PDF role.
   * 
   * @param name
   *        The name of the role.
   */
  PdfRole(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this role.
   * 
   * @return The name of this role.
   */
  public String getName() {
    return this.name;
  }
}
