package serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.PdfColor;
import model.PdfDocument;
import model.PdfElement;
import model.PdfFeature;
import model.PdfFont;
import model.PdfPage;
import model.Serializable;

/**
 * Serializes PdfTextDocument to json format.
 *
 * @author Claudius Korzen
 */
public class JsonPdfSerializer implements PdfSerializer {
  /**
   * The indent length.
   */
  protected static final int INDENT_LENGTH = 4;

  @Override
  public String getOutputFormat() {
    return "json";
  }

  @Override
  public void serialize(PdfDocument doc, OutputStream s) throws IOException {
    serialize(doc, null, s);
  }

  @Override
  public void serialize(PdfDocument document, List<PdfFeature> features,
      OutputStream s) throws IOException {
    JSONObject json = new JSONObject();

    JSONArray pages = new JSONArray();
    for (PdfPage page : document.getPages()) {
      pages.put(serializePage(page, features));
    }
    json.put("pages", pages);

    JSONArray fonts = new JSONArray();
    for (PdfFont font : document.getFonts()) {
      fonts.put(toJson(font));
    }
    json.put("fonts", fonts);

    JSONArray colors = new JSONArray();
    for (PdfColor color : document.getColors()) {
      colors.put(toJson(color));
    }
    json.put("colors", colors);

    s.write(json.toString(INDENT_LENGTH).getBytes(StandardCharsets.UTF_8));
  }

  // ___________________________________________________________________________

  /**
   * Serializes the given page to tsv.
   */
  protected JSONObject serializePage(PdfPage page, List<PdfFeature> features)
    throws IOException {
    JSONObject json = new JSONObject();

    // If the list of features is empty, take all features.
    if (features == null || features.isEmpty()) {
      features = Arrays.asList(PdfFeature.values());
    }

    json.put("id", page.getPageNumber());

    for (PdfFeature feature : features) {
      json.put(feature.name(), serialize(page.getElementsByFeature(feature)));
    }

    return json;
  }

  /**
   * Serializes the given page to tsv.
   */
  protected JSONArray serialize(List<? extends PdfElement> elements)
    throws IOException {
    JSONArray json = new JSONArray();
    if (elements != null) {
      for (PdfElement element : elements) {
        JSONObject serialized = serialize(element);
        if (serialized != null) {
          json.put(serialized);
        }
      }
    }
    return json;
  }

  /**
   * Serializes the given page to tsv.
   */
  protected JSONObject serialize(PdfElement element) throws IOException {
    return element != null && !element.ignore() ? toJson(element) : null;
  }
  
  /**
   * Serializes the given page to tsv.
   */
  protected JSONObject toJson(Serializable element) throws IOException {
    return element != null ? element.toJson() : null;
  }
}