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

public class PdfJsonSerializerTest {
  @Test
  public void testSerialization() throws PdfActException {
    // Parse a PDF document.
    Document pdf = new PdfAct().parse("src/test/resources/ACL_2004.pdf");

    Set<ExtractionUnit> units = new HashSet<>();
    units.add(ExtractionUnit.PARAGRAPH);

    Set<SemanticRole> roles = new HashSet<>();
    roles.add(SemanticRole.TITLE);

    PdfJsonSerializer serializer = new PdfJsonSerializer(units, roles);
    String serialization = new String(serializer.serialize(pdf), StandardCharsets.UTF_8);
    
    // Replace all runs of whitespaces and newlines in the serialization by a single whitespace.
    serialization = serialization.replaceAll("\\s+", " ");

    Assert.assertEquals("{\"paragraphs\": [{\"paragraph\": { \"role\": \"title\", \"positions\": [{ "
            + "\"minY\": 682.2, \"minX\": 135, \"maxY\": 711.1, \"maxX\": 476.9, \"page\": 1 }], "
            + "\"text\": \"Accurate Information Extraction from Research Papers using Conditional "
            + "Random Fields\" }}]}", serialization);
  }
}
