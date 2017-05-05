package icecite.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacterFactory;
import icecite.models.PdfColor;
import icecite.models.PdfColorFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfDocumentFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfFigureFactory;
import icecite.models.PdfFont;
import icecite.models.PdfFontFactory;
import icecite.models.PdfPage;
import icecite.models.PdfPageFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShapeFactory;
import icecite.models.plain.PlainPdfCharacter;
import icecite.models.plain.PlainPdfColor;
import icecite.models.plain.PlainPdfDocument;
import icecite.models.plain.PlainPdfFigure;
import icecite.models.plain.PlainPdfFont;
import icecite.models.plain.PlainPdfPage;
import icecite.models.plain.PlainPdfShape;
import icecite.parser.PdfParser;
import icecite.parser.pdfbox.PdfBoxPdfParser;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.LineFactory;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.PointFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.RectangleFactory;
import icecite.utils.geometric.plain.PlainLine;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;

/**
 * A module that defines the basic Guice bindings for Icecite.
 * 
 * @author Claudius Korzen
 */
public class IceciteBaseModule extends AbstractModule {

  @Override
  protected void configure() {
    // This tells Guice that whenever it sees a dependency on a PdfParser, it
    // should satisfy the dependency using a PdfBoxPdfParser.
    bind(PdfParser.class).to(PdfBoxPdfParser.class);

    // Bind the PDF model factories.
    fc(PdfCharacter.class, PdfCharacterFactory.class, PlainPdfCharacter.class);
    fc(PdfColor.class, PdfColorFactory.class, PlainPdfColor.class);
    fc(PdfDocument.class, PdfDocumentFactory.class, PlainPdfDocument.class);
    fc(PdfFigure.class, PdfFigureFactory.class, PlainPdfFigure.class);
    fc(PdfFont.class, PdfFontFactory.class, PlainPdfFont.class);
    fc(PdfPage.class, PdfPageFactory.class, PlainPdfPage.class);
    fc(PdfShape.class, PdfShapeFactory.class, PlainPdfShape.class);

    // Bind the geometric model factories.
    fc(Rectangle.class, RectangleFactory.class, PlainRectangle.class);
    fc(Line.class, LineFactory.class, PlainLine.class);
    fc(Point.class, PointFactory.class, PlainPoint.class);
  }

  /**
   * Binds the given interface and the given factory to the given
   * implementation.
   * 
   * @param c
   *        The interface to process.
   * @param f
   *        The factory to process.
   * @param i
   *        The implementation to process.
   */
  protected <T, F> void fc(Class<T> c, Class<F> f, Class<? extends T> i) {
    install(new FactoryModuleBuilder().implement(c, i).build(f));
  }
}
