package icecite.cli;

import org.junit.Test;

import icecite.exception.IceciteParseCommandLineException;
import icecite.exception.IceciteValidateException;

/**
 * Tests for the class {@link IceciteCommandLineInterface}.
 * 
 * @author Claudius Korzen
 */
public class IceciteCommandLineInterfaceTest {
  /**
   * Tests the run method with empty arguments.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = IceciteParseCommandLineException.class)
  public void testRunEmptyArgs() throws Exception {
    new IceciteCommandLineInterface().process(new String[] {});
  }
  
  /**
   * Tests the run method with the -h flag.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = IceciteParseCommandLineException.class)
  public void testRunHelpFlag1() throws Exception {
    new IceciteCommandLineInterface().process(new String[] {"-h"});
  }
  
  /**
   * Tests the run method with the --help flag.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = IceciteParseCommandLineException.class)
  public void testRunHelpFlag2() throws Exception {
    new IceciteCommandLineInterface().process(new String[] {"--help"});
  }
  
  /**
   * Tests the run method with only one argument.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = IceciteParseCommandLineException.class)
  public void testRunOneArg() throws Exception {
    new IceciteCommandLineInterface().process(new String[] {"foo"});
  }
  
  /**
   * Tests the run method, where both arguments are invalid.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = IceciteValidateException.class)
  public void testRunTwoInvalidArgs() throws Exception {
    new IceciteCommandLineInterface().process(new String[] {"foo", "bar"});
  }
  
  /**
   * Tests the run method, where the first argument is a folder.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = IceciteValidateException.class)
  public void testRunFirstArgumentFolder() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    String path = classLoader.getResource("pdfs").getPath();
    new IceciteCommandLineInterface().process(new String[] {path, "bar"});
  }
  
//  /**
//   * Tests the run method, where the first argument is a folder.
//   * 
//   * @throws Exception If running the test fails.
//   */
//  @Test
//  public void testRunTwoValidArguments() throws Exception {
//    ClassLoader classLoader = getClass().getClassLoader();
//    
//    String pdfFolder = classLoader.getResource("pdfs").getPath();
//    Path inputPath = Paths.get(pdfFolder, "file-1.pdf");
//    
//    String outputFolder = classLoader.getResource("serializations").getPath();
//    Path outputPath = Paths.get(outputFolder, "file-1.xml");
//    
//    // Delete the output path if it already exists (from another test run). 
//    if (Files.exists(outputPath)) {
//      Files.delete(outputPath);
//    }
//    
//    make sure that the input path exists and the output path does not exist.
//    Assert.assertTrue(Files.exists(inputPath));
//    Assert.assertFalse(Files.exists(outputPath));
//    
//    new IceciteCommandLineInterface().process(
//        new String[] { inputPath.toString(), outputPath.toString() }
//    );
//    
//    Assert.assertTrue(Files.exists(outputPath));
//  }
}
