package pdfact.core.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pdfact.core.util.list.ElementList;

/**
 * A document.
 * 
 * @author Claudius Korzen
 */
public class Document implements HasParagraphs {
  /**
   * The path to the underlying file.
   */
  protected Path path;

  /**
   * The pages of this document.
   */
  protected List<Page> pages;

  /**
   * The paragraphs in this document.
   */
  protected ElementList<Paragraph> paragraphs;

  /**
   * The statistics about all characters in this document.
   */
  protected CharacterStatistic characterStatistic;

  /**
   * The statistics about all text lines in this document.
   */
  protected TextLineStatistic textLineStatistic;

  // ==============================================================================================

  /**
   * Creates a new document.
   * 
   * @param path The path to the underlying file.
   */
  public Document(String path) {
    this(Paths.get(path));
  }

  /**
   * Creates a new document.
   * 
   * @param path The path to the underlying file.
   */
  public Document(File path) {
    this(path.toPath());
  }

  /**
   * Creates a new document.
   * 
   * @param path The path to the underlying file.
   */
  public Document(Path path) {
    this.pages = new ArrayList<>();
    this.paragraphs = new ElementList<>();
    this.path = path;
  }

  // ==============================================================================================

  /**
   * Returns the path to the underlying file.
   * 
   * @return The path to the underlying file.
   */
  public File getFile() {
    return this.path != null ? this.path.toFile() : null;
  }

  /**
   * Sets the path to the underlying file.
   * 
   * @param file The path to the underlying file.
   */
  public void setFile(File file) {
    this.path = file != null ? file.toPath() : null;
  }

  // ==============================================================================================

  /**
   * Returns the path to the underlying file.
   * 
   * @return The path to the underlying file.
   */
  public Path getPath() {
    return this.path;
  }

  /**
   * Sets the path to the underlying file.
   * 
   * @param path The path to the underlying file.
   */
  public void setPath(Path path) {
    this.path = path;
  }

  // ==============================================================================================

  /**
   * Returns the pages of this document.
   * 
   * @return The pages of this document.
   */
  public List<Page> getPages() {
    return this.pages;
  }

  /**
   * Returns the first page of this document.
   * 
   * @return The first page of this document or null if there are no pages.
   */
  public Page getFirstPage() {
    if (this.pages == null || this.pages.isEmpty()) {
      return null;
    }
    return this.pages.get(0);
  }

  /**
   * Returns the last page of this document.
   * 
   * @return The last page of this document or null if there are no pages.
   */
  public Page getLastPage() {
    if (this.pages == null || this.pages.isEmpty()) {
      return null;
    }
    return this.pages.get(this.pages.size() - 1);
  }

  /**
   * Sets the pages of this document.
   * 
   * @param pages The pages to set.
   */
  public void setPages(List<Page> pages) {
    this.pages = pages;
  }

  /**
   * Adds the given page to this document.
   * 
   * @param page The page to add.
   */
  public void addPage(Page page) {
    this.pages.add(page);
  }

  // ==============================================================================================

  @Override
  public ElementList<Paragraph> getParagraphs() {
    return this.paragraphs;
  }

  @Override
  public Paragraph getFirstParagraph() {
    if (this.paragraphs == null || this.paragraphs.isEmpty()) {
      return null;
    }
    return this.paragraphs.get(0);
  }

  @Override
  public Paragraph getLastParagraph() {
    if (this.paragraphs == null || this.paragraphs.isEmpty()) {
      return null;
    }
    return this.paragraphs.get(this.paragraphs.size() - 1);
  }

  @Override
  public void setParagraphs(ElementList<Paragraph> paragraphs) {
    this.paragraphs = paragraphs;
  }

  @Override
  public void addParagraphs(ElementList<Paragraph> paragraphs) {
    this.paragraphs.addAll(paragraphs);
  }

  @Override
  public void addParagraph(Paragraph paragraph) {
    this.paragraphs.add(paragraph);
  }

  // ==============================================================================================

  @Override
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==============================================================================================

  @Override
  public TextLineStatistic getTextLineStatistic() {
    return this.textLineStatistic;
  }

  @Override
  public void setTextLineStatistic(TextLineStatistic statistic) {
    this.textLineStatistic = statistic;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfDocument(" + this.path + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Document) {
      Document otherDocument = (Document) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPath(), otherDocument.getPath());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPath());
    return builder.hashCode();
  }
}
