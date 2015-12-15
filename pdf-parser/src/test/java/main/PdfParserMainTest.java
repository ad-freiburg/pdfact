package main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.freiburg.iif.paths.PathsUtil;
import model.PdfDocument;
import parser.PdfParser;
import parser.pdfbox.PdfBoxParser;
import serializer.PdfSerializer;
import serializer.TsvPdfSerializer;

/**
 * Tests for our main parser class.
 *
 * @author Claudius Korzen
 *
 */
public class PdfParserMainTest {
  /**
   * The default pdf parser for testing purposes.
   */
  protected PdfParser defaultPdfParser = new PdfBoxParser();

  /**
   * The default output formatter.
   */
  protected PdfSerializer defaultOutputFormatter = new TsvPdfSerializer();

  /**
   * The default input directory.
   */
  protected String defaultInputDir = "src/test/resources/input/";

  /**
   * The default output directory.
   */
  protected String defaultOutputDir =
      "src/test/resources/PdfParserMainTest/output/";

  /**
   * The default groundtruth directory.
   */
  protected String defaultGroundtruthDir =
      "src/test/resources/PdfParserMainTest/groundtruth/";

  /**
   * Setting up.
   */
  @Before
  public void setup() throws IOException {
    // Delete the content in the default output directory.
    PathsUtil.cleanDirectory(Paths.get(defaultOutputDir));
  }

  // ___________________________________________________________________________
  // Test the constructor.

  /**
   * Test the constructor with a null argument.
   */
  @Test
  public void testConstructorWithNull() throws Exception {
    PdfParser parser = null;
    PdfParserMain program = new PdfParserMain(parser);
    Assert.assertNotNull(PdfParserMain.pdfParsers);
    Assert.assertFalse(PdfParserMain.pdfParsers.isEmpty());
    Assert.assertNotNull(PdfParserMain.pdfSerializers);
    Assert.assertFalse(PdfParserMain.pdfSerializers.isEmpty());
    Assert.assertNotNull(PdfParserMain.defaultPdfParserName);
    Assert.assertFalse(PdfParserMain.defaultPdfParserName.isEmpty());
    Assert.assertNotNull(PdfParserMain.defaultPdfSerializerName);
    Assert.assertFalse(PdfParserMain.defaultPdfSerializerName.isEmpty());
    Assert.assertNull(program.pdfParser);
    Assert.assertNull(program.pdfSerializer);
    Assert.assertNull(program.input);
    Assert.assertNull(program.inputFileOrDirectory);
    Assert.assertNull(program.output);
    Assert.assertNull(program.outputDirectory);
    Assert.assertTrue(program.pdfsToProcess.isEmpty());
    Assert.assertEquals(0, program.numOfProcessedPdfs);
  }

