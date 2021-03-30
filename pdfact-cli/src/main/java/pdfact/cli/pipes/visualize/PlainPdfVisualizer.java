package pdfact.cli.pipes.visualize;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import pdfact.cli.model.ExtractionUnit;
import pdfact.cli.pipes.visualize.pdfbox.PdfBoxDrawer;
import pdfact.cli.util.exception.PdfActVisualizeException;
import pdfact.core.model.Character;
import pdfact.core.model.Document;
import pdfact.core.model.Element;
import pdfact.core.model.Figure;
import pdfact.core.model.HasPosition;
import pdfact.core.model.HasPositions;
import pdfact.core.model.HasSemanticRole;
import pdfact.core.model.Page;
import pdfact.core.model.Paragraph;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.SemanticRole;
import pdfact.core.model.Shape;
import pdfact.core.model.TextArea;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;

/**
 * A plain implementation of {@link PdfVisualizer}.
 *
 * @author Claudius Korzen
 */
public class PlainPdfVisualizer implements PdfVisualizer {
  /**
   * The units to visualize.
   */
  protected Set<ExtractionUnit> extractionUnits;

  /**
   * The semantic roles to consider on visualizing.
   */
  protected Set<SemanticRole> semanticRolesToInclude;

  // ==============================================================================================
  // Constructors.

  /**
   * Creates a new PDF visualizer.
   *
   * @param units The text units.
   * @param roles The semantic roles to include.
   */
  public PlainPdfVisualizer(Set<ExtractionUnit> extractionUnits, Set<SemanticRole> roles) {
    this.extractionUnits = extractionUnits;
    this.semanticRolesToInclude = roles;
  }

  // ==============================================================================================

  @Override
  public byte[] visualize(Document pdf) throws PdfActVisualizeException {
    if (pdf != null) {
      try {
        PdfDrawer drawer = new PdfBoxDrawer(pdf.getFile());

        for (ExtractionUnit unit : this.extractionUnits) {
          switch (unit) {
            case CHARACTER:
              visualizeCharacters(pdf, drawer);
              break;
            case TEXT_AREA:
              visualizeTextAreas(pdf, drawer);
              break;
            case TEXT_LINE:
              visualizeTextLines(pdf, drawer);
              break;
            case WORD:
              visualizeWords(pdf, drawer);
              break;
            case TEXT_BLOCK:
              visualizeTextBlocks(pdf, drawer);
              break;
            case PARAGRAPH:
            default:
              visualizeParagraphs(pdf, drawer);
              break;
          }
        }

        return drawer.toByteArray();
      } catch (IOException e) {
        throw new PdfActVisualizeException("Error on visualization.", e);
      }
    }
    return null;
  }

  // ==============================================================================================

