package icecite.guice;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import icecite.models.PdfCharacter;
import icecite.models.PdfCharacter.PdfCharacterFactory;
import icecite.models.PdfCharacterList;
import icecite.models.PdfCharacterList.PdfCharacterListFactory;
import icecite.models.PdfColor;
import icecite.models.PdfColor.PdfColorFactory;
import icecite.models.PdfDocument;
import icecite.models.PdfDocument.PdfDocumentFactory;
import icecite.models.PdfFigure;
import icecite.models.PdfFigure.PdfFigureFactory;
import icecite.models.PdfFont;
import icecite.models.PdfFont.PdfFontFactory;
import icecite.models.PdfFontFace;
import icecite.models.PdfFontFace.PdfFontFaceFactory;
import icecite.models.PdfPage;
import icecite.models.PdfPage.PdfPageFactory;
import icecite.models.PdfParagraph;
import icecite.models.PdfParagraph.PdfParagraphFactory;
import icecite.models.PdfPosition;
import icecite.models.PdfPosition.PdfPositionFactory;
import icecite.models.PdfShape;
import icecite.models.PdfShape.PdfShapeFactory;
import icecite.models.PdfTextBlock;
import icecite.models.PdfTextBlock.PdfTextBlockFactory;
import icecite.models.PdfTextLine;
import icecite.models.PdfTextLine.PdfTextLineFactory;
import icecite.models.PdfTextLineList;
import icecite.models.PdfTextLineList.PdfTextLineListFactory;
import icecite.models.PdfWord;
import icecite.models.PdfWord.PdfWordFactory;
import icecite.models.PdfWordList;
import icecite.models.PdfWordList.PdfWordListFactory;
import icecite.models.plain.PlainPdfCharacter;
import icecite.models.plain.PlainPdfCharacterList;
import icecite.models.plain.PlainPdfColor;
import icecite.models.plain.PlainPdfDocument;
import icecite.models.plain.PlainPdfFigure;
import icecite.models.plain.PlainPdfFont;
import icecite.models.plain.PlainPdfFontFace;
import icecite.models.plain.PlainPdfPage;
import icecite.models.plain.PlainPdfParagraph;
import icecite.models.plain.PlainPdfPosition;
import icecite.models.plain.PlainPdfShape;
import icecite.models.plain.PlainPdfTextBlock;
import icecite.models.plain.PlainPdfTextLine;
import icecite.models.plain.PlainPdfTextLineList;
import icecite.models.plain.PlainPdfWord;
import icecite.models.plain.PlainPdfWordList;
import icecite.parse.PdfParser;
import icecite.parse.PdfParser.PdfParserFactory;
import icecite.parse.PlainPdfParser;
import icecite.parse.stream.PdfStreamParser;
import icecite.parse.stream.PdfStreamParser.PdfStreamParserFactory;
import icecite.parse.stream.pdfbox.PdfBoxPdfStreamParser;
import icecite.semanticize.PdfTextSemanticizer;
import icecite.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import icecite.semanticize.plain.PlainPdfTextSemanticizer;
import icecite.semanticize.plain.modules.AbstractModule;
import icecite.semanticize.plain.modules.AcknowledgmentsModule;
import icecite.semanticize.plain.modules.BodyTextModule;
import icecite.semanticize.plain.modules.CaptionModule;
import icecite.semanticize.plain.modules.CategoriesModule;
import icecite.semanticize.plain.modules.FootnoteModule;
import icecite.semanticize.plain.modules.GeneralTermsModule;
import icecite.semanticize.plain.modules.HeadingModule;
import icecite.semanticize.plain.modules.ItemizeItemModule;
import icecite.semanticize.plain.modules.KeywordsModule;
import icecite.semanticize.plain.modules.PdfTextSemanticizerModule;
import icecite.semanticize.plain.modules.ReferenceModule;
import icecite.semanticize.plain.modules.TableModule;
import icecite.semanticize.plain.modules.TitleModule;
import icecite.serialize.JsonPdfSerializer;
import icecite.serialize.PdfSerializer;
import icecite.serialize.TxtPdfSerializer;
import icecite.serialize.XmlPdfSerializer;
import icecite.tokenize.PdfTextTokenizer;
import icecite.tokenize.PdfTextTokenizer.PdfTextTokenizerFactory;
import icecite.tokenize.PlainPdfTextTokenizer;
import icecite.tokenize.areas.PdfTextAreaTokenizer;
import icecite.tokenize.areas.XYCutPdfTextAreaTokenizer;
import icecite.tokenize.blocks.PdfTextBlockTokenizer;
import icecite.tokenize.blocks.PlainPdfTextBlockTokenizer;
import icecite.tokenize.lines.PdfTextLineTokenizer;
import icecite.tokenize.lines.XYCutPdfTextLineTokenizer;
import icecite.tokenize.paragraphs.PdfParagraphTokenizer;
import icecite.tokenize.paragraphs.PdfParagraphTokenizer.PdfParagraphTokenizerFactory;
import icecite.tokenize.paragraphs.PlainPdfParagraphTokenizer;
import icecite.tokenize.paragraphs.dehyphenate.PdfWordDehyphenator;
import icecite.tokenize.paragraphs.dehyphenate.PdfWordDehyphenator.PdfWordDehyphenatorFactory;
import icecite.tokenize.paragraphs.dehyphenate.PlainPdfWordDehyphenator;
import icecite.tokenize.words.PdfWordTokenizer;
import icecite.tokenize.words.XYCutPdfWordTokenizer;
import icecite.utils.geometric.Line;
import icecite.utils.geometric.Line.LineFactory;
import icecite.utils.geometric.Point;
import icecite.utils.geometric.Point.PointFactory;
import icecite.utils.geometric.Rectangle;
import icecite.utils.geometric.Rectangle.RectangleFactory;
import icecite.utils.geometric.plain.PlainLine;
import icecite.utils.geometric.plain.PlainPoint;
import icecite.utils.geometric.plain.PlainRectangle;
import icecite.visualize.PdfDrawer;
import icecite.visualize.PdfDrawerFactory;
import icecite.visualize.PdfVisualizer;
import icecite.visualize.PdfVisualizer.PdfVisualizerFactory;
import icecite.visualize.PlainPdfVisualizer;
import icecite.visualize.pdfbox.PdfBoxDrawer;

