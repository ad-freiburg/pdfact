package pdfact.guice;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

import pdfact.models.PdfCharacter;
import pdfact.models.PdfCharacter.PdfCharacterFactory;
import pdfact.models.PdfCharacterList;
import pdfact.models.PdfCharacterList.PdfCharacterListFactory;
import pdfact.models.PdfCharacterStatistic;
import pdfact.models.PdfCharacterStatistic.PdfCharacterStatisticFactory;
import pdfact.models.PdfCharacterStatistician;
import pdfact.models.PdfColor;
import pdfact.models.PdfColor.PdfColorFactory;
import pdfact.models.PdfDocument;
import pdfact.models.PdfDocument.PdfDocumentFactory;
import pdfact.models.PdfFigure;
import pdfact.models.PdfFigure.PdfFigureFactory;
import pdfact.models.PdfFont;
import pdfact.models.PdfFont.PdfFontFactory;
import pdfact.models.PdfFontFace;
import pdfact.models.PdfFontFace.PdfFontFaceFactory;
import pdfact.models.PdfPage;
import pdfact.models.PdfPage.PdfPageFactory;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfParagraph.PdfParagraphFactory;
import pdfact.models.PdfPosition;
import pdfact.models.PdfPosition.PdfPositionFactory;
import pdfact.models.PdfShape;
import pdfact.models.PdfShape.PdfShapeFactory;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextBlock.PdfTextBlockFactory;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfTextLine.PdfTextLineFactory;
import pdfact.models.PdfTextLineList;
import pdfact.models.PdfTextLineList.PdfTextLineListFactory;
import pdfact.models.PdfTextLineStatistic;
import pdfact.models.PdfTextLineStatistic.PdfTextLineStatisticFactory;
import pdfact.models.PdfTextLineStatistician;
import pdfact.models.PdfWord;
import pdfact.models.PdfWord.PdfWordFactory;
import pdfact.models.PdfWordList;
import pdfact.models.PdfWordList.PdfWordListFactory;
import pdfact.models.plain.PlainPdfCharacter;
import pdfact.models.plain.PlainPdfCharacterList;
import pdfact.models.plain.PlainPdfCharacterStatistic;
import pdfact.models.plain.PlainPdfCharacterStatistician;
import pdfact.models.plain.PlainPdfColor;
import pdfact.models.plain.PlainPdfDocument;
import pdfact.models.plain.PlainPdfFigure;
import pdfact.models.plain.PlainPdfFont;
import pdfact.models.plain.PlainPdfFontFace;
import pdfact.models.plain.PlainPdfPage;
import pdfact.models.plain.PlainPdfParagraph;
import pdfact.models.plain.PlainPdfPosition;
import pdfact.models.plain.PlainPdfShape;
import pdfact.models.plain.PlainPdfTextBlock;
import pdfact.models.plain.PlainPdfTextLine;
import pdfact.models.plain.PlainPdfTextLineList;
import pdfact.models.plain.PlainPdfTextLineStatistician;
import pdfact.models.plain.PlainPdfTextLineStatistics;
import pdfact.models.plain.PlainPdfWord;
import pdfact.models.plain.PlainPdfWordList;
import pdfact.parse.PdfParser;
import pdfact.parse.PdfParser.PdfParserFactory;
import pdfact.parse.PlainPdfParser;
import pdfact.parse.stream.PdfStreamParser;
import pdfact.parse.stream.PdfStreamParser.PdfStreamParserFactory;
import pdfact.parse.stream.pdfbox.PdfBoxPdfStreamParser;
import pdfact.semanticize.PdfTextSemanticizer;
import pdfact.semanticize.PdfTextSemanticizer.PdfTextSemanticizerFactory;
import pdfact.semanticize.plain.PlainPdfTextSemanticizer;
import pdfact.semanticize.plain.modules.AbstractModule;
import pdfact.semanticize.plain.modules.AcknowledgmentsModule;
import pdfact.semanticize.plain.modules.BodyTextModule;
import pdfact.semanticize.plain.modules.CaptionModule;
import pdfact.semanticize.plain.modules.CategoriesModule;
import pdfact.semanticize.plain.modules.FootnoteModule;
import pdfact.semanticize.plain.modules.GeneralTermsModule;
import pdfact.semanticize.plain.modules.HeadingModule;
import pdfact.semanticize.plain.modules.ItemizeItemModule;
import pdfact.semanticize.plain.modules.KeywordsModule;
import pdfact.semanticize.plain.modules.PdfTextSemanticizerModule;
import pdfact.semanticize.plain.modules.ReferenceModule;
import pdfact.semanticize.plain.modules.TableModule;
import pdfact.semanticize.plain.modules.TitleModule;
import pdfact.tokenize.PdfTextTokenizer;
import pdfact.tokenize.PdfTextTokenizer.PdfTextTokenizerFactory;
import pdfact.tokenize.PlainPdfTextTokenizer;
import pdfact.tokenize.areas.PdfTextAreaSegmenter;
import pdfact.tokenize.areas.XYCutPdfTextAreaSegmenter;
import pdfact.tokenize.blocks.PdfTextBlockTokenizer;
import pdfact.tokenize.blocks.PlainPdfTextBlockTokenizer;
import pdfact.tokenize.lines.PdfTextLineSegmenter;
import pdfact.tokenize.lines.PdfTextLineTokenizer;
import pdfact.tokenize.lines.PlainPdfTextLineTokenizer;
import pdfact.tokenize.lines.XYCutPdfTextLineSegmenter;
import pdfact.tokenize.paragraphs.PdfParagraphSegmenter;
import pdfact.tokenize.paragraphs.PdfParagraphTokenizer;
import pdfact.tokenize.paragraphs.PdfParagraphTokenizer.PdfParagraphTokenizerFactory;
import pdfact.tokenize.paragraphs.PlainPdfParagraphSegmenter;
import pdfact.tokenize.paragraphs.PlainPdfParagraphTokenizer;
import pdfact.tokenize.paragraphs.dehyphenate.PdfWordDehyphenator;
import pdfact.tokenize.paragraphs.dehyphenate.PdfWordDehyphenator.PdfWordDehyphenatorFactory;
import pdfact.tokenize.paragraphs.dehyphenate.PlainPdfWordDehyphenator;
import pdfact.tokenize.words.PdfWordSegmenter;
import pdfact.tokenize.words.PdfWordTokenizer;
import pdfact.tokenize.words.PlainPdfWordTokenizer;
import pdfact.tokenize.words.XYCutPdfWordSegmenter;
import pdfact.utils.geometric.Line;
import pdfact.utils.geometric.Line.LineFactory;
import pdfact.utils.geometric.Point;
import pdfact.utils.geometric.Point.PointFactory;
import pdfact.utils.geometric.Rectangle;
import pdfact.utils.geometric.Rectangle.RectangleFactory;
import pdfact.utils.geometric.plain.PlainLine;
import pdfact.utils.geometric.plain.PlainPoint;
import pdfact.utils.geometric.plain.PlainRectangle;

