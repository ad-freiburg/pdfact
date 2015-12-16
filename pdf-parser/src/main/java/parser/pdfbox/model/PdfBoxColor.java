package parser.pdfbox.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.json.JSONObject;

import de.freiburg.iif.text.StringUtils;
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
   * The color in fashion of PdfBox.
   */
  protected PDColor color;
  
  /**
   * The rgb array.
   */
  protected float[] rgb = { 0.0f, 0.0f, 0.0f };
    
  /**
   * The default constructor.
   */
  protected PdfBoxColor(PDColor color) {
    this.color = color;
    this.id = "color-" + colorMap.size();
    this.rgb = toRGB(color);
  }
  
  /**
   * Creates a PdfBoxColor from given PDColor. For two equal PDColors, also the
   * equivalent PdfBoxColors will be equal.
   */
  public static PdfBoxColor create(PDColor color) throws IOException {
    float[] rgb = toRGB(color);
    String rgbStr = rgb != null ? Arrays.toString(rgb) : null;
    
    if (!colorMap.containsKey(rgbStr)) {
      colorMap.put(rgbStr, new PdfBoxColor(color));
    }
    return colorMap.get(rgbStr);
  }
    
  /**
   * Returns the color in fashion of PdfBox.
   */
  public PDColor getPdColor() {
    return this.color;
  }
  
  public boolean isWhite() {
    return isWhite(0);
  }
  
  public boolean isWhite(float tolerance) {
    return rgb[0] > (1 - tolerance) && rgb[1] > (1 - tolerance) 
        && rgb[2] > (1 - tolerance);
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
  public String toTsv() {
    StringBuilder tsv = new StringBuilder();
    
    tsv.append("color");
    tsv.append("\t");
    tsv.append(getId());
    tsv.append("\t");
    tsv.append(Arrays.toString(rgb));
    
    return tsv.toString();
  }

  @Override
  public String toXml(int indentLevel, int indentLength) {
    StringBuilder xml = new StringBuilder();
    
    String indent = StringUtils.repeat(" ", indentLevel * indentLength);
        
    xml.append(indent);
    xml.append("<");
    xml.append("color");
    xml.append(" id=\"" + getId() + "\"");
    xml.append(" r=\"" + rgb[0] + "\"");
    xml.append(" g=\"" + rgb[1] + "\"");
    xml.append(" b=\"" + rgb[2] + "\"");
    xml.append(" />"); 
    
    return xml.toString();
  }

  @Override
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    
    json.put("id", getId());
    json.put("r", rgb[0]);
    json.put("g", rgb[1]);
    json.put("b", rgb[2]);
    
    return json;
  }

  @Override
  public String getId() {
    return this.id;
  }
  
  /**
   * Transforms the given color to rgb array.
   */
  protected static float[] toRGB(PDColor color) {
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
}
