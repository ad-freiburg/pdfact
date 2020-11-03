package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Any resource in a document (font, color, etc.).
 * 
 * @author Claudius Korzen
 */
public class Resource {
  /**
   * The id of this resource.
   */
  protected String id;

  // ==============================================================================================

  /**
   * Returns the id of this resource (used for referencing on serialization).
   * 
   * @return The id.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Sets the id of this resource (used for referencing on serialization).
   * 
   * @param id The id.
   */
  public void setId(String id) {
    this.id = id;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Resource(" + getId() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Resource) {
      Resource otherResource = (Resource) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getId(), otherResource.getId());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getId());
    return builder.hashCode();
  }
}
