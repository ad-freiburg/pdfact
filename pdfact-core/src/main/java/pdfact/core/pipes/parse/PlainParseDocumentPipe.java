package pdfact.core.pipes.parse;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pdfact.core.model.Character;
import pdfact.core.model.Document;
import pdfact.core.model.Page;
import pdfact.core.pipes.parse.stream.pdfbox.PdfBoxPdfStreamsParser;
import pdfact.core.util.exception.PdfActException;

/**
 * A plain implementation of {@link ParseDocumentPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainParseDocumentPipe implements ParseDocumentPipe {
  /**
   * The logger.
   */
  protected final Logger log = LogManager.getLogger("char-extraction");

  // ==============================================================================================

  @Override
  public Document execute(Document doc) throws PdfActException {
    parseDocument(doc);
    
    if (log.isDebugEnabled()) {
      for (Page page : doc.getPages()) {
        log.debug("==================== Page " + page.getPageNumber() + " ====================");
        for (Character character : page.getCharacters()) {
          log.debug("-------------------------------------------");
          log.debug("Character:     " + character.getText());
          log.debug("... page:      " + character.getPosition().getPageNumber());
          String minX = String.format("%.1f", character.getPosition().getRectangle().getMinX());
          String minY = String.format("%.1f", character.getPosition().getRectangle().getMinY());
          String maxX = String.format("%.1f", character.getPosition().getRectangle().getMaxX());
          String maxY = String.format("%.1f", character.getPosition().getRectangle().getMaxY());
          log.debug("... position:  [" + minX + ", " + minY + ", " + maxX + ", " +maxY + "]");
          log.debug("... font:      " + character.getFontFace().getFont().getBaseName());
          log.debug("... fontsize:  " + character.getFontFace().getFontSize() + "pt");
          log.debug("... is bold:   " + character.getFontFace().getFont().isBold());
          log.debug("... is italic: " + character.getFontFace().getFont().isItalic());
          log.debug("... is type3:  " + character.getFontFace().getFont().isType3Font());
          log.debug("... RGB color: " + Arrays.toString(character.getColor().getRGB()));
          log.debug("... rank:      " + character.getExtractionRank());        
        }
      }
    }

    return doc;
  }

  /**
   * Parses the given document.
   * 
   * @param doc
   *        The document to parse.
   * 
   * @throws PdfActException
   *         If something went wrong while parsing the document.
   */
  protected void parseDocument(Document doc) throws PdfActException {
    new PdfBoxPdfStreamsParser().parse(doc);
  }
}
