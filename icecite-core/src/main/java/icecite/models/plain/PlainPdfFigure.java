package icecite.models.plain;

import icecite.models.PdfFigure;
import icecite.models.PdfType;

/**
 * A plain implementation of {@link PdfFigure}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfFigure extends PlainPdfElement implements PdfFigure {
  @Override
  public PdfType getType() {
    return PdfType.FIGURES;
  }
}
