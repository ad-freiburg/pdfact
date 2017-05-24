package icecite.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacter.PdfCharacterFactory;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;
import icecite.models.PdfColorRegistry;
import icecite.models.PdfDocument;
import icecite.models.PdfDocument.PdfDocumentFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfFigure.PdfFigureFactory;
import icecite.models.PdfFont;
import icecite.models.PdfFont.PdfFontFactory;
import icecite.models.PdfFontRegistry;
import icecite.models.PdfPage;
import icecite.models.PdfPage.PdfPageFactory;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShape.PdfShapeFactory;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.models.plain.PlainPdfCharacter;
import icecite.models.plain.PlainPdfCharacterList;
import icecite.models.plain.PlainPdfColor;
import icecite.models.plain.PlainPdfColorRegistry;
import icecite.models.plain.PlainPdfDocument;
import icecite.models.plain.PlainPdfFigure;
import icecite.models.plain.PlainPdfFont;
import icecite.models.plain.PlainPdfFontRegistry;
import icecite.models.plain.PlainPdfPage;
import icecite.models.plain.PlainPdfParagraph;
import icecite.models.plain.PlainPdfShape;
import icecite.models.plain.PlainPdfTextBlock;
import icecite.models.plain.PlainPdfTextLine;
import icecite.models.plain.PlainPdfWord;
import icecite.parser.PdfParser;
import icecite.parser.PdfParser.PdfParserFactory;
import icecite.parser.PlainPdfParser;
import icecite.parser.stream.PdfStreamParser;
import icecite.parser.stream.PdfStreamParser.PdfStreamParserFactory;
import icecite.parser.stream.pdfbox.PdfBoxPdfStreamParser;
import icecite.tokenizer.PdfParagraphTokenizer;
import icecite.tokenizer.PdfTextBlockTokenizer;
import icecite.tokenizer.PdfTextLineTokenizer;
import icecite.tokenizer.PdfTextTokenizer;
import icecite.tokenizer.PdfWordTokenizer;
import icecite.tokenizer.PlainPdfParagraphTokenizer;
import icecite.tokenizer.PlainPdfTextTokenizer;
import icecite.tokenizer.XYCutPdfTextBlockTokenizer;
import icecite.tokenizer.XYCutPdfTextLineTokenizer;
import icecite.tokenizer.XYCutPdfWordTokenizer;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Line.LineFactory;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Point.PointFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;
import icecite.utils.geometric.plain.PlainLine;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;

// TODO: Find out how to dynamically update bindings (needed for the parser,
// where PdfBox needs extra bindings.

/**
 * A module that defines the basic Guice bindings for Icecite.
 * 
 * @author Claudius Korzen
 */
public class IceciteBaseModule extends AbstractModule {

  @Override
  protected void configure() {
    // Bind the parsers.
    fc(PdfParser.class, PdfParserFactory.class, PlainPdfParser.class);
    fc(PdfStreamParser.class, PdfStreamParserFactory.class,
        PdfBoxPdfStreamParser.class);

    // Bind the tokenizers.
    bind(PdfTextTokenizer.class).to(PlainPdfTextTokenizer.class);
    bind(PdfTextBlockTokenizer.class).to(XYCutPdfTextBlockTokenizer.class);
    bind(PdfTextLineTokenizer.class).to(XYCutPdfTextLineTokenizer.class);
    bind(PdfWordTokenizer.class).to(XYCutPdfWordTokenizer.class);
    bind(PdfParagraphTokenizer.class).to(PlainPdfParagraphTokenizer.class);

    // Bind the registries for PdfColor and PdfFont.
    bind(PdfColorRegistry.class).to(PlainPdfColorRegistry.class)
        .in(Singleton.class);
    bind(PdfFontRegistry.class).to(PlainPdfFontRegistry.class)
        .in(Singleton.class);

    // Bind the PDF model factories.
    fc(PdfCharacter.class, PdfCharacterFactory.class, PlainPdfCharacter.class);
    fc(PdfDocument.class, PdfDocumentFactory.class, PlainPdfDocument.class);
    fc(PdfFigure.class, PdfFigureFactory.class, PlainPdfFigure.class);
    fc(PdfPage.class, PdfPageFactory.class, PlainPdfPage.class);
    fc(PdfShape.class, PdfShapeFactory.class, PlainPdfShape.class);
    fc(PdfTextBlock.class, PdfTextBlockFactory.class, PlainPdfTextBlock.class);
    fc(PdfWord.class, PdfWordFactory.class, PlainPdfWord.class);
    fc(PdfTextLine.class, PdfTextLineFactory.class, PlainPdfTextLine.class);
    fc(PdfParagraph.class, PdfParagraphFactory.class, PlainPdfParagraph.class);
    fc(PdfFont.class, PdfFontFactory.class, PlainPdfFont.class);
    fc(PdfColor.class, PdfColorFactory.class, PlainPdfColor.class);

    fc(PdfCharacterList.class, PdfCharacterListFactory.class,
        PlainPdfCharacterList.class);

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
