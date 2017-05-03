package parser.pdfbox.core.operator.color;

/**
 * SCN: Sets the colour to use for stroking stroking operations.
 * Supports Pattern, Separation, DeviceN and ICCBased colour spaces.
 *
 * @author Claudius Korzen
 */
public class SetStrokingColorN extends SetStrokingColor {
  @Override
  public String getName() {
    return "SCN";
  }
}
