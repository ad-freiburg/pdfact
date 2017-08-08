package pdfact.cli;

import org.junit.Test;

import pdfact.cli.PdfActCommandLineInterface;
import pdfact.exception.PdfActParseCommandLineException;
import pdfact.exception.PdfActValidateException;

/**
 * Tests for the class {@link PdfActCommandLineInterface}.
 * 
 * @author Claudius Korzen
 */
public class PdfActCommandLineInterfaceTest {
  /**
   * Tests the run method with empty arguments.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = PdfActParseCommandLineException.class)
  public void testRunEmptyArgs() throws Exception {
    new PdfActCommandLineInterface().run(new String[] {});
  }
  
  /**
   * Tests the run method with the -h flag.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = PdfActParseCommandLineException.class)
  public void testRunHelpFlag1() throws Exception {
    new PdfActCommandLineInterface().run(new String[] {"-h"});
  }
  
  /**
   * Tests the run method with the --help flag.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = PdfActParseCommandLineException.class)
  public void testRunHelpFlag2() throws Exception {
    new PdfActCommandLineInterface().run(new String[] {"--help"});
  }
  
  /**
   * Tests the run method with only one argument.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = PdfActValidateException.class)
  public void testRunOneArg() throws Exception {
    new PdfActCommandLineInterface().run(new String[] {"foo"});
  }
  
  /**
   * Tests the run method, where both arguments are invalid.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = PdfActValidateException.class)
  public void testRunTwoInvalidArgs() throws Exception {
    new PdfActCommandLineInterface().run(new String[] {"foo", "bar"});
  }
  
  /**
   * Tests the run method, where the first argument is a folder.
   * 
   * @throws Exception If running the test fails.
   */
  @Test(expected = PdfActValidateException.class)
  public void testRunFirstArgumentFolder() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    String path = classLoader.getResource("pdfs").getPath();
    new PdfActCommandLineInterface().run(new String[] {path, "bar"});
  }
}
