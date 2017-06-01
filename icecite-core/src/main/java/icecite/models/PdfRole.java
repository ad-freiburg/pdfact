package icecite.models;

/**
 * An enumeration of semantic roles for paragraphs of PDF documents.
 * 
 * @author Claudius Korzen
 */
public enum PdfRole {
  /**
   * The abstract.
   */
  ABSTRACT("abstract"),
  /**
   * The heading of the abstract.
   */
  ABSTRACT_HEADING("abstract heading"),
  /**
   * The acknowledgments.
   */
  ACKNOWLEDGMENTS("acknowledgments"),
  /**
   * The heading of the acknowledgments.
   */
  ACKNOWLEDGMENTS_HEADING("acknowledgments heading"),
  /**
   * The affiliation.
   */
  AFFILIATION("affiliation"),  
  /**
   * An appendix.
   */
  APPENDIX("appendix"),
  /**
   * A heading of an appendix.
   */
  APPENDIX_HEADING("appendix heading"),
  /**
   * The authors.
   */
  AUTHORS("authors"),
  /**
   * A part of the body text.
   */
  BODY_TEXT("body text"),
  /**
   * A heading of a body text section.
   */
  BODY_TEXT_HEADING("body text heading"),
  /**
   * A caption.
   */
  CAPTION("caption"),
  /**
   * A caption of a figure.
   */
  FIGURE_CAPTION("figure caption"),
  /**
   * A footnote.
   */
  FOOTNOTE("footnote"),
  /**
   * A formula.
   */
  FORMULA("formula"),
  /**
   * An item of a list.
   */
  LIST_ITEM("list item"),
  /**
   * A page header.
   */
  PAGE_HEADER("page header"),
  /**
   * A page footer.
   */
  PAGE_FOOTER("page footer"),
  /**
   * The title.
   */
  TITLE("title"),
  /**
   * A reference.
   */
  REFERENCE("reference"),
  /**
   * The heading of the references section.
   */
  REFERENCES_HEADING("references heading"),
  /**
   * A table.
   */
  TABLE("table"),
  /**
   * A caption of a table.
   */
  TABLE_CAPTION("table caption");
  
  
  /**
   * The name of this role.
   */
  protected String name;

  /**
   * Creates a new PDF role.
   * 
   * @param name
   *        The name of the role.
   */
  PdfRole(String name) {
    this.name = name;
  }

  /**
   * Returns the name of this role.
   * 
   * @return The name of this role.
   */
  public String getName() {
    return this.name;
  }
}
