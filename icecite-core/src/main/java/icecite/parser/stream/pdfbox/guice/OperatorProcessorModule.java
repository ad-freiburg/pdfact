package icecite.parser.stream.pdfbox.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import icecite.parser.stream.pdfbox.operators.OperatorProcessor;
import icecite.parser.stream.pdfbox.operators.color.SetNonStrokingColor;
import icecite.parser.stream.pdfbox.operators.color.SetNonStrokingColorN;
import icecite.parser.stream.pdfbox.operators.color.SetNonStrokingColorSpace;
import icecite.parser.stream.pdfbox.operators.color.SetNonStrokingDeviceCMYKColor;
import icecite.parser.stream.pdfbox.operators.color.SetNonStrokingDeviceGrayColor;
import icecite.parser.stream.pdfbox.operators.color.SetNonStrokingDeviceRGBColor;
import icecite.parser.stream.pdfbox.operators.color.SetStrokingColor;
import icecite.parser.stream.pdfbox.operators.color.SetStrokingColorN;
import icecite.parser.stream.pdfbox.operators.color.SetStrokingColorSpace;
import icecite.parser.stream.pdfbox.operators.color.SetStrokingDeviceCMYKColor;
import icecite.parser.stream.pdfbox.operators.color.SetStrokingDeviceGrayColor;
import icecite.parser.stream.pdfbox.operators.color.SetStrokingDeviceRGBColor;
import icecite.parser.stream.pdfbox.operators.graphic.AppendRectangleToPath;
import icecite.parser.stream.pdfbox.operators.graphic.BeginInlineImage;
import icecite.parser.stream.pdfbox.operators.graphic.ClipEvenOddRule;
import icecite.parser.stream.pdfbox.operators.graphic.ClipNonZeroRule;
import icecite.parser.stream.pdfbox.operators.graphic.ClosePath;
import icecite.parser.stream.pdfbox.operators.graphic.CurveTo;
import icecite.parser.stream.pdfbox.operators.graphic.CurveToReplicateFinalPoint;
import icecite.parser.stream.pdfbox.operators.graphic.CurveToReplicateInitialPoint;
import icecite.parser.stream.pdfbox.operators.graphic.EndPath;
import icecite.parser.stream.pdfbox.operators.graphic.FillEvenOddAndStrokePath;
import icecite.parser.stream.pdfbox.operators.graphic.FillEvenOddRule;
import icecite.parser.stream.pdfbox.operators.graphic.FillNonZeroAndStrokePath;
import icecite.parser.stream.pdfbox.operators.graphic.FillNonZeroRule;
import icecite.parser.stream.pdfbox.operators.graphic.Invoke;
import icecite.parser.stream.pdfbox.operators.graphic.LineTo;
import icecite.parser.stream.pdfbox.operators.graphic.ModifyCurrentTransformationMatrix;
import icecite.parser.stream.pdfbox.operators.graphic.MoveTo;
import icecite.parser.stream.pdfbox.operators.graphic.RestoreGraphicsState;
import icecite.parser.stream.pdfbox.operators.graphic.SaveGraphicsState;
import icecite.parser.stream.pdfbox.operators.graphic.SetGraphicsStateParameters;
import icecite.parser.stream.pdfbox.operators.graphic.StrokePath;
import icecite.parser.stream.pdfbox.operators.text.BeginText;
import icecite.parser.stream.pdfbox.operators.text.EndText;
import icecite.parser.stream.pdfbox.operators.text.MoveText;
import icecite.parser.stream.pdfbox.operators.text.MoveTextSetLeading;
import icecite.parser.stream.pdfbox.operators.text.MoveToNextLineAndShowText;
import icecite.parser.stream.pdfbox.operators.text.MoveToNextLineAndShowTextWithSpacing;
import icecite.parser.stream.pdfbox.operators.text.SetCharacterSpacing;
import icecite.parser.stream.pdfbox.operators.text.SetFontAndSize;
import icecite.parser.stream.pdfbox.operators.text.SetTextHorizontalScaling;
import icecite.parser.stream.pdfbox.operators.text.SetTextLeading;
import icecite.parser.stream.pdfbox.operators.text.SetTextMatrix;
import icecite.parser.stream.pdfbox.operators.text.SetTextRenderingMode;
import icecite.parser.stream.pdfbox.operators.text.SetTextRise;
import icecite.parser.stream.pdfbox.operators.text.SetType3GlyphWidthAndBoundingBox;
import icecite.parser.stream.pdfbox.operators.text.SetWordSpacing;
import icecite.parser.stream.pdfbox.operators.text.ShowText;
import icecite.parser.stream.pdfbox.operators.text.ShowTextWithIndividualGlyphPositioning;

/**
 * A module that defines the Guice bindings to the operator processors.
 * 
 * @author Claudius Korzen
 */
public class OperatorProcessorModule extends AbstractModule {
  @Override
  protected void configure() {
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
