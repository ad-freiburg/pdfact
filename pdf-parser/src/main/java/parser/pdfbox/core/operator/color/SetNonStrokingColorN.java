package parser.pdfbox.core.operator.color;

/**
 * scn: Sets the color to use for stroking non-stroking operations. Supports
 * Pattern, Separation, DeviceN and ICCBased colour spaces.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColorN extends SetNonStrokingColor {
  @Override
  public String getName() {
    return "scn";
  }
}
