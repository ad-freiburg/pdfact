package pdfact.core.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;

import pdfact.core.model.Character;
import pdfact.core.model.Character.CharacterFactory;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.CharacterStatistic.CharacterStatisticFactory;
import pdfact.core.model.Color;
import pdfact.core.model.Color.ColorFactory;
import pdfact.core.model.Figure;
import pdfact.core.model.Figure.FigureFactory;
import pdfact.core.model.Font;
import pdfact.core.model.Font.FontFactory;
import pdfact.core.model.FontFace;
import pdfact.core.model.FontFace.FontFaceFactory;
import pdfact.core.model.Line;
import pdfact.core.model.Line.LineFactory;
import pdfact.core.model.Page;
import pdfact.core.model.Page.PageFactory;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Paragraph.ParagraphFactory;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.PdfDocument.PdfDocumentFactory;
import pdfact.core.model.PlainCharacter;
import pdfact.core.model.PlainCharacterStatistic;
import pdfact.core.model.PlainColor;
import pdfact.core.model.PlainFigure;
import pdfact.core.model.PlainFont;
import pdfact.core.model.PlainFontFace;
import pdfact.core.model.PlainLine;
import pdfact.core.model.PlainPage;
import pdfact.core.model.PlainParagraph;
import pdfact.core.model.PlainPdfDocument;
import pdfact.core.model.PlainPoint;
import pdfact.core.model.PlainPosition;
import pdfact.core.model.PlainRectangle;
import pdfact.core.model.PlainShape;
import pdfact.core.model.PlainTextArea;
import pdfact.core.model.PlainTextBlock;
import pdfact.core.model.PlainTextLine;
import pdfact.core.model.PlainTextLineStatistic;
import pdfact.core.model.PlainWord;
import pdfact.core.model.Point;
import pdfact.core.model.Point.PointFactory;
import pdfact.core.model.Position;
import pdfact.core.model.Position.PositionFactory;
import pdfact.core.model.Rectangle;
import pdfact.core.model.Rectangle.RectangleFactory;
import pdfact.core.model.Shape;
import pdfact.core.model.Shape.ShapeFactory;
import pdfact.core.model.TextArea;
import pdfact.core.model.TextArea.TextAreaFactory;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextBlock.TextBlockFactory;
import pdfact.core.model.TextLine;
import pdfact.core.model.TextLine.TextLineFactory;
import pdfact.core.model.TextLineStatistic;
import pdfact.core.model.TextLineStatistic.TextLineStatisticFactory;
import pdfact.core.model.Word;
import pdfact.core.model.Word.WordFactory;
import pdfact.core.pipes.PdfActCorePipe;
import pdfact.core.pipes.PdfActCorePipe.PdfActCorePipeFactory;
import pdfact.core.pipes.PlainPdfActCorePipe;
import pdfact.core.pipes.dehyphenate.DehyphenateWordsPipe;
import pdfact.core.pipes.dehyphenate.DehyphenateWordsPipe.DehyphenateWordsPipeFactory;
import pdfact.core.pipes.dehyphenate.PlainDehyphenateWordsPipe;
import pdfact.core.pipes.filter.characters.FilterCharactersPipe;
import pdfact.core.pipes.filter.characters.FilterCharactersPipe.FilterCharactersPipeFactory;
import pdfact.core.pipes.filter.characters.PlainFilterCharactersPipe;
import pdfact.core.pipes.filter.figures.FilterFiguresPipe;
import pdfact.core.pipes.filter.figures.FilterFiguresPipe.FilterFiguresPipeFactory;
import pdfact.core.pipes.filter.figures.PlainFilterFiguresPipe;
import pdfact.core.pipes.filter.shapes.FilterShapesPipe;
import pdfact.core.pipes.filter.shapes.FilterShapesPipe.FilterShapesPipeFactory;
import pdfact.core.pipes.filter.shapes.PlainFilterShapesPipe;
import pdfact.core.pipes.parse.ParsePdfStreamsPipe;
import pdfact.core.pipes.parse.ParsePdfStreamsPipe.ParsePdfPipeFactory;
import pdfact.core.pipes.parse.PlainParsePdfStreamsPipe;
import pdfact.core.pipes.parse.stream.PdfStreamsParser;
import pdfact.core.pipes.parse.stream.PdfStreamsParser.PdfStreamsParserFactory;
import pdfact.core.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;
import pdfact.core.pipes.parse.stream.pdfbox.guice.PdfBoxOperatorProcessorGuiceModule;
import pdfact.core.pipes.semanticize.DetectSemanticsPipe;
import pdfact.core.pipes.semanticize.DetectSemanticsPipe.DetectSemanticsPipeFactory;
import pdfact.core.pipes.semanticize.PlainDetectSemanticsPipe;
import pdfact.core.pipes.semanticize.modules.AbstractModule;
import pdfact.core.pipes.semanticize.modules.AcknowledgmentsModule;
import pdfact.core.pipes.semanticize.modules.BodyTextModule;
import pdfact.core.pipes.semanticize.modules.CaptionModule;
import pdfact.core.pipes.semanticize.modules.CategoriesModule;
import pdfact.core.pipes.semanticize.modules.FootnoteModule;
import pdfact.core.pipes.semanticize.modules.GeneralTermsModule;
import pdfact.core.pipes.semanticize.modules.HeadingModule;
import pdfact.core.pipes.semanticize.modules.ItemizeItemModule;
import pdfact.core.pipes.semanticize.modules.KeywordsModule;
import pdfact.core.pipes.semanticize.modules.PageHeaderFooterModule;
import pdfact.core.pipes.semanticize.modules.PdfTextSemanticizerModule;
import pdfact.core.pipes.semanticize.modules.ReferenceModule;
import pdfact.core.pipes.semanticize.modules.TableModule;
import pdfact.core.pipes.semanticize.modules.TitleModule;
import pdfact.core.pipes.tokenize.areas.TokenizeToTextAreasPipe;
import pdfact.core.pipes.tokenize.areas.TokenizeToTextAreasPipe.TokenizeToTextAreasPipeFactory;
import pdfact.core.pipes.tokenize.areas.XYCutTokenizeToTextAreasPipe;
import pdfact.core.pipes.tokenize.blocks.PlainTokenizeToTextBlocksPipe;
import pdfact.core.pipes.tokenize.blocks.TokenizeToTextBlocksPipe;
import pdfact.core.pipes.tokenize.blocks.TokenizeToTextBlocksPipe.TokenizeToTextBlocksPipeFactory;
import pdfact.core.pipes.tokenize.lines.PlainTokenizeToTextLinesPipe;
import pdfact.core.pipes.tokenize.lines.TokenizeToTextLinesPipe;
import pdfact.core.pipes.tokenize.lines.TokenizeToTextLinesPipe.TokenizeToTextLinesPipeFactory;
import pdfact.core.pipes.tokenize.paragraphs.PlainTokenizeToParagraphsPipe;
import pdfact.core.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe;
import pdfact.core.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe.TokenizeToParagraphsPipeFactory;
import pdfact.core.pipes.tokenize.words.TokenizeToWordsPipe;
import pdfact.core.pipes.tokenize.words.TokenizeToWordsPipe.TokenizeToWordsPipeFactory;
import pdfact.core.pipes.tokenize.words.XYCutTokenizeToWordsPipe;
import pdfact.core.pipes.translate.characters.PlainStandardizeCharactersPipe;
import pdfact.core.pipes.translate.characters.StandardizeCharactersPipe;
import pdfact.core.pipes.translate.characters.StandardizeCharactersPipe.StandardizeCharactersPipeFactory;
import pdfact.core.pipes.translate.diacritics.MergeDiacriticsPipe;
import pdfact.core.pipes.translate.diacritics.MergeDiacriticsPipe.MergeDiacriticsPipeFactory;
import pdfact.core.pipes.translate.diacritics.PlainMergeDiacriticsPipe;
import pdfact.core.pipes.translate.ligatures.PlainSplitLigaturesPipe;
import pdfact.core.pipes.translate.ligatures.SplitLigaturesPipe;
import pdfact.core.pipes.translate.ligatures.SplitLigaturesPipe.SplitLigaturesPipeFactory;
import pdfact.core.pipes.validate.PlainValidatePdfPathPipe;
import pdfact.core.pipes.validate.ValidatePdfPathPipe;
import pdfact.core.pipes.validate.ValidatePdfPathPipe.ValidatePdfPathPipeFactory;
import pdfact.core.util.counter.FloatCounter;
import pdfact.core.util.counter.FloatCounter.FloatCounterFactory;
import pdfact.core.util.counter.ObjectCounter;
import pdfact.core.util.counter.ObjectCounter.ObjectCounterFactory;
import pdfact.core.util.counter.PlainFloatCounter;
import pdfact.core.util.counter.PlainObjectCounter;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.list.ElementList.ElementListFactory;
import pdfact.core.util.list.PlainElementList;
import pdfact.core.util.log.Log4JTypeListener;
import pdfact.core.util.normalize.PlainWordNormalizer;
import pdfact.core.util.normalize.WordNormalizer;
import pdfact.core.util.normalize.WordNormalizer.WordNormalizerFactory;
import pdfact.core.util.pipeline.Pipeline;
import pdfact.core.util.pipeline.Pipeline.PdfActPipelineFactory;
import pdfact.core.util.pipeline.PlainPipeline;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.statistician.PlainCharacterStatistician;
import pdfact.core.util.statistician.PlainTextLineStatistician;
import pdfact.core.util.statistician.TextLineStatistician;