  /**
   * Visualizes the characters of the given PDF document.
   *
   * @param pdf    The PDF document to process.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeCharacters(Document pdf, PdfDrawer drawer)
          throws PdfActVisualizeException {
    for (Paragraph paragraph : pdf.getParagraphs()) {
      // Ignore the paragraph if its role should not be extracted.
      if (!hasRelevantRole(paragraph)) {
        continue;
      }

      for (Word word : paragraph.getWords()) {
        if (word == null) {
          continue;
        }

        for (Character character : word.getCharacters()) {
          visualizeCharacter(character, drawer);
        }
      }
    }

    // try {
    // System.out.println("Visualize");
    // Rectangle rect = this.rectangleFactory.create(323.2,881.3,328.7,886.8);
    // drawer.drawRectangle(rect, 1, Color.BLUE, Color.BLUE);
    // rect = this.rectangleFactory.create(323.2, 600, 328.7, 610);
    // drawer.drawRectangle(rect, 1, Color.BLUE, Color.BLUE);
    // } catch (Exception e) {
    // System.out.println("ERROR");
    // }
  }

  /**
   * Visualizes the given character.
   *
   * @param character The character to visualize.
   * @param drawer    The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeCharacter(Character character, PdfDrawer drawer)
          throws PdfActVisualizeException {
    visualizePdfElement(character, drawer, Color.BLACK);
  }

  // ==============================================================================================

  /**
   * Visualizes the text areas of the given PDF document.
   *
   * @param pdf    The PDF document to process.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeTextAreas(Document pdf, PdfDrawer drawer)
          throws PdfActVisualizeException {
    for (Page page : pdf.getPages()) {
      for (TextArea area : page.getTextAreas()) {
        visualizeTextArea(area, drawer);
      }
    }
  }

  /**
   * Visualizes the given text area.
   *
   * @param textArea The text area to visualize.
   * @param drawer   The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeTextArea(TextArea textArea, PdfDrawer drawer)
          throws PdfActVisualizeException {
    visualizePdfElement(textArea, drawer, Color.GRAY);
  }

  // ==============================================================================================

  /**
   * Visualizes the text lines of the given PDF document.
   *
   * @param pdf    The PDF document to process.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeTextLines(Document pdf, PdfDrawer drawer)
          throws PdfActVisualizeException {
    for (Page page : pdf.getPages()) {
      for (TextLine line : page.getTextLines()) {
        visualizeTextLine(line, drawer);
      }
    }
  }

  /**
   * Visualizes the given text line.
   *
   * @param textLine The text line to visualize.
   * @param drawer   The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeTextLine(TextLine textLine, PdfDrawer drawer)
          throws PdfActVisualizeException {
    // System.out.println(textLine.getText() + " " + textLine.getPosition().getRectangle());
    visualizePdfElement(textLine, drawer, Color.BLUE);
  }

  // ==============================================================================================

  /**
   * Visualizes the words of the given PDF document.
   *
   * @param pdf    The PDF document to process.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeWords(Document pdf, PdfDrawer drawer) throws PdfActVisualizeException {
    // Visualize the textual elements.
    for (Paragraph paragraph : pdf.getParagraphs()) {
      // Ignore the paragraph if its role should not be extracted.
      if (!hasRelevantRole(paragraph)) {
        continue;
      }

      for (Word word : paragraph.getWords()) {
        visualizeWord(word, drawer);
      }
    }
  }

  /**
   * Visualizes the given word.
   *
   * @param word   The word to visualize.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeWord(Word word, PdfDrawer drawer) throws PdfActVisualizeException {
    visualizePdfElement(word, drawer, Color.BLUE);
  }

  // ==============================================================================================

  /**
   * Visualizes the text lines of the given PDF document.
   *
   * @param pdf    The PDF document to process.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeTextBlocks(Document pdf, PdfDrawer drawer)
          throws PdfActVisualizeException {
    for (Page page : pdf.getPages()) {
      for (TextBlock block : page.getTextBlocks()) {
        visualizeTextBlock(block, drawer);
      }
    }
  }

  /**
   * Visualizes the given text block.
   *
   * @param textBlock The text block to visualize.
   * @param drawer    The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeTextBlock(TextBlock textBlock, PdfDrawer drawer)
          throws PdfActVisualizeException {
    visualizePdfElement(textBlock, drawer, Color.YELLOW);
  }

  // ==============================================================================================

  /**
   * Visualizes the paragraphs of the given PDF document.
   *
   * @param pdf    The PDF document to process.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeParagraphs(Document pdf, PdfDrawer drawer)
          throws PdfActVisualizeException {
    // Visualize the textual elements.
    for (Paragraph paragraph : pdf.getParagraphs()) {
      // Ignore the paragraph if its role should not be extracted.
      if (!hasRelevantRole(paragraph)) {
        continue;
      }

      visualizeParagraph(paragraph, drawer);
    }
  }

  /**
   * Visualizes the given paragraph.
   *
   * @param paragraph The paragraph to visualize.
   * @param drawer    The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeParagraph(Paragraph paragraph, PdfDrawer drawer)
          throws PdfActVisualizeException {
    visualizePdfElement(paragraph, drawer, Color.BLUE);
  }

  // ==============================================================================================

  /**
   * Visualizes the given figure.
   *
   * @param figure The figure to visualize.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeFigure(Figure figure, PdfDrawer drawer) throws PdfActVisualizeException {
    visualizePdfElement(figure, drawer, Color.CYAN);
  }

  /**
   * Visualizes the given shape.
   *
   * @param shape  The shape to visualize.
   * @param drawer The drawer to use.
   *
   * @throws PdfActVisualizeException If the drawing failed.
   */
  protected void visualizeShape(Shape shape, PdfDrawer drawer) throws PdfActVisualizeException {
    visualizePdfElement(shape, drawer, Color.ORANGE);
  }

  // ==============================================================================================

