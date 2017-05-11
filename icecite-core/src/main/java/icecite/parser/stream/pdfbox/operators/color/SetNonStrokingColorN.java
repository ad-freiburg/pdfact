package icecite.parser.stream.pdfbox.operators.color;

/**
 * scn: Sets the color to use for non-stroking operations. Supports Pattern,
 * Separation, DeviceN and ICCBased color spaces.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColorN extends SetNonStrokingColor {
  @Override
  public String getName() {
    return "scn";
  }
}