/**
 * A module that defines the basic Guice bindings for PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCoreGuiceModule extends com.google.inject.AbstractModule {
  @Override
  protected void configure() {
    // Install the factory of the basic pipeline.
    install(new FactoryModuleBuilder()
        .implement(Pipeline.class, PlainPipeline.class)
        .build(PdfActPipelineFactory.class));

    // Bind the listener to inject log4j loggers.
    bindListener(Matchers.any(), new Log4JTypeListener());

    // ========================================================================

    // Install the factory of the pipe that validates the paths of PDF files.
    install(new FactoryModuleBuilder()
        .implement(ValidatePdfPathPipe.class, PlainValidatePdfPathPipe.class)
        .build(ValidatePdfPathPipeFactory.class));

    // Install the factory of the pipe that parses streams of PDF files.
    install(new FactoryModuleBuilder()
        .implement(ParsePdfStreamsPipe.class, PlainParsePdfStreamsPipe.class)
        .build(ParsePdfPipeFactory.class));

    // Install the factory of the pipe that merges characters with diacritics.
    install(new FactoryModuleBuilder()
        .implement(MergeDiacriticsPipe.class, PlainMergeDiacriticsPipe.class)
        .build(MergeDiacriticsPipeFactory.class));

    // Install the factory of the pipe that splits ligatures.
    install(new FactoryModuleBuilder()
        .implement(SplitLigaturesPipe.class, PlainSplitLigaturesPipe.class)
        .build(SplitLigaturesPipeFactory.class));

    // Install the factory of the pipe that standardizes characters.
    install(new FactoryModuleBuilder()
        .implement(StandardizeCharactersPipe.class,
            PlainStandardizeCharactersPipe.class)
        .build(StandardizeCharactersPipeFactory.class));

    // Install the factory of the pipe that filters specific characters.
    install(new FactoryModuleBuilder()
        .implement(FilterCharactersPipe.class, PlainFilterCharactersPipe.class)
        .build(FilterCharactersPipeFactory.class));

    // Install the factory of the pipe that filters specific figures.
    install(new FactoryModuleBuilder()
        .implement(FilterFiguresPipe.class, PlainFilterFiguresPipe.class)
        .build(FilterFiguresPipeFactory.class));

    // Install the factory of the pipe that filters specific shapes.
    install(new FactoryModuleBuilder()
        .implement(FilterShapesPipe.class, PlainFilterShapesPipe.class)
        .build(FilterShapesPipeFactory.class));

    // Install the factory of the pipe that tokenizes pages into text areas.
    install(new FactoryModuleBuilder()
        .implement(TokenizeToTextAreasPipe.class,
            XYCutTokenizeToTextAreasPipe.class)
        .build(TokenizeToTextAreasPipeFactory.class));

    // Install the factory of the pipe that tokenizes text areas into lines.
    install(new FactoryModuleBuilder()
        .implement(TokenizeToTextLinesPipe.class,
            PlainTokenizeToTextLinesPipe.class)
        .build(TokenizeToTextLinesPipeFactory.class));

    // Install the factory of the pipe that tokenizes text lines into words.
    install(new FactoryModuleBuilder()
        .implement(TokenizeToWordsPipe.class, XYCutTokenizeToWordsPipe.class)
        .build(TokenizeToWordsPipeFactory.class));

    // Install the factory of the pipe that tokenizes text lines into blocks.
    install(new FactoryModuleBuilder()
        .implement(TokenizeToTextBlocksPipe.class,
            PlainTokenizeToTextBlocksPipe.class)
        .build(TokenizeToTextBlocksPipeFactory.class));

    // Install the factory of the pipe that detects the semantics of blocks.
    install(new FactoryModuleBuilder()
        .implement(DetectSemanticsPipe.class, PlainDetectSemanticsPipe.class)
        .build(DetectSemanticsPipeFactory.class));

    // Install the factory of the pipe that tokenizes blocks into paragraphs.
    install(new FactoryModuleBuilder()
        .implement(TokenizeToParagraphsPipe.class,
            PlainTokenizeToParagraphsPipe.class)
        .build(TokenizeToParagraphsPipeFactory.class));

    // Install the factory of the pipe that dehyphenates words.
    install(new FactoryModuleBuilder()
        .implement(DehyphenateWordsPipe.class, PlainDehyphenateWordsPipe.class)
        .build(DehyphenateWordsPipeFactory.class));

    // Install the factory of the core pipe (combining all core module pipes).
    install(new FactoryModuleBuilder()
        .implement(PdfActCorePipe.class, PlainPdfActCorePipe.class)
        .build(PdfActCorePipeFactory.class));

    // ========================================================================
    // Install the factories of the base models.

    // Install the factory to build a PDF document.
    install(new FactoryModuleBuilder()
        .implement(PdfDocument.class, PlainPdfDocument.class)
        .build(PdfDocumentFactory.class));

    // Install the factory to build a page of a PDF document.
    install(new FactoryModuleBuilder()
        .implement(Page.class, PlainPage.class)
        .build(PageFactory.class));

    // Install the factory to build a text character.
    install(new FactoryModuleBuilder()
        .implement(Character.class, PlainCharacter.class)
        .build(CharacterFactory.class));

    // Install the factory to build a list of text characters.
    // install(new FactoryModuleBuilder()
    // .implement(CharacterList.class, PlainCharacterList.class)
    // .build(CharacterListFactory.class));

    // Install the factory to build a figure.
    install(new FactoryModuleBuilder()
        .implement(Figure.class, PlainFigure.class)
        .build(FigureFactory.class));

    // Install the factory to build a shape.
    install(new FactoryModuleBuilder()
        .implement(Shape.class, PlainShape.class)
        .build(ShapeFactory.class));

    // Install the factory to build a font.
    install(new FactoryModuleBuilder()
        .implement(Font.class, PlainFont.class)
        .build(FontFactory.class));

    // Install the factory to build a font face (pair of font & font size).
    install(new FactoryModuleBuilder()
        .implement(FontFace.class, PlainFontFace.class)
        .build(FontFaceFactory.class));

    // Install the factory to build a color.
    install(new FactoryModuleBuilder()
        .implement(Color.class, PlainColor.class)
        .build(ColorFactory.class));

    // Install the factory to build a (geometric) rectangle.
    install(new FactoryModuleBuilder()
        .implement(Rectangle.class, PlainRectangle.class)
        .build(RectangleFactory.class));

    // Install the factory to build a (geometric) line.
    install(new FactoryModuleBuilder()
        .implement(Line.class, PlainLine.class)
        .build(LineFactory.class));

    // Install the factory to build a (geometric) point.
    install(new FactoryModuleBuilder()
        .implement(Point.class, PlainPoint.class)
        .build(PointFactory.class));

    // Install the factory to build a PDF position (pair of page & rectangle).
    install(new FactoryModuleBuilder()
        .implement(Position.class, PlainPosition.class)
        .build(PositionFactory.class));

    // Install the factory to build a text area (a loose collection of chars).
    install(new FactoryModuleBuilder()
        .implement(TextArea.class, PlainTextArea.class)
        .build(TextAreaFactory.class));

    // Install the factory to build a text line.
    install(new FactoryModuleBuilder()
        .implement(TextLine.class, PlainTextLine.class)
        .build(TextLineFactory.class));

    // Install the factory to build a list of text lines.
    // install(new FactoryModuleBuilder()
    // .implement(TextLineList.class, PlainTextLineList.class)
    // .build(TextLineListFactory.class));

    // Install the factory to build a word.
    install(new FactoryModuleBuilder()
        .implement(Word.class, PlainWord.class)
        .build(WordFactory.class));

    // Install the factory to build a list of words.
    // install(new FactoryModuleBuilder()
    // .implement(WordList.class, PlainWordList.class)
    // .build(WordListFactory.class));

    // Install the factory to build a text block (= "paragraph" per page).
    install(new FactoryModuleBuilder()
        .implement(TextBlock.class, PlainTextBlock.class)
        .build(TextBlockFactory.class));

    // Install the factory to build a paragraph.
    install(new FactoryModuleBuilder()
        .implement(Paragraph.class, PlainParagraph.class)
        .build(ParagraphFactory.class));

    // Install the factory to build a statistic about characters.
    install(new FactoryModuleBuilder()
        .implement(CharacterStatistic.class, PlainCharacterStatistic.class)
        .build(CharacterStatisticFactory.class));

    // Install the factory to build a statistics about text lines.
    install(new FactoryModuleBuilder()
        .implement(TextLineStatistic.class, PlainTextLineStatistic.class)
        .build(TextLineStatisticFactory.class));

    // Install the statisticians (computing the statistics).
    bind(CharacterStatistician.class).to(PlainCharacterStatistician.class);
    bind(TextLineStatistician.class).to(PlainTextLineStatistician.class);

    // ========================================================================

    // Install the factory of the float counter.
    install(new FactoryModuleBuilder()
        .implement(FloatCounter.class, PlainFloatCounter.class)
        .build(FloatCounterFactory.class));

    // Install the factory of the object counter to count strings.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ObjectCounter<String>>() {},
            new TypeLiteral<PlainObjectCounter<String>>() {})
        .build(new TypeLiteral<ObjectCounterFactory<String>>() {}));

    // Install the factory of the object counter to count colors.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ObjectCounter<Color>>() {},
            new TypeLiteral<PlainObjectCounter<Color>>() {})
        .build(new TypeLiteral<ObjectCounterFactory<Color>>() {}));

    // Install the factory of the object counter to count colors.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ObjectCounter<FontFace>>() {},
            new TypeLiteral<PlainObjectCounter<FontFace>>() {})
        .build(new TypeLiteral<ObjectCounterFactory<FontFace>>() {}));

    // ========================================================================
    // Install the factories of element lists.

    // Install the factory to create lists of characters.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<Character>>() {},
            new TypeLiteral<PlainElementList<Character>>() {})
        .build(new TypeLiteral<ElementListFactory<Character>>() {}));

    // Install the factory to create lists of figures.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<Figure>>() {},
            new TypeLiteral<PlainElementList<Figure>>() {})
        .build(new TypeLiteral<ElementListFactory<Figure>>() {}));

    // Install the factory to create lists of shapes.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<Shape>>() {},
            new TypeLiteral<PlainElementList<Shape>>() {})
        .build(new TypeLiteral<ElementListFactory<Shape>>() {}));

    // Install the factory to create lists of words.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<Word>>() {},
            new TypeLiteral<PlainElementList<Word>>() {})
        .build(new TypeLiteral<ElementListFactory<Word>>() {}));

    // Install the factory to create lists of text lines.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<TextLine>>() {},
            new TypeLiteral<PlainElementList<TextLine>>() {})
        .build(new TypeLiteral<ElementListFactory<TextLine>>() {}));

    // Install the factory to create lists of text areas.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<TextArea>>() {},
            new TypeLiteral<PlainElementList<TextArea>>() {})
        .build(new TypeLiteral<ElementListFactory<TextArea>>() {}));

    // Install the factory to create lists of text blocks.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<TextBlock>>() {},
            new TypeLiteral<PlainElementList<TextBlock>>() {})
        .build(new TypeLiteral<ElementListFactory<TextBlock>>() {}));

    // Install the factory to create lists of paragraphs.
    install(new FactoryModuleBuilder()
        .implement(new TypeLiteral<ElementList<Paragraph>>() {},
            new TypeLiteral<PlainElementList<Paragraph>>() {})
        .build(new TypeLiteral<ElementListFactory<Paragraph>>() {}));

    // ========================================================================

    // Install the module that gives the PDF operator modules bindings.
    install(new PdfBoxOperatorProcessorGuiceModule());

    // Install the factory of the PDF stream parser.
    install(new FactoryModuleBuilder()
        .implement(PdfStreamsParser.class, PdfBoxPdfStreamsParser.class)
        .build(PdfStreamsParserFactory.class));

    // ========================================================================

    // Install the factory of the word normalizer.
    install(new FactoryModuleBuilder()
        .implement(WordNormalizer.class, PlainWordNormalizer.class)
        .build(WordNormalizerFactory.class));

    // ========================================================================
    // Install stuff needed for the pipe that semanticizes text blocks.

    // Install the semanticizer modules.
    Multibinder<PdfTextSemanticizerModule> binder =
        Multibinder.newSetBinder(binder(), PdfTextSemanticizerModule.class);
    binder.addBinding().to(TitleModule.class);
    binder.addBinding().to(PageHeaderFooterModule.class);
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
}
