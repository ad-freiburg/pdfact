package pdfact.tokenize.paragraphs;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import pdfact.models.PdfCharacter;
import pdfact.models.PdfDocument;
import pdfact.models.PdfPage;
import pdfact.models.PdfRole;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfWord;
import pdfact.utils.character.PdfCharacterUtils;

// TODO: Need to refactor.

/**
 * A plain implementation of {@link PdfParagraphSegmenter}.
 * 
 * @author Claudius Korzen
 */
public class PlainPdfParagraphSegmenter implements PdfParagraphSegmenter {
  @Override
  public List<List<PdfTextBlock>> segment(PdfDocument pdf) {
    List<List<PdfTextBlock>> result = new ArrayList<>();

    // Put all blocks to a single list to be able to iterate them in one go.
    List<PdfTextBlock> allTextBlocks = new ArrayList<>();
    for (PdfPage page : pdf.getPages()) {
      allTextBlocks.addAll(page.getTextBlocks());
    }

    TIntSet indexesOfAlreadyProcessedBlocks = new TIntHashSet();
    
    // Identify the paragraphs from the text blocks.
    for (int i = 0; i < allTextBlocks.size(); i++) {
      PdfTextBlock block = allTextBlocks.get(i);

      if (indexesOfAlreadyProcessedBlocks.contains(i)) {
        // The block was already added to a paragraph. Ignore it.
        continue;
      }

      // Create a new paragraph.
      List<PdfTextBlock> paragraphBlocks = new ArrayList<>();
      paragraphBlocks.add(block);
      indexesOfAlreadyProcessedBlocks.add(i);
      
      // If the role of the block is "body text", check if there is another
      // block in the remaining blocks that belongs to the same paragraph.
      if (block.getRole() == PdfRole.BODY_TEXT) {
        for (int j = i + 1; j < allTextBlocks.size(); j++) {
          PdfTextBlock otherBlock = allTextBlocks.get(j);
          if (otherBlock.getRole() != PdfRole.BODY_TEXT) {
            continue;
          }
          if (!belongsToParagraph(otherBlock, paragraphBlocks)) {
            break;
          }
          // Add the block to the existing paragraph.
          paragraphBlocks.add(otherBlock);
          indexesOfAlreadyProcessedBlocks.add(j);
        }
      }
      result.add(paragraphBlocks);
    }
    return result;
  }

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
  protected boolean belongsToParagraph(PdfTextBlock block,
      List<PdfTextBlock> paraBlocks) {
    if (block == null || paraBlocks == null) {
      return false;
    }

    if (paraBlocks.isEmpty()) {
      return false;
    }
    
    PdfTextBlock lastParaBlock = paraBlocks.get(paraBlocks.size() - 1);
    
    // The block belongs to the paragraph, if the paragraph doesn't end with
    // a punctuation mark.
    PdfWord word = lastParaBlock.getLastTextLine().getLastWord();
    PdfCharacter lastChar = word != null ? word.getLastCharacter() : null;
    if (!PdfCharacterUtils.isPunctuationMark(lastChar)) {
      return true;
    }

    // The block belongs to the paragraph, if the block starts with an
    // lowercased letter.
    PdfWord firstWord = block.getFirstTextLine().getFirstWord();
    if (PdfCharacterUtils.isLowercase(firstWord.getFirstCharacter())) {
      return true;
    }

    return false;
  }
}
