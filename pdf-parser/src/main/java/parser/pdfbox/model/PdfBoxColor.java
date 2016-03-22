package parser.pdfbox.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;

import model.PdfColor;
import model.PdfDocument;

/**
 * An implementation of PdfColor using PdfBox.
 *
 * @author Claudius Korzen
 *
 */
public class PdfBoxColor implements PdfColor {
  /**
   * Maps a rgb array to its equivalent PdfColor object.
   */
  protected static Map<String, PdfBoxColor> colorMap = new HashMap<>();
  
  /**
   * The unique id of this color. Needed for serialization.
   */
  protected String id;
    
  /**
   * The rgb array.
   */
  protected float[] rgb = { 0.0f, 0.0f, 0.0f };
      
  /**
   * The default constructor.
   */
  protected PdfBoxColor(float[] rgb) {
    this.id = "color-" + colorMap.size();
    this.rgb = rgb;
  }
  
  /**
   * Creates a PdfBoxColor from given PDColor. For two equal PDColors, also the
   * equivalent PdfBoxColors will be equal.
   */
  public static PdfBoxColor create(PDColor color) throws IOException {
    return create(toRGB(color));
  }
  
  /**
   * Creates a PdfBoxColor from given PDColor. For two equal PDColors, also the
   * equivalent PdfBoxColors will be equal.
   */
  public static PdfBoxColor create(float[] rgb) throws IOException {
    String rgbStr = rgb != null ? Arrays.toString(rgb) : null;
    
    if (!colorMap.containsKey(rgbStr)) {
      colorMap.put(rgbStr, new PdfBoxColor(rgb));
    }
    return colorMap.get(rgbStr);
  }
  
  public boolean isWhite() {
    return isWhite(0);
  }
  
  public boolean isWhite(float tolerance) {
    return rgb[0] >= (1 - tolerance) && rgb[1] >= (1 - tolerance) 
        && rgb[2] >= (1 - tolerance);
  }
  
  @Override
  public PdfDocument getPdfDocument() {
    return null;
  }
  
  @Override
  public boolean equals(Object object) {
    // If the object is an PDColor, compare the rgb arrays.
    if (object instanceof PDColor) {
      PDColor color = (PDColor) object;
      float[] components = color.getComponents();
      PDColorSpace colorSpace = color.getColorSpace();
      if (colorSpace != null) {
        try {
          float[] rgb = colorSpace.toRGB(components);
          return Arrays.equals(rgb, this.rgb);
        } catch (IOException e) {
          return false;
        }
      }
      return false;
    } else if (object instanceof PdfColor) {
      return object == this;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    return Arrays.hashCode(rgb);
  }
  
  @Override
  public String getId() {
    return this.id;
  }
  
  /**
   * Transforms the given color to rgb array.
   */
  public static float[] toRGB(PDColor color) {
    try {
      float[] components = color.getComponents();
      PDColorSpace colorSpace = color.getColorSpace();
      if (colorSpace != null) {
        return colorSpace.toRGB(components);
      }
    } catch (IOException e) {
      return null;
    }
    return null;
  }

  @Override
  public float getR() {
    return this.rgb[0];
  }

  @Override
  public float getG() {
    return this.rgb[1];
  }

  @Override
  public float getB() {
    return this.rgb[2];
  }
  
  @Override 
  public float[] getRGB() {
    return this.rgb;
  }
}
