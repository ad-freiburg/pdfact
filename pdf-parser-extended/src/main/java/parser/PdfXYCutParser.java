package parser;

import static model.SweepDirection.HorizontalSweepDirection.BOTTOM_TO_TOP;
import static model.SweepDirection.HorizontalSweepDirection.TOP_TO_BOTTOM;
import static model.SweepDirection.VerticalSweepDirection.LEFT_TO_RIGHT;
import static model.SweepDirection.VerticalSweepDirection.RIGHT_TO_LEFT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.freiburg.iif.model.Rectangle;
import de.freiburg.iif.model.simple.SimpleRectangle;
import model.Comparators;
import model.PdfArea;
import model.PdfDocument;
import model.PdfNonTextParagraph;
import model.PdfPage;
import model.PdfTextLine;
import model.PdfTextParagraph;
import model.PdfWord;
import model.PdfXYCutArea;
import model.PdfXYCutNonTextParagraph;
import model.PdfXYCutTextLine;
import model.PdfXYCutTextParagraph;
import model.PdfXYCutWord;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;
import rules.BlockifyNonTextPageRule;
import rules.BlockifyRule;
import rules.BlockifyTextBlockRule;
import rules.BlockifyTextLineRule;
import rules.BlockifyTextPageRule;
import rules.ParagraphifyRule;

/**
 * Implementation of a PdfAnalyzer mainly based on the xy-cut algorithm.
 *
 * @author Claudius Korzen
 */
public class PdfXYCutParser implements PdfExtendedParser {
  /**
   * The rule to blockify a page into text blocks.
   */
  BlockifyTextPageRule blockifyPageRule = new BlockifyTextPageRule();

  /**
   * The rule to blockify a page into non text blocks.
   */
  BlockifyNonTextPageRule blockifyNonTextPageRule =
      new BlockifyNonTextPageRule();

  /**
   * The rule to blockify a text block into text lines.
   */
  BlockifyTextBlockRule blockifyBlockRule = new BlockifyTextBlockRule();

  /**
   * The rule to blockify a text line into words.
   */
  BlockifyTextLineRule blockifyLineRule = new BlockifyTextLineRule();

  // ___________________________________________________________________________

  @Override
  public PdfDocument parse(PdfDocument document) {
    if (document == null) {
      return document;
    }

    if (document.getPages() == null) {
      return document;
    }

    for (PdfPage page : document.getPages()) {
      List<PdfArea> blocks = identifyBlocks(page);

      List<PdfNonTextParagraph> nonTextParas = identifyNonTextParagraphs(page);
      page.setNonTextParagraphs(nonTextParas);

      List<PdfTextLine> lines = identifyLines(page, blocks);
      page.setTextLines(lines);

      List<PdfWord> words = identifyWords(page, lines);
      page.setWords(words);

      List<PdfTextParagraph> paragraphs = identifyParagraphs(page, lines);
      page.setParagraphs(paragraphs);
    }

    return document;
  }

  // ___________________________________________________________________________
  // Identification methods.

  /**
   * Identifies text blocks from text characters in the given page.
   */
  protected List<PdfArea> identifyBlocks(PdfPage page) {
    PdfArea area = new PdfXYCutArea(page, page.getTextCharacters());
    return blockify(area, this.blockifyPageRule);
  }

  /**
   * Identifies non text paragraphs from non text elements in the given page.
   */
  protected List<PdfNonTextParagraph> identifyNonTextParagraphs(PdfPage page) {
    PdfArea area = new PdfXYCutArea(page, page.getNonTextElements());
    List<PdfArea> paraAreas = blockify(area, this.blockifyNonTextPageRule);
    return toNonTextParagraphs(page, paraAreas);
  }

  /**
   * Identifies text lines in the given list of blocks.
   */
  protected List<PdfTextLine> identifyLines(PdfPage page, List<PdfArea> areas) {
    List<PdfTextLine> textLines = new ArrayList<>();
    for (PdfArea area : areas) {
      List<PdfArea> lineAreas = blockify(area, this.blockifyBlockRule);
      textLines.addAll(toTextLines(page, lineAreas));
    }
    return textLines;
  }

  /**
   * Identifies words in the given list of text lines. This method will add the
   * words to the correspondent line and returns a list of all identified words
   * in the lines.
   */
  protected List<PdfWord> identifyWords(PdfPage page, List<PdfTextLine> lines) {
    List<PdfWord> words = new ArrayList<>();
    for (PdfTextLine line : lines) {
      List<PdfArea> wordAreas = blockify(line, this.blockifyLineRule);

      // Sort the words and the characters in the words by x-value.
      Collections.sort(wordAreas, new Comparators.MinXComparator());
      for (PdfArea area : wordAreas) {
        Collections.sort(area.getElements(), new Comparators.MinXComparator());
      }

      List<PdfXYCutWord> lineWords = toWords(page, wordAreas);

      // TODO: Dehyphenize.
      if (!words.isEmpty()) {
        PdfWord lastWord = words.get(words.size() - 1);
        String lastCharacter = lastWord.getTextCharacters()
            .get(lastWord.getTextCharacters().size() - 1).getUnicode();

        if (lastCharacter != null && !lastCharacter.isEmpty()) {
          char lastChar = lastCharacter.charAt(0);
          if (lastChar == '-') {
            lastWord.setIsHyphenized(true);
          }
        }
      }

      words.addAll(lineWords);
      line.setWords(lineWords);
    }
    return words;
  }

