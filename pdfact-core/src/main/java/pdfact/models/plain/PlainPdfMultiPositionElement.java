package pdfact.models.plain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.models.PdfMultiPositionElement;
import pdfact.models.PdfPosition;

/**
 * A plain implementation of {@link PdfMultiPositionElement}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfMultiPositionElement extends PlainPdfElement
    implements PdfMultiPositionElement {
  /**
   * The positions of this PDF element.
   */
  protected List<PdfPosition> positions;

  /**
   * The default constructor.
   */
  public PlainPdfMultiPositionElement() {
    this.positions = new ArrayList<>();
  }

  // ==========================================================================

  @Override
  public List<PdfPosition> getPositions() {
    return this.positions;
  }

  @Override
  public void setPositions(List<PdfPosition> positions) {
    this.positions = positions;
  }

  @Override
  public void addPositions(List<PdfPosition> positions) {
    this.positions.addAll(positions);
  }

  @Override
  public void addPosition(PdfPosition position) {
    this.positions.add(position);
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfMultiPositionElement(pos: " + getPositions() + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfMultiPositionElement) {
      PdfMultiPositionElement otherElement = (PdfMultiPositionElement) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPositions(), otherElement.getPositions());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPositions());
    return builder.hashCode();
  }
}