  /**
   * Test the constructor with a valid parser argument.
   */
  @Test
  public void testConstructorWithValidParser() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    Assert.assertNotNull(PdfParserMain.pdfParsers);
    Assert.assertFalse(PdfParserMain.pdfParsers.isEmpty());
    Assert.assertNotNull(PdfParserMain.pdfSerializers);
    Assert.assertFalse(PdfParserMain.pdfSerializers.isEmpty());
    Assert.assertNotNull(PdfParserMain.defaultPdfParserName);
    Assert.assertFalse(PdfParserMain.defaultPdfParserName.isEmpty());
    Assert.assertNotNull(PdfParserMain.defaultPdfSerializerName);
    Assert.assertFalse(PdfParserMain.defaultPdfSerializerName.isEmpty());
    Assert.assertEquals(defaultPdfParser, program.pdfParser);
    Assert.assertNull(program.pdfSerializer);
    Assert.assertNull(program.input);
    Assert.assertNull(program.inputFileOrDirectory);
    Assert.assertNull(program.output);
    Assert.assertNull(program.outputDirectory);
    Assert.assertTrue(program.pdfsToProcess.isEmpty());
    Assert.assertEquals(0, program.numOfProcessedPdfs);
  }

  // ___________________________________________________________________________
  // Test the start() method.

  /**
   * Test start() method with no parser. Should throw a
   * IllegalArgumentException, because no parser is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNoParser() throws Exception {
    PdfParser parser = null;
    PdfParserMain program = new PdfParserMain(parser);
    program.process();
  }

  /**
   * Test start() method with a valid parser. Should throw a
   * IllegalArgumentException, because no output formatter is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithValidParser() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.process();
  }

  /**
   * Test start() method with a valid parser and valid output formatter. Should
   * throw an IllegalArgumentException, because no input is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithParserAndOutputFormatter() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setOutputFormatter(defaultOutputFormatter);
    program.process();
  }

  /**
   * Test start() method with a valid parser and valid output formatter.
   */
  @Test
  public void testProcessWithParserWithInput() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setOutputFormatter(defaultOutputFormatter);
    program.setInput(defaultInputDir);
    program.setPreventConsoleOutput(true);
    List<PdfDocument> result = program.process();

    Assert.assertNotNull(program.input);
    Assert.assertEquals(defaultInputDir, program.input);
    Assert.assertNotNull(program.pdfParser);
    Assert.assertEquals(defaultPdfParser, program.pdfParser);
    Assert.assertEquals(defaultOutputFormatter, program.pdfSerializer);
    Assert.assertNotNull(program.pdfsToProcess);
    Assert.assertEquals(0, program.pdfsToProcess.size());
    Assert.assertEquals(6, program.numOfProcessedPdfs);

    Assert.assertNotNull(result);
    Assert.assertEquals(6, result.size());
    // TODO: Test the correctness of the documents.
  }

  // ___________________________________________________________________________
  // Test parseNextPdf() method.

  /**
   * Test parseNextPdf() method when there are no pdfs given.
   */
  @Test
  public void testParseNextPdfWithNoPdfs() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    Assert.assertNull(program.parseNextPdf());
    Assert.assertEquals(0, program.numOfProcessedPdfs);
  }

  /**
   * Test parseNextPdf() method when there are pdfs given.
   */
  @Test
  public void testParseNextPdfWithPdfs() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setOutputFormatter(defaultOutputFormatter);
    program.setPreventConsoleOutput(true);
    program.setInput(defaultInputDir);
    program.initialize();

    PdfDocument document = program.parseNextPdf();
    Assert.assertNotNull(document);
    Assert.assertEquals(1, program.numOfProcessedPdfs);
    document = program.parseNextPdf();
    Assert.assertNotNull(document);
    Assert.assertEquals(2, program.numOfProcessedPdfs);
    document = program.parseNextPdf();
    Assert.assertNotNull(document);
    Assert.assertEquals(3, program.numOfProcessedPdfs);
    document = program.parseNextPdf();
    Assert.assertNotNull(document);
    Assert.assertEquals(4, program.numOfProcessedPdfs);
    document = program.parseNextPdf();
    Assert.assertNotNull(document);
    Assert.assertEquals(5, program.numOfProcessedPdfs);
    document = program.parseNextPdf();
    Assert.assertNotNull(document);
    Assert.assertEquals(6, program.numOfProcessedPdfs);
    document = program.parseNextPdf();
    Assert.assertNull(document);
    Assert.assertEquals(6, program.numOfProcessedPdfs);
  }

  // ___________________________________________________________________________
  // Test the serialize() method.

  /**
   * Test output() method.
   */
  @Test
  public void testSerialize() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setOutputFormatter(defaultOutputFormatter);
    program.setInput(defaultInputDir);
    program.setOutput(defaultOutputDir);

    program.initialize();

    List<PdfDocument> textDocuments = new ArrayList<>();
    PdfDocument textDocument;
    while ((textDocument = program.parseNextPdf()) != null) {
      textDocuments.add(textDocument);
    }

    program.serialize(textDocuments);

    Path gtPath = Paths.get(defaultGroundtruthDir);
    Path outputPath = Paths.get(defaultOutputDir);
    Assert.assertTrue(PathsUtil.directoryContentEquals(gtPath, outputPath));
  }

  // ___________________________________________________________________________
  // Test the initialize() method.

  /**
   * Test, if an exception is thrown, if there is no pdf parser given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithNoPdfParser() throws Exception {
    PdfParser parser = null;
    PdfParserMain program = new PdfParserMain(parser);
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, if there is no input given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithNoInput() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, if the given input file does not exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithNonExistingInputFile() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("/path/that/does/not/exist/file.pdf");
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, if the given input file isn't a pdf file.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithNoPdfFile() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("src/test/resources/input/no-pdfs/no-pdf.xml");
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, when the given input directory does not
   * exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithNonExistingInputDir() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("/path/that/does/not/exist/");
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, when the given input directory does not
   * contain any pdf files.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithInputDirWithNoPdfs() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("src/test/resources/input/no-pdfs/");
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, if no output format is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithNoOutputFormat() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("src/test/resources/input/only-text.pdf");
    program.initialize();
  }

  /**
   * Test, if an exception is thrown, if the output format is not valid.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInitializeWithInvalidOutputFormat() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("src/test/resources/input/only-text.pdf");
    program.setOutputFormatter(null);
    program.initialize();
  }

  /**
   * Test the initialize method for a single pdf input file.
   */
  @Test
  public void testInitializeWithPdfFile() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("src/test/resources/input/only-text.pdf");
    program.setOutputFormatter(defaultOutputFormatter);
    program.setPreventConsoleOutput(true);
    program.initialize();

    Assert.assertNotNull(program.input);
    Assert.assertNotNull(program.pdfParser);
    Assert.assertEquals(defaultPdfParser, program.pdfParser);
    Assert.assertEquals(PdfParserMain.defaultPdfSerializerName,
        program.getOutputFormatter().getOutputFormat());
    Assert.assertNotNull(program.pdfsToProcess);
    Assert.assertEquals(1, program.pdfsToProcess.size());
    Assert.assertEquals(0, program.numOfProcessedPdfs);
    Assert.assertEquals("src/test/resources/input/only-text.pdf",
        program.getInput());
  }

  /**
   * Test the initialize method for a single input directory.
   */
  @Test
  public void testInitializeWithPdfDir() throws Exception {
    PdfParserMain program = new PdfParserMain(defaultPdfParser);
    program.setInput("src/test/resources/input/");
    program.setOutputFormatter(defaultOutputFormatter);
    program.setPreventConsoleOutput(true);
    program.initialize();

    Assert.assertNotNull(program.input);
    Assert.assertNotNull(program.pdfParser);
    Assert.assertEquals(defaultPdfParser, program.pdfParser);
    Assert.assertEquals(defaultOutputFormatter, program.getOutputFormatter());
    Assert.assertNotNull(program.pdfsToProcess);
    Assert.assertEquals(6, program.pdfsToProcess.size());
    Assert.assertEquals(0, program.numOfProcessedPdfs);
    Assert.assertEquals("src/test/resources/input/", program.getInput());
  }

  // ___________________________________________________________________________
  // Test the main method.

  /**
   * Test, if an exception is thrown, when there are no arguments.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyCommandLineArguments() throws Exception {
    String[] args = {};
    PdfParserMain.main(args);
  }

  /**
   * Test, if an exception is thrown, when the given input file doesn't exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonExistingInputFile() throws Exception {
    String[] args = { "/path/that/does/not/exist/file.pdf" };
    PdfParserMain.main(args);
  }

  /**
   * Test, if an exception is thrown, when the input file isn't a pdf file.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonPdfFile() throws Exception {
    String[] args = { "src/test/resources/input/no-pdfs/no-pdf.xml" };
    PdfParserMain.main(args);
  }

  /**
   * Test, if an exception is thrown, when the given input directory does not
   * exist.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonExistingInputDir() throws Exception {
    String[] args = { "/path/that/does/not/exist/" };
    PdfParserMain.main(args);
  }

  /**
   * Test, if an exception is thrown, when the given input directory does not
   * contain any pdf files.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInputDirWithNoPdfs() throws Exception {
    String[] args = { "src/test/resources/input/no-pdfs/" };
    PdfParserMain.main(args);
  }

  /**
   * Test, if an exception is thrown, when the given pdf parser isn't valid.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonValidPdfParser() throws Exception {
    String[] args = { "-p", "foobar", "src/test/resources/input/" };
    PdfParserMain.main(args);
  }

  /**
   * Test, if an exception is thrown, when the given output format isn't given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonValidOutputFormat() throws Exception {
    String[] args = { "-f", "foobar", "src/test/resources/input/" };
    PdfParserMain.main(args);
  }
}
