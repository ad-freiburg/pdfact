package icecite.models.plain;

import icecite.models.PdfResource;

// TODO: Implement hashCode() and equals().

/**
 * A plain implementation of {@link PdfResource}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfResource implements PdfResource {
  /**
   * The id of this resource.
   */
  protected String id;

  // ==========================================================================
  
  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfResource()";
  }

  // ==========================================================================
  
  @Override
  public boolean equals(Object other) {
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
