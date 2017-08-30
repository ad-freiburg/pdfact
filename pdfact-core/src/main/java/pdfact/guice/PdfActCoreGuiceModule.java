package pdfact.guice;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

import pdfact.model.Character;
import pdfact.model.Character.CharacterFactory;
import pdfact.model.CharacterStatistic;
import pdfact.model.CharacterStatistic.CharacterStatisticFactory;
import pdfact.model.Color;
import pdfact.model.Color.ColorFactory;
import pdfact.model.Figure;
import pdfact.model.Figure.FigureFactory;
import pdfact.model.Font;
import pdfact.model.Font.FontFactory;
import pdfact.model.FontFace;
import pdfact.model.FontFace.FontFaceFactory;
import pdfact.model.Line;
import pdfact.model.Line.LineFactory;
import pdfact.model.Page;
import pdfact.model.Page.PageFactory;
import pdfact.model.Paragraph;
import pdfact.model.Paragraph.ParagraphFactory;
import pdfact.model.PdfDocument;
import pdfact.model.PdfDocument.PdfDocumentFactory;
import pdfact.model.Point;
import pdfact.model.Point.PointFactory;
import pdfact.model.Position;
import pdfact.model.Position.PositionFactory;
import pdfact.model.Rectangle;
import pdfact.model.Rectangle.RectangleFactory;
import pdfact.model.Shape;
import pdfact.model.Shape.ShapeFactory;
import pdfact.model.TextArea;
import pdfact.model.TextArea.TextAreaFactory;
import pdfact.model.TextBlock;
import pdfact.model.TextBlock.TextBlockFactory;
import pdfact.model.TextLine;
import pdfact.model.TextLine.TextLineFactory;
import pdfact.model.TextLineStatistic;
import pdfact.model.TextLineStatistic.TextLineStatisticFactory;
import pdfact.model.Word;
import pdfact.model.Word.WordFactory;
import pdfact.model.plain.PlainCharacter;
import pdfact.model.plain.PlainCharacterStatistic;
import pdfact.model.plain.PlainColor;
import pdfact.model.plain.PlainFigure;
import pdfact.model.plain.PlainFont;
import pdfact.model.plain.PlainFontFace;
import pdfact.model.plain.PlainLine;
import pdfact.model.plain.PlainPage;
import pdfact.model.plain.PlainParagraph;
import pdfact.model.plain.PlainPdfDocument;
import pdfact.model.plain.PlainPipeline;
import pdfact.model.plain.PlainPoint;
import pdfact.model.plain.PlainPosition;
import pdfact.model.plain.PlainRectangle;
import pdfact.model.plain.PlainShape;
import pdfact.model.plain.PlainTextArea;
import pdfact.model.plain.PlainTextBlock;
import pdfact.model.plain.PlainTextLine;
import pdfact.model.plain.PlainTextLineStatistic;
import pdfact.model.plain.PlainWord;
import pdfact.pipes.PdfActCorePipe;
import pdfact.pipes.PdfActCorePipe.PdfActCorePipeFactory;
import pdfact.pipes.PlainPdfActCorePipe;
import pdfact.pipes.dehyphenate.DehyphenateWordsPipe;
import pdfact.pipes.dehyphenate.DehyphenateWordsPipe.DehyphenateWordsPipeFactory;
import pdfact.pipes.dehyphenate.PlainDehyphenateWordsPipe;
import pdfact.pipes.filter.characters.FilterCharactersPipe;
import pdfact.pipes.filter.characters.FilterCharactersPipe.FilterCharactersPipeFactory;
import pdfact.pipes.filter.characters.PlainFilterCharactersPipe;
import pdfact.pipes.filter.figures.FilterFiguresPipe;
import pdfact.pipes.filter.figures.FilterFiguresPipe.FilterFiguresPipeFactory;
import pdfact.pipes.filter.figures.PlainFilterFiguresPipe;
import pdfact.pipes.filter.shapes.FilterShapesPipe;
import pdfact.pipes.filter.shapes.FilterShapesPipe.FilterShapesPipeFactory;
import pdfact.pipes.filter.shapes.PlainFilterShapesPipe;
import pdfact.pipes.parse.ParsePdfStreamsPipe;
import pdfact.pipes.parse.ParsePdfStreamsPipe.ParsePdfPipeFactory;
import pdfact.pipes.parse.PlainParsePdfStreamsPipe;
import pdfact.pipes.parse.stream.PdfStreamsParser;
import pdfact.pipes.parse.stream.PdfStreamsParser.PdfStreamsParserFactory;
import pdfact.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;
import pdfact.pipes.parse.stream.pdfbox.guice.PdfBoxOperatorProcessorGuiceModule;
import pdfact.pipes.semanticize.DetectSemanticsPipe;
import pdfact.pipes.semanticize.DetectSemanticsPipe.DetectSemanticsPipeFactory;
import pdfact.pipes.semanticize.PlainDetectSemanticsPipe;
import pdfact.pipes.semanticize.modules.AbstractModule;
import pdfact.pipes.semanticize.modules.AcknowledgmentsModule;
import pdfact.pipes.semanticize.modules.BodyTextModule;
import pdfact.pipes.semanticize.modules.CaptionModule;
import pdfact.pipes.semanticize.modules.CategoriesModule;
import pdfact.pipes.semanticize.modules.FootnoteModule;
import pdfact.pipes.semanticize.modules.GeneralTermsModule;
import pdfact.pipes.semanticize.modules.HeadingModule;
import pdfact.pipes.semanticize.modules.ItemizeItemModule;
import pdfact.pipes.semanticize.modules.KeywordsModule;
import pdfact.pipes.semanticize.modules.PdfTextSemanticizerModule;
import pdfact.pipes.semanticize.modules.ReferenceModule;
import pdfact.pipes.semanticize.modules.TableModule;
import pdfact.pipes.semanticize.modules.TitleModule;
import pdfact.pipes.tokenize.areas.TokenizeToTextAreasPipe;
import pdfact.pipes.tokenize.areas.TokenizeToTextAreasPipe.TokenizeToTextAreasPipeFactory;
import pdfact.pipes.tokenize.areas.XYCutTokenizeToTextAreasPipe;
import pdfact.pipes.tokenize.blocks.PlainTokenizeToTextBlocksPipe;
import pdfact.pipes.tokenize.blocks.TokenizeToTextBlocksPipe;
import pdfact.pipes.tokenize.blocks.TokenizeToTextBlocksPipe.TokenizeToTextBlocksPipeFactory;
import pdfact.pipes.tokenize.lines.PlainTokenizeToTextLinesPipe;
import pdfact.pipes.tokenize.lines.TokenizeToTextLinesPipe;
import pdfact.pipes.tokenize.lines.TokenizeToTextLinesPipe.TokenizeToTextLinesPipeFactory;
import pdfact.pipes.tokenize.paragraphs.PlainTokenizeToParagraphsPipe;
import pdfact.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe;
import pdfact.pipes.tokenize.paragraphs.TokenizeToParagraphsPipe.TokenizeToParagraphsPipeFactory;
import pdfact.pipes.tokenize.words.TokenizeToWordsPipe;
import pdfact.pipes.tokenize.words.TokenizeToWordsPipe.TokenizeToWordsPipeFactory;
import pdfact.pipes.tokenize.words.XYCutTokenizeToWordsPipe;
import pdfact.pipes.translate.diacritics.MergeDiacriticsPipe;
import pdfact.pipes.translate.diacritics.MergeDiacriticsPipe.MergeDiacriticsPipeFactory;
import pdfact.pipes.translate.diacritics.PlainMergeDiacriticsPipe;
import pdfact.pipes.translate.ligatures.PlainSplitLigaturesPipe;
import pdfact.pipes.translate.ligatures.SplitLigaturesPipe;
import pdfact.pipes.translate.ligatures.SplitLigaturesPipe.SplitLigaturesPipeFactory;
import pdfact.pipes.validate.PlainValidatePdfPathPipe;
import pdfact.pipes.validate.ValidatePdfPathPipe;
import pdfact.pipes.validate.ValidatePdfPathPipe.ValidatePdfPathPipeFactory;
import pdfact.util.list.CharacterList;
import pdfact.util.list.CharacterList.CharacterListFactory;
import pdfact.util.list.TextLineList;
import pdfact.util.list.TextLineList.TextLineListFactory;
import pdfact.util.list.WordList;
import pdfact.util.list.WordList.WordListFactory;
import pdfact.util.list.plain.PlainCharacterList;
import pdfact.util.list.plain.PlainTextLineList;
import pdfact.util.list.plain.PlainWordList;
import pdfact.util.pipeline.Pipeline;
import pdfact.util.pipeline.Pipeline.PdfActPipelineFactory;
import pdfact.util.statistic.CharacterStatistician;
import pdfact.util.statistic.TextLineStatistician;
import pdfact.util.statistic.plain.PlainCharacterStatistician;
import pdfact.util.statistic.plain.PlainTextLineStatistician;

