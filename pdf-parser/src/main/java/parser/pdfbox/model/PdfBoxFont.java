package parser.pdfbox.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;

import de.freiburg.iif.path.LineReader;
import model.PdfDocument;
import model.PdfFont;

/**
 * An implementation of a PdfFont using PdfBox. 
 *
 * @author Claudius Korzen
 */
public class PdfBoxFont implements PdfFont {
  /**
   * Maps a PDFont to its equivalent PdfFont object.
   */
  protected static Map<String, PdfBoxFont> afmMap = new HashMap<>();
  
  /**
   * The unique id of this font.
   */
  protected String id;
  
  /**
   * The font in fashion of PdfBox.
   */
  protected PDFont font;
     
  /** 
   * The font dictionary. 
   */
  protected COSDictionary fontDict;
  
  /** 
   * The font descriptor. 
   */
  protected COSDictionary fontDesc;
  
  /** 
   * The pdf name of this font. 
   */
  protected String name;
  
  /** 
   * The name of this font. 
   */
  protected String basename;
  
  /** 
   * The full name of this font. 
   */
  protected String fullName;
  
  /** 
   * The family name of this font. 
   */
  protected String familyName;
  
  /**
   * The flag to indicate, that the weight of this font is bold.
   */
  protected boolean isBold;
  
  /** 
   * The flag to indicate, that the weight of this font is italic. 
   */
  protected boolean isItalic;
  
  /** 
   * The flag to indicate, that this font is type3 font. 
   */
  protected boolean isType3Font;
  
  /** 
   * The flag to indicate, that this font is a symbolic font. 
   */
  protected boolean isSymbolicFont;
  
  /**
   * The default constructor.
   */
  protected PdfBoxFont() {
    this.id = "font-" + afmMap.size();
  }
  
  /**
   * The default constructor.
   */
  protected PdfBoxFont(PDFont font) {
    this();
    this.font = font;
    this.name = font.getName();
    this.basename = computeBasename(font);
    this.isType3Font = font instanceof PDType3Font;
    // pdfFont.isSymbolicFont = font.isSymbolicFont(); TODO
    
    // Normalize all kinds of fonttypes. There are several possible version
    // which have to be normalized e.g. Arial,Bold Arial-BoldMT
    // Helvetica-oblique ...
    this.basename = this.basename.replaceAll(" ", "")
        .replaceAll(",", "")
        .replaceAll("-", "");

    int length1 = this.basename.length();
    this.basename = this.basename.replaceAll("extrabold", "")
        .replaceAll("bold", "")
        .replaceAll("black", "");
    int length2 = this.basename.length();
    this.isBold = length2 < length1;
    
    this.basename = this.basename.replaceAll("italic", "")
        .replaceAll("oblique", "");
    length1 = this.basename.length();
    this.isItalic = length1 < length2;

    this.basename = this.basename
        .replaceAll("condensed", "")
        .replaceAll("medium", "")
        .replaceAll("regular", "")
        .replaceAll("light", "");
  }
  
  /**
   * Creates a PdfBoxFont from given PDFont. For two equal PDFonts, also the
   * equivalent PdfBoxFonts will be equal.
   */
  public static PdfBoxFont create(PDFont font) throws IOException {
    if (font == null) {
      return null;
    }
        
    if (afmMap.isEmpty()) {
      readAfmMapFromFile();
    }
    
    String basename = computeBasename(font);
    
    // Check, if the afm map contains the given font.
    PdfBoxFont pdfFont = afmMap.get(basename);
    
    if (pdfFont == null) {
      pdfFont = new PdfBoxFont(font);
      afmMap.put(basename, pdfFont);
    }
    
    return pdfFont;
  }
   
  // ___________________________________________________________________________
  
  @Override
  public String getId() {
    return id;
  }
  
  @Override
  public String getName() {
    return font.getName();
  }
  
  @Override
  public String getBasename() {
    return basename;
  }
  
  @Override
  public String getFullName() {
    return this.fullName;
  }

  @Override
  public String getFamilyName() {
    return this.familyName;
  }

  @Override
  public boolean isItalic() {
    return isItalic;
  }

  @Override
  public boolean isBold() {
    return isBold;
  }
  
  @Override
  public boolean isType3Font() {
    return font instanceof PDType3Font;
  }
  
  /**
   * Returns the font in fashion of PdfBox.
   */
  public PDFont getPdFont() {
    return this.font;
  }
    
  @Override
  public PdfDocument getPdfDocument() {
    return null;
  }
  
  // ___________________________________________________________________________
    
  /**
   * Reads the afm map from file.
   * 
   * @return the afm map.
   * @throws IOException
   *           if reading the file fails.
   */
  protected static void readAfmMapFromFile() throws IOException {
    LineReader reader = new LineReader() {
      @Override
      public void handleLine(String line) {
        if (!line.trim().isEmpty()) {
          String[] fields = line.split("\t");

          // Parse the tab-separated line.
          PdfBoxFont font = new PdfBoxFont();
          font.basename = fields[0];
          font.fullName = fields[1];
          font.familyName = fields[2];
          font.isBold = fields[3].trim().equals("1");
          font.isItalic = fields[4].trim().equals("1");

          afmMap.put(font.getBasename(), font);
        }
      }
    };
    reader.read("afm.map");
  }

  /**
   * Computes the normalized fontname of the font. This is a string with only
   * the fontname and no additional style informations (like "bold" or
   * "italic").
   * 
   * @param font
   *          the font to analyze.
   * @return the derived font name.
   */
  public static String computeBasename(PDFont font) {
    // Terminate all whitespaces, commas and hyphens
    String fontname;
    if (font instanceof PDType3Font) {
      fontname = "type3";
    } else {
      fontname = font.getName().toLowerCase();

      // Terminate trailing characters up to the "+".
      // As far as I know, these characters are used in names of embedded fonts
      // If the embedded font can't be read, we'll try to find it here
      if (fontname.indexOf("+") > -1) {
        fontname = fontname.substring(fontname.indexOf("+") + 1);
      }
    }

    return fontname;
  }
}
