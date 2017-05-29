package icecite.models.plain;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.assistedinject.AssistedInject;

import icecite.models.PdfParagraph;
import icecite.models.PdfTextLine;
import icecite.models.PdfType;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;

/**
 * A plain implementation of {@link PdfParagraph}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraph extends PlainPdfElement implements PdfParagraph {
  /**
   * The text lines of this paragraph.
   */
  protected List<PdfTextLine> textLines;

  /**
   * The text of this paragraph.
   */
  protected String text;

  // ==========================================================================
  // Constructors.

  /**
   * Creates an empty paragraph.
   * 
   * @param rectangleFactory
   *        The factory to create instances of Rectangle.
   */
  @AssistedInject
  public PlainPdfParagraph(RectangleFactory rectangleFactory) {
    this.textLines = new ArrayList<>();
    this.boundingBox = rectangleFactory.create();
  }

  // ==========================================================================

  @Override
  public List<PdfTextLine> getTextLines() {
    return this.textLines;
  }

  @Override
  public void setTextLines(List<PdfTextLine> textLines) {
    this.textLines = textLines;
  }

  @Override
  public void addTextLine(PdfTextLine textLine) {
    this.textLines.add(textLine);
    // TODO
    if (textLine.getBoundingBox().getMinX() < this.boundingBox.getMinX()) {
      this.boundingBox.setMinX(textLine.getBoundingBox().getMinX());
    }
    if (textLine.getBoundingBox().getMinY() < this.boundingBox.getMinY()) {
      this.boundingBox.setMinY(textLine.getBoundingBox().getMinY());
    }
    if (textLine.getBoundingBox().getMaxX() > this.boundingBox.getMaxX()) {
      this.boundingBox.setMaxX(textLine.getBoundingBox().getMaxX());
    }
    if (textLine.getBoundingBox().getMaxY() > this.boundingBox.getMaxY()) {
      this.boundingBox.setMaxY(textLine.getBoundingBox().getMaxY());
    }
  }

  // ==========================================================================

  @Override
  public String getText() {
    return this.text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
  }

  // ==========================================================================

  @Override
  public Rectangle getBoundingBox() {
    return this.boundingBox;
  }

  @Override
  public void setBoundingBox(Rectangle boundingBox) {
    // The bounding box results from the text lines of this paragraph.
    throw new UnsupportedOperationException();
  }

  // ==========================================================================

  @Override
  public PdfType getType() {
    return PdfType.PARAGRAPHS;
  }
}
