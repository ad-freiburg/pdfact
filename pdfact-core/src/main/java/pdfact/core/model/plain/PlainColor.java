package pdfact.core.model.plain;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.model.Color;

/**
 * A plain implementation of {@link Color}.
 * 
 * @author Claudius Korzen
 */
public class PlainColor extends PlainResource implements Color {
  /**
   * The name of this color.
   */
  protected String name;

  /**
   * The RGB value of this color.
   */
  protected float[] rgb;

  // ==========================================================================

  /**
   * Creates a new color.
   */
  @AssistedInject
  public PlainColor() {
    this.rgb = new float[3];
  }

  /**
   * Creates a new color based on the given RGB value.
   * 
   * @param rgb
   *        The RGB value given as an array of three float values in [0,1].
   */
  @AssistedInject
  public PlainColor(@Assisted float[] rgb) {
    this();
    this.rgb = rgb;
  }

  /**
   * Creates a new color based on the given RGB value.
   * 
   * @param r
   *        The R value.
   * @param g
   *        The G value.
   * @param b
   *        The B value.
   */
  @AssistedInject
  public PlainColor(
      @Assisted("r") float r,
      @Assisted("g") float g,
      @Assisted("b") float b) {
    this();
    this.rgb[0] = r;
    this.rgb[1] = g;
    this.rgb[2] = b;
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
  public float[] getRGB() {
    return this.rgb;
  }

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

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainColor(" + Arrays.toString(this.rgb) + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Color) {
      Color otherColor = (Color) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getName(), otherColor.getName());
      builder.append(getRGB(), otherColor.getRGB());
      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getName());
    builder.append(getRGB());
    return builder.hashCode();
  }
}