// TODO: Find out how to dynamically update bindings (needed for the parser,
// where PdfBox needs extra bindings.

/**
 * A module that defines the basic Guice bindings for Icecite.
 * 
 * @author Claudius Korzen
 */
public class IceciteCoreModule extends com.google.inject.AbstractModule {

  @Override
  protected void configure() {
    // Bind the parsers.
    fc(PdfParser.class, PdfParserFactory.class, PlainPdfParser.class);
    fc(PdfStreamParser.class, PdfStreamParserFactory.class,
        PdfBoxPdfStreamParser.class);

    // Bind the tokenizers.
    fc(PdfTextTokenizer.class, PdfTextTokenizerFactory.class,
        PlainPdfTextTokenizer.class);
    bind(PdfTextAreaTokenizer.class).to(XYCutPdfTextAreaTokenizer.class);
    bind(PdfTextLineTokenizer.class).to(XYCutPdfTextLineTokenizer.class);
    bind(PdfWordTokenizer.class).to(XYCutPdfWordTokenizer.class);
    bind(PdfTextBlockTokenizer.class).to(PlainPdfTextBlockTokenizer.class);
    
    fc(PdfParagraphTokenizer.class, PdfParagraphTokenizerFactory.class, 
        PlainPdfParagraphTokenizer.class);
    fc(PdfWordDehyphenator.class, PdfWordDehyphenatorFactory.class, 
        PlainPdfWordDehyphenator.class);
    
    // Bind the semanticizer.
    fc(PdfTextSemanticizer.class, PdfTextSemanticizerFactory.class,
        PlainPdfTextSemanticizer.class);

    // Bind the PDF model factories.
    fc(PdfDocument.class, PdfDocumentFactory.class, PlainPdfDocument.class);
    fc(PdfPage.class, PdfPageFactory.class, PlainPdfPage.class);

    fc(PdfCharacter.class, PdfCharacterFactory.class, PlainPdfCharacter.class);
    fc(PdfCharacterList.class, PdfCharacterListFactory.class,
        PlainPdfCharacterList.class);

    fc(PdfFigure.class, PdfFigureFactory.class, PlainPdfFigure.class);
    fc(PdfShape.class, PdfShapeFactory.class, PlainPdfShape.class);
    fc(PdfFont.class, PdfFontFactory.class, PlainPdfFont.class);
    fc(PdfFontFace.class, PdfFontFaceFactory.class, PlainPdfFontFace.class);
    fc(PdfColor.class, PdfColorFactory.class, PlainPdfColor.class);

    fc(PdfTextBlock.class, PdfTextBlockFactory.class, PlainPdfTextBlock.class);

    fc(PdfWord.class, PdfWordFactory.class, PlainPdfWord.class);
    fc(PdfWordList.class, PdfWordListFactory.class, PlainPdfWordList.class);

    fc(PdfTextLine.class, PdfTextLineFactory.class, PlainPdfTextLine.class);
    fc(PdfTextLineList.class, PdfTextLineListFactory.class,
        PlainPdfTextLineList.class);

    fc(PdfParagraph.class, PdfParagraphFactory.class, PlainPdfParagraph.class);

    // Bind the geometric model factories.
    fc(PdfPosition.class, PdfPositionFactory.class, PlainPdfPosition.class);
    fc(Rectangle.class, RectangleFactory.class, PlainRectangle.class);
    fc(Line.class, LineFactory.class, PlainLine.class);
    fc(Point.class, PointFactory.class, PlainPoint.class);

    Multibinder<PdfTextSemanticizerModule> binder = Multibinder
        .newSetBinder(binder(), PdfTextSemanticizerModule.class);

    // Bind semantic role testers.
    binder.addBinding().to(TitleModule.class);
    binder.addBinding().to(HeadingModule.class);
    binder.addBinding().to(AbstractModule.class);
    binder.addBinding().to(KeywordsModule.class);
    binder.addBinding().to(CategoriesModule.class);
    binder.addBinding().to(GeneralTermsModule.class);
    binder.addBinding().to(AcknowledgmentsModule.class);
    binder.addBinding().to(ReferenceModule.class);
    binder.addBinding().to(CaptionModule.class);
    binder.addBinding().to(FootnoteModule.class);
    binder.addBinding().to(ItemizeItemModule.class);
    binder.addBinding().to(TableModule.class);
    binder.addBinding().to(BodyTextModule.class);

    MapBinder<String, PdfSerializer> serializerBinder = MapBinder
        .newMapBinder(binder(), String.class, PdfSerializer.class);

    // Bind the serializers.
    serializerBinder.addBinding(TxtPdfSerializer.getOutputFormat())
        .to(TxtPdfSerializer.class);
    serializerBinder.addBinding(XmlPdfSerializer.getOutputFormat())
        .to(XmlPdfSerializer.class);
    serializerBinder.addBinding(JsonPdfSerializer.getOutputFormat())
        .to(JsonPdfSerializer.class);

    // Bind the visualizer.
    fc(PdfDrawer.class, PdfDrawerFactory.class, PdfBoxDrawer.class);
    fc(PdfVisualizer.class, PdfVisualizerFactory.class,
        PlainPdfVisualizer.class);
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
