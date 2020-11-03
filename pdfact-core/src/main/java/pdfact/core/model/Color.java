package pdfact.core.model;

// TODO: Implement the support of other color schemes (CMYK, etc.)
// TODO: Clarify, if the rgb values are within the interval [0, 1] or [0, 255].

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A color in a document.
 * 
 * @author Claudius Korzen
 */
public class Color extends Resource {
  /**
   * The name of this color.
   */
  protected String name;

  /**
   * The RGB value of this color.
   */
  protected float[] rgb;

  // ==============================================================================================

  /**
   * Creates a new color.
   */
  public Color() {
    this.rgb = new float[3];
  }

  /**
   * Creates a new color.
   * 
   * @param rgb The RGB value given as an array of three float values in [0,1].
   */
  public Color(float[] rgb) {
    this.rgb = rgb;
  }

  /**
   * Creates a new color.
   * 
   * @param r The R value.
   * @param g The G value.
   * @param b The B value.
   */
  public Color(float r, float g, float b) {
    this();
    this.rgb[0] = r;
    this.rgb[1] = g;
    this.rgb[2] = b;
  }

  // ==============================================================================================

  /**
   * Returns the name of this color.
   * 
   * @return The name of this color.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of this color.
   * 
   * @param name The name of this color.
   */
  public void setName(String name) {
    this.name = name;
  }

  // ==============================================================================================

  /**
   * Returns the RGB value of this color.
   * 
   * @return The RGB value given as an array of three float values in [0,1].
   */
  public float[] getRGB() {
    return this.rgb;
  }

  /**
   * Sets the RGB value of this color.
   * 
   * @param rgb The RGB value given as an array of three float values in [0,1].
   */
  public void setRGB(final float[] rgb) {
    this.rgb = rgb;
  }

  /**
   * Sets the RGB value of this color.
   * 
   * @param r The R value.
   * @param g The G value.
   * @param b The B value.
   */
  public void setRGB(float r, float g, float b) {
    this.rgb[0] = r;
    this.rgb[1] = g;
    this.rgb[2] = b;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Color(" + Arrays.toString(this.rgb) + ")";
  }

  // ==============================================================================================

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
