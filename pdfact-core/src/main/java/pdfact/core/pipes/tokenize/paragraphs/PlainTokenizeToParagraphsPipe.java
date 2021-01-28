package pdfact.core.pipes.tokenize.paragraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import pdfact.core.model.Character;
import pdfact.core.model.CharacterStatistic;
import pdfact.core.model.Color;
import pdfact.core.model.Document;
import pdfact.core.model.FontFace;
import pdfact.core.model.Page;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Position;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;
import pdfact.core.util.PdfActUtils;
import pdfact.core.util.exception.PdfActException;
import pdfact.core.util.lexicon.CharacterLexicon;
import pdfact.core.util.list.ElementList;
import pdfact.core.util.statistician.CharacterStatistician;
import pdfact.core.util.statistician.TextLineStatistician;

/**
 * A plain implementation of {@link TokenizeToParagraphsPipe}.
 * 
 * @author Claudius Korzen
 */
public class PlainTokenizeToParagraphsPipe implements TokenizeToParagraphsPipe {
  /**
   * The logger.
   */
  protected static Logger log = LogManager.getFormatterLogger("paragraph-detection");

  /**
   * The statistician to compute statistics about characters.
   */
  protected CharacterStatistician characterStatistician;

  /**
   * The statistician to compute statistics about text lines.
   */
  protected TextLineStatistician textLineStatistician;

  /**
   * The number of processed text blocks.
   */
  protected int numProcessedTextBlocks;

  /**
   * The number of tokenized paragraphs.
   */
  protected int numTokenizedParagraphs;

  /**
   * Creates a new pipe that tokenizes the text blocks of a PDF document into paragraphs.
   */
  public PlainTokenizeToParagraphsPipe() {
    this.characterStatistician = new CharacterStatistician();
    this.textLineStatistician = new TextLineStatistician();
  }

  @Override
  public Document execute(Document pdf) throws PdfActException {
    tokenizeToParagraphs(pdf);

    if (log.isDebugEnabled()) {
      for (Paragraph paragraph : pdf.getParagraphs()) {
        log.debug("-------------------------------------------");
        log.debug("Detected paragraph: \"%s\"", paragraph.getText());
        for (int i = 0; i < paragraph.getPositions().size(); i++) {
          Position pos = paragraph.getPositions().get(i);
          log.debug("... page[%d]:         %d", i, pos.getPageNumber());
          float x1 = pos.getRectangle().getMinX();
          float y1 = pos.getRectangle().getMinY();
          float x2 = pos.getRectangle().getMaxX();
          float y2 = pos.getRectangle().getMaxY();
          log.debug("... bounding box[%d]: [%.1f, %.1f, %.1f, %.1f]", i, x1, y1, x2, y2);
        }
        
        FontFace fontFace = paragraph.getCharacterStatistic().getMostCommonFontFace();
        log.debug("... main font:       %s", fontFace.getFont().getBaseName());
        log.debug("... main fontsize:   %.1fpt", fontFace.getFontSize());
        float avgFontsize = paragraph.getCharacterStatistic().getAverageFontsize();
        log.debug("... avg. fontsize:   %.1fpt", avgFontsize);
        log.debug("... mainly bold:     %s", fontFace.getFont().isBold());
        log.debug("... mainly italic:   %s", fontFace.getFont().isItalic());
        log.debug("... mainly type3:    %s", fontFace.getFont().isType3Font());
        Color color = paragraph.getCharacterStatistic().getMostCommonColor();
        log.debug("... main RGB color:  %s", Arrays.toString(color.getRGB()));
        log.debug("... role:            %s", paragraph.getSemanticRole());
      }
    }
    
    return pdf;
  }

  // ==============================================================================================

  /**
   * Tokenizes the text block of the given PDF document into paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   */
  protected void tokenizeToParagraphs(Document pdf) {
    ElementList<Paragraph> paragraphs = new ElementList<>();

    // Segment the PDF document into paragraphs.
    List<List<TextBlock>> segments = segmentIntoParagraphs(pdf);

    // Create the PdfParagraph objects.
    for (List<TextBlock> segment : segments) {
      Paragraph paragraph = new Paragraph();
      for (TextBlock block : segment) {
        for (TextLine line : block.getTextLines()) {
          paragraph.addWords(line.getWords());
        }
      }
      paragraph.setText(computeText(paragraph));
      paragraph.setPositions(computePositions(segment));
      paragraph.setSemanticRole(computeRole(segment));
      paragraph.setCharacterStatistic(computeCharacterStatistic(paragraph));
      paragraphs.add(paragraph);
    }

    this.numTokenizedParagraphs += paragraphs.size();

    pdf.setParagraphs(paragraphs);
  }

  /**
   * Computes the text for the given paragraph.
   * 
   * @param p
   *        The paragraph to process.
   * @return The text for the given paragraph.
   */
  protected String computeText(Paragraph p) {
    return PdfActUtils.join(p.getWords(), " ");
  }