  /**
   * Identifies paragraphs from the given list of text lines.
   */
  protected List<PdfTextParagraph> identifyParagraphs(PdfPage page,
      List<PdfTextLine> lines) {
    List<PdfTextParagraph> paragraphs = new ArrayList<>();
    PdfTextParagraph paragraph = new PdfXYCutTextParagraph(page);

    for (int i = 0; i < lines.size(); i++) {
      PdfTextLine prevLine = i > 0 ? lines.get(i - 1) : null;
      PdfTextLine line = lines.get(i);
      PdfTextLine nextLine = i < lines.size() - 1 ? lines.get(i + 1) : null;

      if (ParagraphifyRule.introducesNewParagraph(page, paragraph, prevLine,
          line, nextLine)) {
        paragraphs.add(paragraph);
        paragraph = new PdfXYCutTextParagraph(page);
      }
      paragraph.addTextLine(line);
    }
    paragraphs.add(paragraph);

    return paragraphs;
  }

  // ___________________________________________________________________________
  // Blockify methods.

  /**
   * Splits the given area into smaller areas, correspondent to the given
   * blockify rules.
   */
  protected List<PdfArea> blockify(PdfArea area, BlockifyRule rule) {
    List<PdfArea> subareas = new ArrayList<>();
    blockify(area, rule, subareas);

    return subareas;
  }

  /**
   * Splits the given page area into blocks recursively, correspondent to the
   * given rule.
   */
  protected void blockify(PdfArea area, BlockifyRule rule,
      List<PdfArea> result) {
    // Try to split the area vertically.
    List<PdfArea> areas = splitVertically(area, rule);

    if (areas.isEmpty()) {
      // The area couldn't be separated vertically. Try it horizontally.
      areas = splitHorizontally(area, rule);

      if (areas.isEmpty()) {
        // The area couldn't also be separated horizontally.
        // Add the area to result.
        result.add(area);
      } else {
        // The area was split horizontally.
        for (PdfArea subarea : areas) {
          blockify(subarea, rule, result);
        }
      }
    } else {
      // The area was split vertically.
      for (PdfArea subarea : areas) {
        blockify(subarea, rule, result);
      }
    }
  }

  // ___________________________________________________________________________

  /**
   * Tries to split the given area vertically.
   */
  protected List<PdfArea> splitVertically(PdfArea area, BlockifyRule rule) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Identify the vertical split lane.
      Rectangle lane = identifyVerticalLane(area, rule);

