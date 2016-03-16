package parser.pdfbox.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDDocument;

import model.Comparators;
import model.DimensionStatistics;
import model.PdfColor;
import model.PdfDocument;
import model.PdfFont;
import model.PdfPage;
import model.PdfTextAlignment;
import model.TextLineStatistics;
import model.TextStatistics;
import statistics.DimensionStatistician;
import statistics.TextLineStatistician;
import statistics.TextStatistician;

/**
 * Concrete implementation of a PdfTextDocument using PdfBox.
 *
 * @author Claudius Korzen
 */
public class PdfBoxDocument implements PdfDocument {
  /**
   * The document in fashion of PdfBox.
   */
  protected PDDocument document;

  /**
   * The list of pages.
   */
  protected List<PdfPage> pages;

  /**
   * The pdf file, from which the data were extracted.
   */
  protected Path pdfFile;

  /**
   * The dimension statistics.
   */
  protected DimensionStatistics dimensionStatistics;
  
  /**
   * The text statistics.
   */
  protected TextStatistics textStatistics;
  
  /**
   * The text line statistics.
   */
  protected TextLineStatistics textLineStatistics;
  
  /**
   * Flag to indicate whether the dimension statistics need an update.
   */
  protected boolean needsDimensionStatisticsUpdate;

  /**
   * Flag to indicate whether the text statistics need an update.
   */
  protected boolean needsTextStatisticsUpdate;

  /**
   * Flag to indicate whether the text line statistics need an update.
   */
  protected boolean needsTextLineStatisticsUpdate;
  
  /**
   * The alignment of text line in this document.
   */
  protected PdfTextAlignment alignment;
  
  /**
   * The markup of section headings.
   */
  protected String sectionHeadingMarkup;
  
  // ___________________________________________________________________________

  /**
   * The default constructor.
   */
  public PdfBoxDocument(PDDocument document) {
    this.document = document;
    this.pages = new ArrayList<>();
  }

  @Override
  public List<PdfPage> getPages() {
    return this.pages;
  }

  /**
   * Adds the given page to this text document.
   */
  public void addTextPage(PdfBoxPage page) {
    if (page != null) {
      page.setPdfDocument(this);
      this.pages.add(page);
      this.needsDimensionStatisticsUpdate = true;
      this.needsTextStatisticsUpdate = true;
      this.needsTextLineStatisticsUpdate = true;
    }
  }

  /**
   * Sets the list of pages of this document to the given one.
   */
  public void setTextPages(List<PdfBoxPage> pages) {
    this.pages.clear();
    if (pages != null) {
      for (PdfBoxPage page : pages) {
        addTextPage(page);
      }
    }
  }

  /**
   * Returns the document in fashion of PdfBox.
   */
  public PDDocument getPdDocument() {
    return this.document;
  }

  @Override
  public Path getPdfFile() {
    return this.pdfFile;
  }

  /**
   * Sets the pdf file, from which the data were extracted.
   */
  public void setPdfFile(Path pdfFile) {
    this.pdfFile = pdfFile;
  }

  // ___________________________________________________________________________

  @Override
  public DimensionStatistics getDimensionStatistics() {
    if (dimensionStatistics == null || needsDimensionStatisticsUpdate) {
      this.dimensionStatistics = DimensionStatistician.accumulate(getPages());
      this.needsDimensionStatisticsUpdate = false;
    }
    return this.dimensionStatistics;
  }

  @Override
  public TextStatistics getTextStatistics() {
    if (textStatistics == null || needsTextStatisticsUpdate) {
      this.textStatistics = TextStatistician.accumulate(getPages());
      this.needsTextStatisticsUpdate = false;
    }
    return this.textStatistics;
  }
  
  @Override
  public TextLineStatistics getTextLineStatistics() {
    if (textLineStatistics == null || needsTextLineStatisticsUpdate) {
      this.textLineStatistics = TextLineStatistician.accumulate(getPages());
      this.needsTextLineStatisticsUpdate = false;
    }
    return this.textLineStatistics;
  }

  @Override
  public List<PdfFont> getFonts() {
    HashMap<String, PdfFont> fonts = new HashMap<>();
    
    for (PdfPage page : getPages()) {
      for (PdfFont font : page.getFonts()) {
        fonts.put(font.getId(), font);
      }
    }
    List<PdfFont> fontsList = new ArrayList<>(fonts.values());
    
    Collections.sort(fontsList, new Comparators.IdComparator());
    
    return fontsList;
  }

  @Override
  public List<PdfColor> getColors() {
    HashMap<String, PdfColor> colors = new HashMap<>();
    
    for (PdfPage page : getPages()) {
      for (PdfColor color : page.getColors()) {
        colors.put(color.getId(), color);
      }
    }
    List<PdfColor> colorsList = new ArrayList<>(colors.values());
    
    Collections.sort(colorsList, new Comparators.IdComparator());
    
    return colorsList;
  }

  @Override
  public float getFontsize() {
    return getTextStatistics().getMostCommonFontsize();
  }
  
  @Override
  public PdfTextAlignment getTextAlignment() {
    if (alignment == null) {
      alignment = computeTextAlignment();
    }
    return alignment;
  }
  
  public PdfTextAlignment computeTextAlignment() {
    Map<PdfTextAlignment, Integer> alignmentFreqs = new HashMap<>(); 
    
    for (PdfPage page : getPages()) {
      int freq = 0;
      if (alignmentFreqs.containsKey(page.getTextLineAlignment())) {
        freq = alignmentFreqs.get(page.getTextLineAlignment());
      }
      alignmentFreqs.put(page.getTextLineAlignment(), freq + 1);
    }
    
    int maxFreq = 0;
    PdfTextAlignment maxFreqAlignment = null;
    for (Entry<PdfTextAlignment, Integer> entry : alignmentFreqs.entrySet()) {
      if (entry.getValue() > maxFreq) {
        maxFreq = entry.getValue();
        maxFreqAlignment = entry.getKey();
      }
    }
    
    return maxFreqAlignment;
  }

  @Override
  public void setSectionHeadingMarkup(String markup) {
    this.sectionHeadingMarkup = markup;
  }
  
  @Override
  public String getSectionHeadingMarkup() {
    return sectionHeadingMarkup;
  }
}
