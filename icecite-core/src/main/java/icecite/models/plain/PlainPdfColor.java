package icecite.models.plain;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfColor;

// TODO: Allow various color systems, not only RGB.

/**
 * A plain implementation of {@link PdfColor}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfColor extends PlainPdfResource implements PdfColor {
  /**
   * The name of this color.
   */
  protected String name;

  /**
   * The RGB value of this color.
   */
  protected float[] rgb;

  // ==========================================================================
  // Constructors

  /**
   * Creates a new color.
   */
  @AssistedInject
  public PlainPdfColor() {
    this.rgb = new float[3];
  }

  /**
   * Creates a new color.
   * 
   * @param rgb
   *        The RGB value, given by three floats in range [0,255].
   */
  @AssistedInject
  public PlainPdfColor(@Assisted float[] rgb) {
    this.rgb = rgb;
  }

  // ==========================================================================

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  // ==========================================================================

  @Override
  public void setRGB(final float[] rgb) {
    this.rgb = rgb;
  }

  @Override
  public void setRGB(float r, float g, float b) {
    this.rgb[0] = r;
    this.rgb[1] = g;
    this.rgb[2] = b;
  }

  @Override
  public float[] getRGB() {
    return this.rgb;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfColor(" + Arrays.toString(this.rgb) + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfColor) {
      PdfColor otherColor = (PdfColor) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getRGB(), otherColor.getRGB());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getRGB());
    return builder.hashCode();
  }
}
