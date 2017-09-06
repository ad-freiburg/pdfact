package pdfact.core.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A plain implementation of {@link Resource}.
 * 
 * @author Claudius Korzen
 */
public class PlainResource implements Resource {
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
    return "PlainResource(" + getId() + ")";
  }

  // ==========================================================================

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
