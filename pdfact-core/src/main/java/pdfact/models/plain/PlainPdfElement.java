package pdfact.models.plain;

import pdfact.models.PdfElement;
import pdfact.models.PdfElementType;

/**
 * A plain implementation of {@link PdfElement}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfElement implements PdfElement {
  @Override
  public PdfElementType getType() {
    return null;
  }
}