  /**
   * Computes the positions for the given paragraph.
   * 
   * @param blocks
   *        The blocks of the paragraph to process.
   * 
   * @return The positions for the given paragraph.
   */
  protected List<Position> computePositions(List<TextBlock> blocks) {
    List<Position> positions = new ArrayList<>();
    for (TextBlock block : blocks) {
      positions.add(block.getPosition());
    }
    return positions;
  }

  /**
   * Computes the role for the given paragraph.
   * 
   * @param blocks
   *        The blocks of the paragraph to process.
   * 
   * @return The role for the given paragraph.
   */
  protected SemanticRole computeRole(List<TextBlock> blocks) {
    if (blocks == null || blocks.isEmpty()) {
      return null;
    }
    // Return the role of the first text block.
    return blocks.get(0).getSemanticRole();
  }

  /**
   * Computes the statistic about the characters of the given paragraph.
   * 
   * @param paragraph
   *        The paragraph to process.
   * 
   * @return The statistic.
   */
  protected CharacterStatistic computeCharacterStatistic(Paragraph paragraph) {
    return this.characterStatistician.aggregate(paragraph.getWords());
  }

  // ==============================================================================================

  /**
   * Segments the given PDF document into paragraphs.
   * 
   * @param pdf
   *        The PDF document to process.
   * 
   * @return The list of list of text blocks of a paragraph.
   */
  protected List<List<TextBlock>> segmentIntoParagraphs(Document pdf) {
    List<List<TextBlock>> result = new ArrayList<>();

    // Put all blocks to a single list to be able to iterate them in one go.
    List<TextBlock> allTextBlocks = new ArrayList<>();
    for (Page page : pdf.getPages()) {
      allTextBlocks.addAll(page.getTextBlocks());
    }

    TIntSet indexesOfAlreadyProcessedBlocks = new TIntHashSet();

    // Identify the paragraphs from the text blocks.
    for (int i = 0; i < allTextBlocks.size(); i++) {
      TextBlock block = allTextBlocks.get(i);

      this.numProcessedTextBlocks++;

      if (indexesOfAlreadyProcessedBlocks.contains(i)) {
        // The block was already added to a paragraph. Ignore it.
        continue;
      }

      // Create a new paragraph.
      List<TextBlock> paragraphBlocks = new ArrayList<>();
      paragraphBlocks.add(block);
      indexesOfAlreadyProcessedBlocks.add(i);

      // If the role of the block is "body text", check if there is another
      // block in the remaining blocks that belongs to the same paragraph.
      if (block.getSemanticRole() == SemanticRole.BODY_TEXT) {
        for (int j = i + 1; j < allTextBlocks.size(); j++) {
          TextBlock otherBlock = allTextBlocks.get(j);
          if (otherBlock.getSemanticRole() == SemanticRole.HEADING) {
            break;
          }
          if (otherBlock.getSemanticRole() == SemanticRole.ITEMIZE_ITEM) {
            break;
          }
          if (otherBlock.getSemanticRole() == SemanticRole.BODY_TEXT) {
            if (!belongsToParagraph(otherBlock, paragraphBlocks)) {
              break;
            }
            // Add the block to the existing paragraph.
            paragraphBlocks.add(otherBlock);
            indexesOfAlreadyProcessedBlocks.add(j);
          }
        }
      }
      result.add(paragraphBlocks);
    }
    return result;
  }

  // ==============================================================================================

  /**
   * Checks, if the given text block belongs to the given paragraph.
   * 
   * @param block
   *        The text block to process.
   * @param paraBlocks
   *        The paragraph to process.
   * 
   * @return True, if the given text block should be added to the paragraph.
   */
  protected boolean belongsToParagraph(TextBlock block, List<TextBlock> paraBlocks) {
    if (block == null) {
      return false;
    } 
    
    log.debug("-----------------------------------------------------");
    log.debug("Text block: \"%s\" ...", block.getText());

    if (paraBlocks == null || paraBlocks.isEmpty()) {
      log.debug("... does *not* belong to the prev. paragraph because there is no prev. paragraph");
      return false;
    }

    TextBlock lastParaBlock = paraBlocks.get(paraBlocks.size() - 1);
    log.debug("... page:                          %s", block.getPosition().getPageNumber());
    log.debug("... bounding box:                  %s", block.getPosition().getRectangle());
    log.debug("... last block of prev. paragraph: %s", lastParaBlock.getText());

    // The block belongs to the paragraph, if the paragraph doesn't end with a terminating 
    // punctuation mark.
    Word word = lastParaBlock.getLastTextLine().getLastWord();
    Character lastChar = word != null ? word.getLastCharacter() : null;
    if (!CharacterLexicon.isTerminatingPunctuationMark(lastChar)) {
      log.debug("... belongs to prev. paragraph:    true (the prev. paragraph doesn't end "
         + "with a terminating punctuation mark).");
      return true;
    }

    // The block belongs to the paragraph, if the block starts with an
    // lowercased letter.
    Word firstWord = block.getFirstTextLine().getFirstWord();
    if (CharacterLexicon.isLowercase(firstWord.getFirstCharacter())) {
      log.debug("... belongs to prev. paragraph:    true (the block starts lowercased).");
      return true;
    }

    log.debug("... belongs to prev. paragraph:    false (no rule applied).");

    return false;
  }
}