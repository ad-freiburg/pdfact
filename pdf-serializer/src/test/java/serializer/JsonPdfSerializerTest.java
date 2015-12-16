package serializer;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.freiburg.iif.path.PathUtils;
import mock.Mocks;

/**
 * Tests for the JsonPdfSerializerTest.
 *
 * @author Claudius Korzen
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonPdfSerializerTest {
  /**
   * The path to the resources dir.
   */
  final String resourcesPath = "src/test/resources/JsonPdfSerializerTest";
  
  /**
   * The serializer under test.
   */
  JsonPdfSerializer serializer;
      
  /**
   * Setup.
   */
  @Before
  public void setup() {
    this.serializer = new JsonPdfSerializer();
  }
  
  /**
   * Test the print() method.
   */
  @Test
  public void testSerialize() throws Exception {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    serializer.serialize(Mocks.mockDocument1(), stream);
    stream.close();
    
    Path path = Paths.get(resourcesPath).resolve("expected1.txt");
    // Whitespaces and indentation doesn't matter in json, so remove it.
    String expected = PathUtils.readPathContentToString(path).replaceAll("\\s+", "");
    String actual = new String(stream.toByteArray()).replaceAll("\\s+", "");
    
    Assert.assertEquals(expected, actual);
  }
}
