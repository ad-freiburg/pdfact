package model;

/**
 * Implementation of a PdfParagraph.
 *
 * @author Claudius Korzen
 *
 */
public class PdfXYCutNonTextParagraph extends PdfXYCutArea 
    implements PdfNonTextParagraph {  
  /**
   * The context of this paragraph.
   */
  protected String context;
  
  /**
   * The role of this paragraph.
   */
  protected PdfRole role = PdfRole.UNKNOWN;
  
  /**
   * The default constructor.
   */
  public PdfXYCutNonTextParagraph(PdfPage page) {
    super(page);
  }

  /**
   * The default constructor.
   */
  public PdfXYCutNonTextParagraph(PdfPage page, PdfArea area) {
    this(page);
    
    setTextLines(area.getTextLines());
  }

  @Override
  public PdfFeature getFeature() {
    return PdfFeature.paragraphs;
  }

  @Override
  public void setContext(String context) {
    this.context = context;
  }

  @Override
  public String getContext() {
    return this.context;
  }
  
  @Override
  public void setRole(PdfRole role) {
    this.role = role;
    for (PdfElement element : getElements()) {
      element.setRole(role);
    }
  }

  @Override
  public PdfRole getRole() {
    return this.role;
  }

  @Override
  public void addNonTextElement(PdfElement element) {
    this.elementsIndex.insert(element);
    this.nonTextElementsIndex.insert(element);
  }
}
