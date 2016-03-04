package drawer.pdfbox;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.freiburg.iif.model.simple.SimpleLine;
import de.freiburg.iif.model.simple.SimplePoint;
import de.freiburg.iif.model.simple.SimpleRectangle;
import de.freiburg.iif.path.PathUtils;

/**
 * Tests for PdfBoxVisualizerTest.
 *
 * @author Claudius Korzen
 */
public class PdfBoxDrawerTest {
  /**
   * The input directory.
   */
  protected static Path inputDir = Paths.get("src/test/resources/input/");
  
  /**
   * The base directory for this test.
   */
  protected static Path baseDir = 
      Paths.get("src/test/resources/PdfBoxVisualizerTest/");
    
  /**
   * The output directory.
   */
  protected static Path outputDir = baseDir.resolve("output");
  
  /**
   * The groundtruth directory.
   */
  protected static Path groundTruthDir = baseDir.resolve("groundtruth");
  
  // ___________________________________________________________________________
  // Setup.
  
  /**
   * Setup.
   */
  @BeforeClass
  public static void setup() throws IOException {
    // Clean the output directory.
    // PathUtils.cleanDirectory(outputDir);
    // Assert, that the output directory is empty.
    // Assert.assertTrue(PathUtils.listPaths(outputDir).isEmpty());
  }
  
  // ___________________________________________________________________________
  // Test the constructor.
  
  /**
   * Test the constructor with null argument.
   */
  @Test(expected = NullPointerException.class)
  public void testConstructorWithNull() throws IOException {
    File file = null;
    new PdfBoxDrawer(file);
  }
  
  /**
   * Test the constructor with invalid file.
   */
  @Test(expected = FileNotFoundException.class)
  public void testConstructorWithInvalidFile() throws IOException {
    new PdfBoxDrawer(Paths.get("path/that/does/not/exist"));
  }
  
  /**
   * Test the constructor with directory.
   */
  @Test(expected = FileNotFoundException.class)
  public void testConstructorWithDirectory() throws IOException {
    new PdfBoxDrawer(inputDir);
  }
  
  /**
   * Test the constructor with file.
   */
  @Test
  public void testConstructorWithFile() throws IOException {
    Path path = inputDir.resolve("input-1.pdf");
    PdfBoxDrawer v = new PdfBoxDrawer(path);
    
    Assert.assertNotNull(v.getPdDocument());
    Assert.assertEquals(11, v.streams.size());
  }
  
  /**
   * Test the drawing functions at once.
   */
  @Test
  public void testDraw1() throws IOException {
    Path path = inputDir.resolve("input-1.pdf");
    PdfBoxDrawer v = new PdfBoxDrawer(path);
    
    v.drawLine(new SimpleLine(5, 5, 100, 100), 1);
    v.drawLine(new SimpleLine(250, 50, 10, 80), 2, Color.red);
    v.drawLine(new SimpleLine(50, 10, 70, 120), 2, Color.blue, 5);
    
    v.drawRectangle(new SimpleRectangle(30, 40, 50, 60), 1);
    v.drawRectangle(new SimpleRectangle(10, 400, 500, 120), 3, Color.green);
    v.drawRectangle(new SimpleRectangle(90, 30, 100, 100), 2, Color.green, 3);
    
    v.drawText("Hello World", 2);
    v.drawText("Foo bar", 1, new SimplePoint(40, 40));
    v.drawText("XXX", 3, new SimplePoint(200, 500), Color.green);
    v.drawText("Doof", 2, new SimplePoint(200, 500), Color.green, 30);
    
    Path groundTruthFile = groundTruthDir.resolve("groundtruth-1.pdf");
    Path outputFile = outputDir.resolve("output-1.pdf");
    
    OutputStream stream = Files.newOutputStream(outputFile);
    v.writeTo(stream);
    stream.close();

    Assert.assertTrue(Files.exists(outputFile));
    // Assert that the output is equal to the groundtruth. 

    System.out.println(groundTruthFile);
    System.out.println(outputFile);
    System.out.println(PathUtils.contentEquals(groundTruthFile, outputFile));
    
    Assert.assertTrue(PathUtils.contentEquals(groundTruthFile, outputFile));
  }
  
  /**
   * Test the drawing functions once more.
   */
  @Test
  public void testDraw2() throws IOException {
    Path path = inputDir.resolve("input-2.pdf");
    PdfBoxDrawer v = new PdfBoxDrawer(path);
    
    v.drawRectangle(new SimpleRectangle(30, 80, 0, -60), 1);
    v.drawRectangle(new SimpleRectangle(10, 400, 500, 120), 3, Color.blue);
    v.drawRectangle(new SimpleRectangle(10, -30, 10, 100), 2, Color.red, 3);
    
    v.drawLine(new SimpleLine(25, 50, 110, 800), 1);
    v.drawLine(new SimpleLine(10, 50, 20, 80), 2);
    v.drawLine(new SimpleLine(10, 10, 70, 100), 2, Color.blue, 5);
        
    v.drawText("Pups", 2);
    v.drawText("Bloed", 1, new SimplePoint(40, 40));
    v.drawText("Doof", 2, new SimplePoint(200, 500), Color.green);
    v.drawText("Doofer", 2, new SimplePoint(200, 500), Color.green, 30);
    
    Path groundTruthFile = groundTruthDir.resolve("groundtruth-2.pdf");
    Path outputFile = outputDir.resolve("output-2.pdf");
    
    OutputStream stream = Files.newOutputStream(outputFile);
    v.writeTo(stream);
    stream.close();

    Assert.assertTrue(Files.exists(outputFile));
    // Assert that the output is equal to the groundtruth. 
    Assert.assertTrue(PathUtils.contentEquals(groundTruthFile, outputFile));
  }
  
  /**
   * Test the drawing functions with invalid page number.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDrawWithInvalidPageNumber() throws IOException {
    Path path = inputDir.resolve("input-2.pdf");
    PdfBoxDrawer v = new PdfBoxDrawer(path);
    
    v.drawRectangle(new SimpleRectangle(5, 5, 10, 10), 0);
  }
  
  /**
   * Test the drawing functions with invalid page number.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testDrawWithInvalidPageNumber2() throws IOException {
    Path path = inputDir.resolve("input-2.pdf");
    PdfBoxDrawer v = new PdfBoxDrawer(path);
    
    v.drawRectangle(new SimpleRectangle(5, 5, 10, 10), 100);
  }
}