  /**
   * Visualizes the given PDF element using the given drawer.
   *
   * @param element The element to visualize.
   * @param drawer  The drawer to use.
   * @param color   The color to use.
   * @throws PdfActVisualizeException If something went wrong on visualization.
   */
  protected void visualizePdfElement(Element element, PdfDrawer drawer, Color color)
          throws PdfActVisualizeException {
    if (element instanceof HasPositions) {
      HasPositions hasPositions = (HasPositions) element;
      List<Position> positions = hasPositions.getPositions();

      visualizePositions(drawer, color, element, positions);
    }

    if (element instanceof HasPosition) {
      HasPosition hasPosition = (HasPosition) element;
      Position position = hasPosition.getPosition();

      visualizePositions(drawer, color, element, position);
    }
  }

  /**
   * Visualizes the given PDF positions with the given color.
   *
   * @param drawer    The drawer to use.
   * @param color     The color to use.
   * @param element   The element to which the given positions belong to.
   * @param positions The positions to visualize.
   * @throws PdfActVisualizeException If something went wrong on visualization.
   */
  protected void visualizePositions(PdfDrawer drawer, Color color, Element element,
          Position... positions) throws PdfActVisualizeException {
    visualizePositions(drawer, color, element, Arrays.asList(positions));
  }

  /**
   * Visualizes the given PDF positions with the given color.
   *
   * @param drawer    The drawer to use.
   * @param color     The color to use.
   * @param elem      The element to which the given positions belong to.
   * @param positions The positions to visualize.
   * @throws PdfActVisualizeException If something went wrong on visualization.
   */
  protected void visualizePositions(PdfDrawer drawer, Color color, Element elem,
          List<Position> positions) throws PdfActVisualizeException {
    if (drawer == null) {
      return;
    }

    if (positions == null) {
      return;
    }

    for (int i = 0; i < positions.size(); i++) {
      Position position = positions.get(i);
      visualizePosition(elem, position, drawer, color);

      // int page = position.getPageNumber();
      // Rectangle rect = position.getRectangle();
      // try {
      //   drawer.drawText("[" + i + "]", page, new Point(rect.getMaxX() + 5, rect.getMinY()), color);
      // } catch (IOException e) {
      //   throw new PdfActVisualizeException("Couldn't visualize the PDF document", e);
      // }
    }
  }

  /**
   * Visualizes the given PDF position of the given element.
   *
   * @param element  The element to which the given position belongs to.
   * @param position The position to visualize.
   * @param drawer   The drawer to use.
   * @param color    The color to use.
   * @throws PdfActVisualizeException If something went wrong on visualization.
   */
  protected void visualizePosition(Element element, Position position, PdfDrawer drawer,
          Color color) throws PdfActVisualizeException {
    if (position != null) {
      Page page = position.getPage();
      Rectangle rect = position.getRectangle();

      if (page != null && rect != null) {
        int pageNum = page.getPageNumber();
        try {
          drawer.drawRectangle(rect, pageNum, color, null, 1f);

          // Draw the semantic role, if there is any.
          if (element instanceof HasSemanticRole) {
            HasSemanticRole hasSemanticRole = (HasSemanticRole) element;
            SemanticRole role = hasSemanticRole.getSemanticRole();
            if (role != null) {
              String roleStr = role.getName();
              Point pos = rect.getUpperLeft();
              drawer.drawText(roleStr, pageNum, pos, color, 8f);
            }
          }
        } catch (IOException e) {
          throw new PdfActVisualizeException("Couldn't visualize the PDF document", e);
        }
      }
    }
  }

  // ==============================================================================================

  @Override
  public Set<ExtractionUnit> getExtractionUnits() {
    return this.extractionUnits;
  }

  @Override
  public void setExtractiontUnits(Set<ExtractionUnit> extractionUnits) {
    this.extractionUnits = extractionUnits;
  }

  // ==============================================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesToInclude() {
    return this.semanticRolesToInclude;
  }

  @Override
  public void setSemanticRolesToInclude(Set<SemanticRole> roles) {
    this.semanticRolesToInclude = roles;
  }

  // ==============================================================================================

  /**
   * Checks if the semantic role of the given element is relevant, that is: if it is included in
   * this.semanticRolesToInclude.
   *
   * @param element The element to check.
   *
   * @return True, if the role of the given element is relevant.
   */
  protected boolean hasRelevantRole(HasSemanticRole element) {
    if (element == null) {
      return false;
    }

    if (this.semanticRolesToInclude == null || this.semanticRolesToInclude.isEmpty()) {
      // No semantic roles to include -> The element is not relevant.
      return false;
    }

    SemanticRole role = element.getSemanticRole();
    if (role == null) {
      return false;
    }

    return this.semanticRolesToInclude.contains(role);
  }
}
