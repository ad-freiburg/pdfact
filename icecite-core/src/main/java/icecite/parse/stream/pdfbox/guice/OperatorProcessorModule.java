package icecite.parse.stream.pdfbox.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

import icecite.parse.stream.pdfbox.convert.PDColorConverter;
import icecite.parse.stream.pdfbox.convert.PDFontConverter;
import icecite.parse.stream.pdfbox.convert.PDFontFaceConverter;
import icecite.parse.stream.pdfbox.operators.OperatorProcessor;
import icecite.parse.stream.pdfbox.operators.color.SetNonStrokingColor;
import icecite.parse.stream.pdfbox.operators.color.SetNonStrokingColorN;
import icecite.parse.stream.pdfbox.operators.color.SetNonStrokingColorSpace;
import icecite.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceCMYKColor;
import icecite.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceGrayColor;
import icecite.parse.stream.pdfbox.operators.color.SetNonStrokingDeviceRGBColor;
import icecite.parse.stream.pdfbox.operators.color.SetStrokingColor;
import icecite.parse.stream.pdfbox.operators.color.SetStrokingColorN;
import icecite.parse.stream.pdfbox.operators.color.SetStrokingColorSpace;
import icecite.parse.stream.pdfbox.operators.color.SetStrokingDeviceCMYKColor;
import icecite.parse.stream.pdfbox.operators.color.SetStrokingDeviceGrayColor;
import icecite.parse.stream.pdfbox.operators.color.SetStrokingDeviceRGBColor;
import icecite.parse.stream.pdfbox.operators.graphic.AppendRectangleToPath;
import icecite.parse.stream.pdfbox.operators.graphic.BeginInlineImage;
import icecite.parse.stream.pdfbox.operators.graphic.ClipEvenOddRule;
import icecite.parse.stream.pdfbox.operators.graphic.ClipNonZeroRule;
import icecite.parse.stream.pdfbox.operators.graphic.ClosePath;
import icecite.parse.stream.pdfbox.operators.graphic.CurveTo;
import icecite.parse.stream.pdfbox.operators.graphic.CurveToReplicateFinalPoint;
import icecite.parse.stream.pdfbox.operators.graphic.CurveToReplicateInitialPoint;
import icecite.parse.stream.pdfbox.operators.graphic.EndPath;
import icecite.parse.stream.pdfbox.operators.graphic.FillEvenOddAndStrokePath;
import icecite.parse.stream.pdfbox.operators.graphic.FillEvenOddRule;
import icecite.parse.stream.pdfbox.operators.graphic.FillNonZeroAndStrokePath;
import icecite.parse.stream.pdfbox.operators.graphic.FillNonZeroRule;
import icecite.parse.stream.pdfbox.operators.graphic.Invoke;
import icecite.parse.stream.pdfbox.operators.graphic.LineTo;
import icecite.parse.stream.pdfbox.operators.graphic.ModifyCurrentTransformationMatrix;
import icecite.parse.stream.pdfbox.operators.graphic.MoveTo;
import icecite.parse.stream.pdfbox.operators.graphic.RestoreGraphicsState;
import icecite.parse.stream.pdfbox.operators.graphic.SaveGraphicsState;
import icecite.parse.stream.pdfbox.operators.graphic.SetGraphicsStateParameters;
import icecite.parse.stream.pdfbox.operators.graphic.StrokePath;
import icecite.parse.stream.pdfbox.operators.text.BeginText;
import icecite.parse.stream.pdfbox.operators.text.EndText;
import icecite.parse.stream.pdfbox.operators.text.MoveText;
import icecite.parse.stream.pdfbox.operators.text.MoveTextSetLeading;
import icecite.parse.stream.pdfbox.operators.text.MoveToNextLineAndShowText;
import icecite.parse.stream.pdfbox.operators.text.MoveToNextLineAndShowTextWithSpacing;
import icecite.parse.stream.pdfbox.operators.text.SetCharacterSpacing;
import icecite.parse.stream.pdfbox.operators.text.SetFontAndSize;
import icecite.parse.stream.pdfbox.operators.text.SetTextHorizontalScaling;
import icecite.parse.stream.pdfbox.operators.text.SetTextLeading;
import icecite.parse.stream.pdfbox.operators.text.SetTextMatrix;
import icecite.parse.stream.pdfbox.operators.text.SetTextRenderingMode;
import icecite.parse.stream.pdfbox.operators.text.SetTextRise;
import icecite.parse.stream.pdfbox.operators.text.SetType3GlyphWidthAndBoundingBox;
import icecite.parse.stream.pdfbox.operators.text.SetWordSpacing;
import icecite.parse.stream.pdfbox.operators.text.ShowText;
import icecite.parse.stream.pdfbox.operators.text.ShowTextWithIndividualGlyphPositioning;

/**
 * A module that defines the Guice bindings to the operator processors.
 * 
 * @author Claudius Korzen
 */
public class OperatorProcessorModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PDFontFaceConverter.class).in(Singleton.class);
    bind(PDFontConverter.class).in(Singleton.class);
    bind(PDColorConverter.class).in(Singleton.class);
    
    Multibinder<OperatorProcessor> binder = Multibinder.newSetBinder(binder(),
        OperatorProcessor.class);

    // Bind the text operators.
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

    // Bind the graphics operators.
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

    // Bind the color operators.
    binder.addBinding().to(SetNonStrokingColor.class);
    binder.addBinding().to(SetNonStrokingColorN.class);
    binder.addBinding().to(SetNonStrokingColorSpace.class);
    binder.addBinding().to(SetNonStrokingDeviceCMYKColor.class);
    binder.addBinding().to(SetNonStrokingDeviceGrayColor.class);
    binder.addBinding().to(SetNonStrokingDeviceRGBColor.class);
    binder.addBinding().to(SetStrokingColor.class);
    binder.addBinding().to(SetStrokingColorN.class);
    binder.addBinding().to(SetStrokingColorSpace.class);
    binder.addBinding().to(SetStrokingDeviceCMYKColor.class);
    binder.addBinding().to(SetStrokingDeviceGrayColor.class);
    binder.addBinding().to(SetStrokingDeviceRGBColor.class);
  }
}
