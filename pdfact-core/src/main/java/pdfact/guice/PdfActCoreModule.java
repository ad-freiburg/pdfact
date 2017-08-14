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
    // Install the factory of the PDF parser.
    install(new FactoryModuleBuilder()
        .implement(PdfParser.class, PlainPdfParser.class)
        .build(PdfParserFactory.class));

    // Install the factory of the PDF stream parser.
    install(new FactoryModuleBuilder()
        .implement(PdfStreamParser.class, PdfBoxPdfStreamParser.class)
        .build(PdfStreamParserFactory.class));

    // Install the factory of the text tokenizer.
    install(new FactoryModuleBuilder()
        .implement(PdfTextTokenizer.class, PlainPdfTextTokenizer.class)
        .build(PdfTextTokenizerFactory.class));

    // Bind the segmenters.
    bind(PdfTextAreaSegmenter.class).to(XYCutPdfTextAreaSegmenter.class);
    bind(PdfTextLineSegmenter.class).to(XYCutPdfTextLineSegmenter.class);
    bind(PdfWordSegmenter.class).to(XYCutPdfWordSegmenter.class);
    bind(PdfParagraphSegmenter.class).to(PlainPdfParagraphSegmenter.class);

    // Bind the tokenizers.
    bind(PdfTextLineTokenizer.class).to(PlainPdfTextLineTokenizer.class);
    bind(PdfWordTokenizer.class).to(PlainPdfWordTokenizer.class);
    bind(PdfTextBlockTokenizer.class).to(PlainPdfTextBlockTokenizer.class);

    // Install the factories of the statistics.
    install(new FactoryModuleBuilder()
        .implement(PdfCharacterStatistic.class,
            PlainPdfCharacterStatistic.class)
        .build(PdfCharacterStatisticFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfTextLineStatistic.class, PlainPdfTextLineStatistics.class)
        .build(PdfTextLineStatisticFactory.class));

    // Bind the statistician.
    bind(PdfCharacterStatistician.class)
        .to(PlainPdfCharacterStatistician.class);
    bind(PdfTextLineStatistician.class).to(PlainPdfTextLineStatistician.class);

    // Install the factory of the paragraph tokenizer.
    install(new FactoryModuleBuilder()
        .implement(PdfParagraphTokenizer.class,
            PlainPdfParagraphTokenizer.class)
        .build(PdfParagraphTokenizerFactory.class));

    // Install the factory of the word dehyphenator.
    install(new FactoryModuleBuilder()
        .implement(PdfWordDehyphenator.class, PlainPdfWordDehyphenator.class)
        .build(PdfWordDehyphenatorFactory.class));

    // Install the factory of the semanticizer.
    install(new FactoryModuleBuilder()
        .implement(PdfTextSemanticizer.class, PlainPdfTextSemanticizer.class)
        .build(PdfTextSemanticizerFactory.class));

    // Bind the modules of the semanticizer.
    Multibinder<PdfTextSemanticizerModule> binder = Multibinder
        .newSetBinder(binder(), PdfTextSemanticizerModule.class);
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

    // Install the factories of the base models.
    install(new FactoryModuleBuilder()
        .implement(PdfDocument.class, PlainPdfDocument.class)
        .build(PdfDocumentFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfPage.class, PlainPdfPage.class)
        .build(PdfPageFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfCharacter.class, PlainPdfCharacter.class)
        .build(PdfCharacterFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfCharacterList.class, PlainPdfCharacterList.class)
        .build(PdfCharacterListFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfFigure.class, PlainPdfFigure.class)
        .build(PdfFigureFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfShape.class, PlainPdfShape.class)
        .build(PdfShapeFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfFont.class, PlainPdfFont.class)
        .build(PdfFontFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfFontFace.class, PlainPdfFontFace.class)
        .build(PdfFontFaceFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfColor.class, PlainPdfColor.class)
        .build(PdfColorFactory.class));

    install(new FactoryModuleBuilder()
        .implement(PdfTextBlock.class, PlainPdfTextBlock.class)
        .build(PdfTextBlockFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfWord.class, PlainPdfWord.class)
        .build(PdfWordFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfWordList.class, PlainPdfWordList.class)
        .build(PdfWordListFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfTextLine.class, PlainPdfTextLine.class)
        .build(PdfTextLineFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfTextLineList.class, PlainPdfTextLineList.class)
        .build(PdfTextLineListFactory.class));
    install(new FactoryModuleBuilder()
        .implement(PdfParagraph.class, PlainPdfParagraph.class)
        .build(PdfParagraphFactory.class));

    // Install the factories of the geometric models.
    install(new FactoryModuleBuilder()
        .implement(PdfPosition.class, PlainPdfPosition.class)
        .build(PdfPositionFactory.class));
    install(new FactoryModuleBuilder()
        .implement(Rectangle.class, PlainRectangle.class)
        .build(RectangleFactory.class));
    install(new FactoryModuleBuilder()
        .implement(Line.class, PlainLine.class)
        .build(LineFactory.class));
    install(new FactoryModuleBuilder()
        .implement(Point.class, PlainPoint.class)
        .build(PointFactory.class));
  }
}
