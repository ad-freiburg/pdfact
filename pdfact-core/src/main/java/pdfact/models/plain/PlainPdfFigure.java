package pdfact.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import pdfact.models.PdfTextUnit;
import pdfact.models.PdfFigure;

/**
 * A plain implementation of {@link PdfFigure}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfFigure extends PlainPdfElement implements PdfFigure {
  
  // ==========================================================================

  @Override
  public PdfTextUnit getTextUnit() {
    return PdfTextUnit.FIGURE;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfFigure(pos: " + getPosition() + ")";
  }

  // ==========================================================================
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfFigure) {
      PdfFigure otherFigure = (PdfFigure) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherFigure.getPosition());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    return builder.hashCode();
  }
}
