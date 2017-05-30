// package icecite.models.plain;
//
// import java.util.HashMap;
// import java.util.Map;
//
// import icecite.models.PdfFont;
// import icecite.models.PdfFontRegistry;
//
/// **
// * A plain implementation of PdfFontFactory.
// *
// * @author Claudius Korzen
// */
// public class PlainPdfFontRegistry implements PdfFontRegistry {
// /**
// * The index of PdfFont objects per name.
// */
// protected Map<String, PdfFont> index = new HashMap<>();
//
// @Override
// public boolean hasFont(String fontName) {
// return this.index.containsKey(fontName);
// }
//
// @Override
// public PdfFont getFont(String fontName) {
// return this.index.get(fontName);
// }
//
// @Override
// public void registerFont(PdfFont font) {
// if (font == null) {
// return;
// }
// String fontName = font.getName();
// if (fontName == null) {
// return;
// }
// if (hasFont(fontName)) {
// throw new IllegalStateException("The font is already registered.");
// }
// // Set the id of the font.
// font.setId("" + this.index.size());
// // Index the font.
// this.index.put(fontName, font);
// }
// }
