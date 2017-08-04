package pdfact.models.plain;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfTextLineStatistic;

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
  protected List<PdfPage> pages;

  /**
   * The paragraphs in this PDF document.
   */
  protected List<PdfParagraph> paragraphs;

  /**
   * The statistics about all characters in this PDF document.
   */
  protected PdfCharacterStatistic characterStatistic;

  /**
   * The statistics about all text lines in this PDF document.
   */
  protected PdfTextLineStatistic textLineStatistic;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF document.
   * 
   * @param pdf
   *        The path to the underlying PDF file.
   */
  @AssistedInject
  public PlainPdfDocument(@Assisted File pdf) {
    this(pdf.toPath());
  }

  /**
   * Creates a new PDF document.
   *
   * @param pdf
   *        The path to the underlying PDF file.
   */
  @AssistedInject
  public PlainPdfDocument(@Assisted Path pdf) {
    this.pages = new ArrayList<>();
    this.paragraphs = new ArrayList<>();
    this.path = pdf;
  }

  // ==========================================================================

  @Override
  public List<PdfPage> getPages() {
    return this.pages;
  }

  @Override
  public void setPages(List<PdfPage> pages) {
    this.pages = pages;
  }

  @Override
  public void addPage(PdfPage page) {
    this.pages.add(page);
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
  public List<PdfParagraph> getParagraphs() {
    return this.paragraphs;
  }

  @Override
  public PdfParagraph getFirstParagraph() {
    if (this.paragraphs == null || this.paragraphs.isEmpty()) {
      return null;
    }
    return this.paragraphs.get(0);
  }

  @Override
  public PdfParagraph getLastParagraph() {
    if (this.paragraphs == null || this.paragraphs.isEmpty()) {
      return null;
    }
    return this.paragraphs.get(this.paragraphs.size() - 1);
  }

  @Override
  public void setParagraphs(List<PdfParagraph> paragraphs) {
    this.paragraphs = paragraphs;
  }

  @Override
  public void addParagraphs(List<PdfParagraph> paragraphs) {
    for (PdfParagraph paragraph : paragraphs) {
      addParagraph(paragraph);
    }
  }

  @Override
  public void addParagraph(PdfParagraph paragraph) {
    this.paragraphs.add(paragraph);
  }

  // ==========================================================================

  @Override
  public PdfCharacterStatistic getCharacterStatistic() {
    return this.characterStatistic;
  }

  @Override
  public void setCharacterStatistic(PdfCharacterStatistic statistic) {
    this.characterStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public PdfTextLineStatistic getTextLineStatistic() {
    return this.textLineStatistic;
  }

  @Override
  public void setTextLineStatistic(PdfTextLineStatistic statistic) {
    this.textLineStatistic = statistic;
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfDocument(" + this.path + ")";
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
