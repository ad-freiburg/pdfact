package icecite.models.plain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfPage;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.utils.geometric.Rectangle;

// TODO: Don't derive the bounding box in the model.

/**
 * A plain implementation of {@link PdfTextBlock}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfTextBlock extends PlainPdfElement implements PdfTextBlock {
  /**
   * The text lines of this text block.
   */
  protected PdfTextLineList textLines;

  /**
   * The text of this text block.
   */
  protected String text;

  // ==========================================================================

  /**
   * Creates a new text block.
   * 
   * @param page
   *        The page in which this text block is located.
   * @param textLineListFactory
   *        The text lines of this text block.
   */
  @AssistedInject
  public PlainPdfTextBlock(PdfTextLineListFactory textLineListFactory,
      @Assisted PdfPage page) {
    this.textLines = textLineListFactory.create();
    this.page = page;
  }

  // ==========================================================================

  @Override
  public PdfPage getPage() {
    return this.page;
  }

  @Override
  public void setPage(PdfPage page) {
    this.page = page;
  }

  // ==========================================================================

  @Override
  public PdfTextLineList getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(PdfTextLineList textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLines(PdfTextLineList textLines) {
    for (PdfTextLine line : textLines) {
      addTextLine(line);
    }
  }

  @Override
  public void addTextLine(PdfTextLine line) {
    this.textLines.add(line);
  }

  // ==========================================================================

  @Override
  public Rectangle getRectangle() {
    return this.textLines.getRectangle();
  }

  @Override
  public void setRectangle(Rectangle boundingBox) {
    // The bounding box results from the characters of this text block.
    throw new UnsupportedOperationException();
  }

  // ==========================================================================

  @Override
  public String toString() {
    return "PlainPdfTextBlock(page: " + this.page.getPageNumber() + ", rect: "
        + this.boundingBox + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfTextBlock) {
      PdfTextBlock otherTextBlock = (PdfTextBlock) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPage(), otherTextBlock.getPage());
      builder.append(getRectangle(), otherTextBlock.getRectangle());
      builder.append(getTextLines(), otherTextBlock.getTextLines());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPage());
    builder.append(getRectangle());
    builder.append(getTextLines());
    return builder.hashCode();
  }

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }
}