/**
 * A module that defines the basic Guice bindings of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCoreGuiceModule extends com.google.inject.AbstractModule {
  @Override
  protected void configure() {
    // Install the factory of the pipeline.
    install(new FactoryModuleBuilder()
        .implement(Pipeline.class, PlainPipeline.class)
        .build(PdfActPipelineFactory.class));

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

    // Install the factory of the pipe that filters characters.
    install(new FactoryModuleBuilder()
        .implement(FilterCharactersPipe.class, PlainFilterCharactersPipe.class)
        .build(FilterCharactersPipeFactory.class));

    // Install the factory of the pipe that filters figures.
    install(new FactoryModuleBuilder()
        .implement(FilterFiguresPipe.class, PlainFilterFiguresPipe.class)
        .build(FilterFiguresPipeFactory.class));

    // Install the factory of the pipe that filters shapes.
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

    // Install the factory of the pipe that detects semantics of text blocks.
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

    // Install the factory of the pipe that combines all core module pipes.
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
    install(new FactoryModuleBuilder()
        .implement(CharacterList.class, PlainCharacterList.class)
        .build(CharacterListFactory.class));

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
    install(new FactoryModuleBuilder()
        .implement(TextLineList.class, PlainTextLineList.class)
        .build(TextLineListFactory.class));

    // Install the factory to build a word.
    install(new FactoryModuleBuilder()
        .implement(Word.class, PlainWord.class)
        .build(WordFactory.class));

    // Install the factory to build a list of words.
    install(new FactoryModuleBuilder()
        .implement(WordList.class, PlainWordList.class)
        .build(WordListFactory.class));

    // Install the factory to build a text block (= "paragraph" per page).
    install(new FactoryModuleBuilder()
        .implement(TextBlock.class, PlainTextBlock.class)
        .build(TextBlockFactory.class));

    // Install the factory to build a paragraph.
    install(new FactoryModuleBuilder()
        .implement(Paragraph.class, PlainParagraph.class)
        .build(ParagraphFactory.class));

    // Install the factory to build a character statistic.
    install(new FactoryModuleBuilder()
        .implement(CharacterStatistic.class, PlainCharacterStatistic.class)
        .build(CharacterStatisticFactory.class));

    // Install the factory to build a text line statistic.
    install(new FactoryModuleBuilder()
        .implement(TextLineStatistic.class, PlainTextLineStatistic.class)
        .build(TextLineStatisticFactory.class));

    // Install the statisticians (that compute the statistics).
    bind(CharacterStatistician.class).to(PlainCharacterStatistician.class);
    bind(TextLineStatistician.class).to(PlainTextLineStatistician.class);

    // ========================================================================
    // Install stuff needed for the pipe that extracts characters from PDFs.

    // Install the module that gives the PDF operator modules bindings.
    install(new PdfBoxOperatorProcessorGuiceModule());

    // Install the factory of the PDF stream parser.
    install(new FactoryModuleBuilder()
        .implement(PdfStreamsParser.class, PdfBoxPdfStreamsParser.class)
        .build(PdfStreamsParserFactory.class));

    // ========================================================================
    // Install stuff needed for the pipe that semanticizes text blocks.

    // Install the semanticizer modules.
    Multibinder<PdfTextSemanticizerModule> binder =
        Multibinder.newSetBinder(binder(), PdfTextSemanticizerModule.class);
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
}
