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
 * Tests for the TsvPdfSerializerTest.
 *
 * @author Claudius Korzen
 */
@RunWith(MockitoJUnitRunner.class)
public class TsvPdfSerializerTest {
  /**
   * The path to the resources dir.
   */
  final String resourcesPath = "src/test/resources/TsvPdfSerializerTest";
  
  /**
   * The serializer under test.
   */
  TsvPdfSerializer serializer;
      
  /**
   * Setup.
   */
  @Before
  public void setup() {
    this.serializer = new TsvPdfSerializer();
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
    String expected = PathUtils.readPathContentToString(path);
    String actual = new String(stream.toByteArray());
            
    Assert.assertEquals(expected, actual);
  }
}
