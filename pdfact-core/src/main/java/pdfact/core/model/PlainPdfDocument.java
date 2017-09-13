package pdfact.core.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.core.util.list.ElementList;
import pdfact.core.util.list.ElementList.ElementListFactory;

/**
 * A plain implementation of {@link PdfDocument}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfDocument implements PdfDocument {
  /**
   * The path to the underlying PDF file.
   */
  protected Path path;

  /**
   * The pages of this PDF document.
   */
  protected List<Page> pages;

  /**
   * The paragraphs in this PDF document.
   */
  protected ElementList<Paragraph> paragraphs;

  /**
   * The statistics about all characters in this PDF document.
   */
  protected CharacterStatistic characterStatistic;

  /**
   * The statistics about all text lines in this PDF document.
   */
  protected TextLineStatistic textLineStatistic;

  /**
   * Creates a new PDF document.
   * 
   * @param paragraphListFactory
   *        The factory to create lists of paragraphs.
   * @param path
   *        The path to the underlying PDF file.
   */
  @AssistedInject
  public PlainPdfDocument(ElementListFactory<Paragraph> paragraphListFactory,
      @Assisted String path) {
    this(paragraphListFactory, Paths.get(path));
  }

  /**
   * Creates a new PDF document.
   * 
   * @param paragraphListFactory
   *        The factory to create lists of paragraphs.
   * @param path
   *        The path to the underlying PDF file.
   */
  @AssistedInject
  public PlainPdfDocument(ElementListFactory<Paragraph> paragraphListFactory,
      @Assisted File path) {
    this(paragraphListFactory, path.toPath());
  }

  /**
   * Creates a new PDF document.
   * 
   * @param paragraphListFactory
   *        The factory to create lists of paragraphs.
   * @param path
   *        The path to the underlying PDF file.
   */
  @AssistedInject
  public PlainPdfDocument(ElementListFactory<Paragraph> paragraphListFactory,
      @Assisted Path path) {
    this.pages = new ArrayList<>();
    this.paragraphs = paragraphListFactory.create();
    this.path = path;
  }

  // ==========================================================================

  @Override
  public File getFile() {
    return this.path != null ? this.path.toFile() : null;
  }

  @Override
  public void setFile(File file) {
    this.path = file != null ? file.toPath() : null;
  }

  // ==========================================================================

  @Override
  public Path getPath() {
    return this.path;
  }

  @Override
  public void setPath(Path path) {
    this.path = path;
  }

  // ==========================================================================

  @Override
  public List<Page> getPages() {
    return this.pages;
  }

  @Override
  public Page getFirstPage() {
    if (this.pages == null || this.pages.isEmpty()) {
      return null;
    }
    return this.pages.get(0);
  }

  @Override
  public Page getLastPage() {
    if (this.pages == null || this.pages.isEmpty()) {
      return null;
    }
    return this.pages.get(this.pages.size() - 1);
  }

  @Override
  public void setPages(List<Page> pages) {
    this.pages = pages;
  }

  @Override
  public void addPage(Page page) {
    this.pages.add(page);
  }

  // ==========================================================================

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

  // ==========================================================================

  @Override
  public CharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(CharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public TextLineStatistic getTextLineStatistic() {
    return this.textLineStatistic;
  }

  @Override
  public void setTextLineStatistic(TextLineStatistic statistic) {
    this.textLineStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PdfDocument(" + this.path + ")";
  }

  // ==========================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfDocument) {
      PdfDocument otherDocument = (PdfDocument) other;

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
