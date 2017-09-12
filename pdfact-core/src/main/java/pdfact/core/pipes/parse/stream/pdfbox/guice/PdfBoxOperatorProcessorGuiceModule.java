package pdfact.core.pipes.parse.stream.pdfbox.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import pdfact.core.pipes.parse.stream.pdfbox.convert.PDColorConverter;
import pdfact.core.pipes.parse.stream.pdfbox.convert.PDFontConverter;
import pdfact.core.pipes.parse.stream.pdfbox.convert.PDFontFaceConverter;
import pdfact.core.pipes.parse.stream.pdfbox.operators.OperatorProcessor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingColorN;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingColorSpace;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceCMYKColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceGrayColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceRGBColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingColorN;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingColorSpace;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingDeviceCMYKColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingDeviceGrayColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.color.SetStrokingDeviceRGBColor;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.AppendRectangleToPath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.BeginInlineImage;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ClipEvenOddRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ClipNonZeroRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ClosePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.CurveTo;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.CurveToReplicateFinalPoint;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.CurveToReplicateInitialPoint;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.EndPath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillEvenOddAndStrokePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillEvenOddRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillNonZeroAndStrokePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.FillNonZeroRule;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.Invoke;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.LineTo;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.ModifyCurrentTransformationMatrix;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.MoveTo;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.RestoreGraphicsState;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.SaveGraphicsState;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.SetGraphicsStateParameters;
import pdfact.core.pipes.parse.stream.pdfbox.operators.graphic.StrokePath;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.BeginText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.EndText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveTextSetLeading;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveToNextLineAndShowText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.MoveToNextLineAndShowTextWithSpacing;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetCharacterSpacing;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetFontAndSize;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextHorizontalScaling;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextLeading;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextMatrix;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextRenderingMode;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetTextRise;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetType3GlyphWidthAndBoundingBox;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.SetWordSpacing;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.ShowText;
import pdfact.core.pipes.parse.stream.pdfbox.operators.text.ShowTextWithIndividualGlyphPositioning;
import pdfact.core.pipes.parse.stream.pdfbox.utils.PdfBoxGlyphUtils;

/**
 * A module that defines the Guice bindings to the operator processors.
 * 
 * @author Claudius Korzen
 */
public class PdfBoxOperatorProcessorGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    // ========================================================================
    // Install the converters.

    bind(PDFontFaceConverter.class).in(Singleton.class);
    bind(PDFontConverter.class).in(Singleton.class);
    bind(PDColorConverter.class).in(Singleton.class);

    // ========================================================================
    // Install the utils.

    bind(PdfBoxGlyphUtils.class).in(Singleton.class);

    // ========================================================================
    // Install the PDF operator modules.

    Multibinder<OperatorProcessor> binder = Multibinder.newSetBinder(binder(),
        OperatorProcessor.class);

    // Install the text operator modules.
    binder.addBinding().to(BeginText.class); // BT
    binder.addBinding().to(EndText.class); // ET
    binder.addBinding().to(MoveText.class); // Td
    binder.addBinding().to(MoveTextSetLeading.class); // TD
    binder.addBinding().to(MoveToNextLineAndShowText.class); // '
    binder.addBinding().to(MoveToNextLineAndShowTextWithSpacing.class); // "
    binder.addBinding().to(SetCharacterSpacing.class); // Tc
    binder.addBinding().to(SetFontAndSize.class); // Tf
    binder.addBinding().to(MoveToNextLineAndShowText.class); // T*
    binder.addBinding().to(SetTextHorizontalScaling.class); // Tz
    binder.addBinding().to(SetTextLeading.class); // TL
    binder.addBinding().to(SetTextMatrix.class); // Tm
    binder.addBinding().to(SetTextRenderingMode.class); // Tr
    binder.addBinding().to(SetTextRise.class); // Ts
    binder.addBinding().to(SetType3GlyphWidthAndBoundingBox.class); // d1
    binder.addBinding().to(SetWordSpacing.class); // Tw
    binder.addBinding().to(ShowText.class); // Tj
    binder.addBinding().to(ShowTextWithIndividualGlyphPositioning.class); // TJ

    // Install the graphics operator modules.
    binder.addBinding().to(AppendRectangleToPath.class); // re
    binder.addBinding().to(BeginInlineImage.class); // BI
    binder.addBinding().to(ClipEvenOddRule.class); // W*
    binder.addBinding().to(ClipNonZeroRule.class); // W
    binder.addBinding().to(ClosePath.class); // h
    binder.addBinding().to(CurveTo.class); // c
    binder.addBinding().to(CurveToReplicateFinalPoint.class); // y
    binder.addBinding().to(CurveToReplicateInitialPoint.class); // v
    binder.addBinding().to(EndPath.class); // n
    binder.addBinding().to(FillEvenOddAndStrokePath.class); // B*
    binder.addBinding().to(FillEvenOddRule.class); // f*
    binder.addBinding().to(FillNonZeroAndStrokePath.class); // B
    binder.addBinding().to(FillNonZeroRule.class); // f
    binder.addBinding().to(Invoke.class); // Do
    binder.addBinding().to(LineTo.class); // l
    binder.addBinding().to(ModifyCurrentTransformationMatrix.class); // cm
    binder.addBinding().to(MoveTo.class); // m
    binder.addBinding().to(RestoreGraphicsState.class); // Q
    binder.addBinding().to(SaveGraphicsState.class); // q
    binder.addBinding().to(SetGraphicsStateParameters.class); // gs
    binder.addBinding().to(StrokePath.class); // S

    // Install the color operator modules.
    binder.addBinding().to(SetNonStrokingColor.class); // sc
    binder.addBinding().to(SetNonStrokingColorN.class); // scn
    binder.addBinding().to(SetNonStrokingColorSpace.class); // cs
    binder.addBinding().to(SetNonStrokingDeviceCMYKColor.class); // k
    binder.addBinding().to(SetNonStrokingDeviceGrayColor.class); // g
    binder.addBinding().to(SetNonStrokingDeviceRGBColor.class); // rg
    binder.addBinding().to(SetStrokingColor.class); // SC
    binder.addBinding().to(SetStrokingColorN.class); // SCN
    binder.addBinding().to(SetStrokingColorSpace.class); // CS
    binder.addBinding().to(SetStrokingDeviceCMYKColor.class); // K
    binder.addBinding().to(SetStrokingDeviceGrayColor.class); // G
    binder.addBinding().to(SetStrokingDeviceRGBColor.class); // RG
  }
}
