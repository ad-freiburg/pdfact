package pdfact.cli.pipes.serialize;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import pdfact.cli.PdfAct;
import pdfact.cli.model.ExtractionUnit;
import pdfact.core.model.Document;
import pdfact.core.model.SemanticRole;
import pdfact.core.util.exception.PdfActException;

public class PdfXmlSerializerTest {
  @Test
  public void testParagraphSerialization() throws PdfActException {
    // Parse a PDF document.
    Document pdf = new PdfAct().parse("src/test/resources/ACL_2004.pdf");

    Set<ExtractionUnit> units = new HashSet<>();
    units.add(ExtractionUnit.PARAGRAPH);

    Set<SemanticRole> roles = new HashSet<>();
    roles.add(SemanticRole.TITLE);

    PdfXmlSerializer serializer = new PdfXmlSerializer(units, roles);
    String serialization = new String(serializer.serialize(pdf), StandardCharsets.UTF_8);

    Assert.assertEquals(
        "<document>\n"
        + "  <paragraphs>\n"
        + "    <paragraph>\n"
        + "      <positions>\n"
        + "        <position>\n"
        + "          <page>1</page>\n"
        + "          <minX>135.0</minX>\n"
        + "          <minY>682.2</minY>\n"
        + "          <maxX>476.9</maxX>\n"
        + "          <maxY>711.1</maxY>\n"
        + "        </position>\n"
        + "      </positions>\n"
        + "      <role>title</role>\n"
        + "      <text>Accurate Information Extraction from Research Papers using Conditional "
        + "Random Fields</text>\n"
        + "    </paragraph>\n"
        + "  </paragraphs>\n"
        + "</document>", serialization);
  }
}