      if (lane != null) {
        List<Rectangle> subRects = rect.splitVertically(lane.getXMidpoint());
        for (Rectangle subRect : subRects) {
          result.add(new PdfXYCutArea(area, subRect));
        }
      }
    }
    return result;
  }

  /**
   * Tries to split the given area horizontally.
   */
  protected List<PdfArea> splitHorizontally(PdfArea area, BlockifyRule rule) {
    List<PdfArea> result = new ArrayList<PdfArea>();

    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Identify the vertical split lane.
      Rectangle lane = identifyHorizontalLane(area, rule);

      if (lane != null) {
        List<Rectangle> subRects = rect.splitHorizontally(lane.getYMidpoint());
        for (Rectangle subRect : subRects) {
          result.add(new PdfXYCutArea(area, subRect));
        }
      }
    }
    return result;
  }

  // ___________________________________________________________________________

  /**
   * Identifies a vertical lane in the given area.
   */
  protected Rectangle identifyVerticalLane(PdfArea area, BlockifyRule rule) {
    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Determine the sweep direction.
      VerticalSweepDirection dir = rule.getVerticalLaneSweepDirection();
      // Determine the width of vertical lane to sweep.
      float ruleLaneWidth = rule.getVerticalLaneWidth(area);
      float lMinY = rect.getMinY();
      float lMaxY = rect.getMaxY();
      float lMinX = rect.getMinX();
      float lMaxX = rect.getMinX() + ruleLaneWidth;
      if (dir == RIGHT_TO_LEFT) {
        // Adjust the x values, if sweep direction is from right to left.
        lMinX = (rect.getMaxX() - ruleLaneWidth);
        lMaxX = rect.getMaxX();
      }

      // We don't want to simply find the first valid lane (in relation
      // to the given rules), but try to expand the lane as far as possible.
      // The ruleLane corresponds to the values given by the rules:
      Rectangle ruleLane = new SimpleRectangle(lMinX, lMinY, lMaxX, lMaxY);
      // The lane corresponds to the the "expanded" lane (the lane to return).
      Rectangle lane = new SimpleRectangle(Float.MAX_VALUE, lMinY,
          -Float.MAX_VALUE, lMaxY);

      float pos = lMinX;
      // TODO: Do we really need this flag?
      boolean invalidLaneAlreadySeen = false;

      // Sweep the lane through the bounding box.
      while (pos >= rect.getMinX() && pos <= rect.getMaxX() - ruleLaneWidth) {
        ruleLane.moveTo(pos, ruleLane.getMinY());

        if (rule.isValidVerticalLane(area, ruleLane)) {
          if (invalidLaneAlreadySeen) {
            // Expand the lane.
            lane.setMinX(Math.min(lane.getMinX(), ruleLane.getMinX()));
            lane.setMaxX(Math.max(lane.getMaxX(), ruleLane.getMaxX()));
          }
        } else {
          if (lane.getWidth() >= ruleLaneWidth) {
            return lane;
          }

          lane.setMinX(Float.MAX_VALUE);
          lane.setMaxX(-Float.MAX_VALUE);
          invalidLaneAlreadySeen = true;
        }
        pos = (dir == LEFT_TO_RIGHT) ? (pos + 1) : (pos - 1);
      }
    }
    return null;
  }

  /**
   * Identifies a horizontal lane in the given area.
   */
  protected Rectangle identifyHorizontalLane(PdfArea area, BlockifyRule rule) {
    Rectangle rect = area.getRectangle();
    if (rect != null) {
      // Determine the sweep direction.
      HorizontalSweepDirection dir = rule.getHorizontalLaneSweepDirection();
      // Determine the width of vertical lane to sweep.
      float ruleLaneHeight = rule.getHorizontalLaneHeight(area);

      float lMinX = rect.getMinX();
      float lMaxX = rect.getMaxX();
      float lMinY = rect.getMaxY() - ruleLaneHeight;
      float lMaxY = rect.getMaxY();
      if (dir == BOTTOM_TO_TOP) {
        lMinY = rect.getMinY();
        lMaxY = rect.getMinY() + ruleLaneHeight;
      }

      // We don't want to simply find the first valid lane (in relation
      // to the given rules), but try to expand the lane as far as possible.
      // The ruleLane corresponds to the values given by the rules:
      Rectangle ruleLane = new SimpleRectangle(lMinX, lMinY, lMaxX, lMaxY);
      // The lane corresponds to the the "expanded" lane (the lane to return).
      Rectangle lane = new SimpleRectangle(lMinX, Float.MAX_VALUE,
          lMaxX, -Float.MAX_VALUE);

      float pos = lMinY;
      // TODO: Do we really need this flag?
      boolean invalidLaneAlreadySeen = false;

      // Sweep the lane through the bounding box.
      while (pos >= rect.getMinY() && pos <= rect.getMaxY() - ruleLaneHeight) {
        ruleLane.moveTo(ruleLane.getMinX(), pos);

        if (rule.isValidHorizontalLane(area, ruleLane)) {
          if (invalidLaneAlreadySeen) {
            // Expand the lane.
            lane.setMinY(Math.min(lane.getMinY(), ruleLane.getMinY()));
            lane.setMaxY(Math.max(lane.getMaxY(), ruleLane.getMaxY()));
          }
        } else {
          if (lane.getHeight() >= ruleLaneHeight) {
            return lane;
          }

          lane.setMinY(Float.MAX_VALUE);
          lane.setMaxY(-Float.MAX_VALUE);
          invalidLaneAlreadySeen = true;
        }
        pos = (dir == TOP_TO_BOTTOM) ? (pos - 1) : (pos + 1);
      }
    }
    return null;
  }

  // ___________________________________________________________________________
  // Util methods. TODO: Get rid of these methods.

  /**
   * Transforms the given list of page areas to a list of text lines.
   */
  protected List<PdfTextLine> toTextLines(PdfPage page, List<PdfArea> areas) {
    List<PdfTextLine> result = new ArrayList<>();
    for (PdfArea area : areas) {
      result.add(new PdfXYCutTextLine(page, area));
    }
    return result;
  }

  /**
   * Transforms the given list of page areas to a list of words.
   */
  protected List<PdfXYCutWord> toWords(PdfPage page, List<PdfArea> areas) {
    List<PdfXYCutWord> result = new ArrayList<>();
    for (PdfArea area : areas) {
      result.add(new PdfXYCutWord(page, area));
    }
    return result;
  }

  /**
   * Transforms the given list of page areas to a list of paragraphs.
   */
  protected List<PdfTextParagraph> toParagraphs(PdfPage page,
      List<PdfArea> areas) {
    List<PdfTextParagraph> result = new ArrayList<>();
    for (PdfArea a : areas) {
      result.add(new PdfXYCutTextParagraph(page, a));
    }
    return result;
  }

  /**
   * Transforms the given list of page areas to a list of non text paragraphs.
   */
  protected List<PdfNonTextParagraph> toNonTextParagraphs(PdfPage page,
      List<PdfArea> areas) {
    List<PdfNonTextParagraph> result = new ArrayList<>();
    for (PdfArea area : areas) {
      result.add(new PdfXYCutNonTextParagraph(page, area));
    }
    return result;
  }
}