// TODO: Find out how to dynamically update bindings (needed for the parser,
// where PdfBox needs extra bindings.

// TODO: Clean up the Guice module.

/**
 * A module that defines the basic Guice bindings for PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCoreModule extends com.google.inject.AbstractModule {

  @Override
  protected void configure() {
    // Bind the parsers.
    fc(PdfParser.class, PdfParserFactory.class, PlainPdfParser.class);
    fc(PdfStreamParser.class, PdfStreamParserFactory.class,
        PdfBoxPdfStreamParser.class);

    // Bind the tokenizers.
    fc(PdfTextTokenizer.class, PdfTextTokenizerFactory.class,
        PlainPdfTextTokenizer.class);
    bind(PdfTextAreaSegmenter.class).to(XYCutPdfTextAreaSegmenter.class);
    bind(PdfTextLineSegmenter.class).to(XYCutPdfTextLineSegmenter.class);
    bind(PdfTextLineTokenizer.class).to(PlainPdfTextLineTokenizer.class);
    bind(PdfWordSegmenter.class).to(XYCutPdfWordSegmenter.class);
    bind(PdfWordTokenizer.class).to(PlainPdfWordTokenizer.class);
    bind(PdfTextBlockTokenizer.class).to(PlainPdfTextBlockTokenizer.class);

    fc(PdfCharacterStatistic.class, PdfCharacterStatisticFactory.class,
        PlainPdfCharacterStatistic.class);
    fc(PdfTextLineStatistic.class, PdfTextLineStatisticFactory.class,
        PlainPdfTextLineStatistics.class);

    bind(PdfParagraphSegmenter.class).to(PlainPdfParagraphSegmenter.class);
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

    bind(PdfCharacterStatistician.class)
        .to(PlainPdfCharacterStatistician.class);
    bind(PdfTextLineStatistician.class).to(PlainPdfTextLineStatistician.class);

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
