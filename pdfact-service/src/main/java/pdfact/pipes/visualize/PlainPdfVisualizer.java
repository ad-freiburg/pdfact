package pdfact.pipes.visualize;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.exception.PdfActVisualizeException;
import pdfact.model.HasElementType;
import pdfact.model.HasPosition;
import pdfact.model.HasPositions;
import pdfact.model.HasSemanticRole;
import pdfact.model.PdfDocument;
import pdfact.model.Point;
import pdfact.model.Character;
import pdfact.model.Element;
import pdfact.model.ElementType;
import pdfact.model.Figure;
import pdfact.model.Page;
import pdfact.model.Paragraph;
import pdfact.model.Position;
import pdfact.model.Rectangle;
import pdfact.model.SemanticRole;
import pdfact.model.Shape;
import pdfact.model.TextBlock;
import pdfact.model.TextLine;
import pdfact.model.Word;
import pdfact.pipes.visualize.PdfDrawer.PdfDrawerFactory;

/**
 * A plain implementation of {@link PdfVisualizer}.
 *
 * @author Claudius Korzen
 */
public class PlainPdfVisualizer implements PdfVisualizer {
  /**
   * The factory to create instance of {@link PdfDrawer}.
   */
  protected PdfDrawerFactory pdfDrawerFactory;

  /**
   * The element types to consider on visualizing.
   */
  protected Set<ElementType> typesFilter;

  /**
   * The semantic roles to consider on visualizing.
   */
  protected Set<SemanticRole> rolesFilter;

  // ==========================================================================
  // Constructors.

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawer}.
   */
  @AssistedInject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory) {
    this.pdfDrawerFactory = pdfDrawerFactory;
  }

  /**
   * Creates a new PDF visualizer.
   * 
   * @param pdfDrawerFactory
   *        The factory to create instances of {@link PdfDrawer}.
   * @param typesFilter
   *        The element types filter.
   * @param rolesFilter
   *        The semantic roles filter.
   */
  @AssistedInject
  public PlainPdfVisualizer(PdfDrawerFactory pdfDrawerFactory,
      @Assisted Set<ElementType> typesFilter,
      @Assisted Set<SemanticRole> rolesFilter) {
    this(pdfDrawerFactory);
    this.typesFilter = typesFilter;
    this.rolesFilter = rolesFilter;
  }

  // ==========================================================================

  @Override
  public byte[] visualize(PdfDocument pdf) throws PdfActVisualizeException {
    if (pdf != null) {
      PdfDrawer drawer = this.pdfDrawerFactory.create(pdf.getFile());

      // Visualize the textual elements.
      for (Paragraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        // Visualize the paragraph (if types filter includes paragraphs).
        if (hasRelevantElementType(paragraph)) {
          visualizeParagraph(paragraph, drawer);
          try {
            List<Position> positions = paragraph.getPositions();
            int i = 0;
            for (Position position : positions) {
              int pageNum = position.getPage().getPageNumber();
              Point point = position.getRectangle().getUpperLeft();
              drawer.drawText((i++) + " " + paragraph.getSemanticRole(), pageNum, point);
            }
            
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }

        for (Word word : paragraph.getWords()) {
          // Visualize the word (if types filter includes words).
          if (hasRelevantElementType(word)) {
            visualizeWord(word, drawer);
          }
          for (Character character : word.getCharacters()) {
            // Visualize the character (if types filter includes chars).
            if (hasRelevantElementType(character)) {
              visualizeCharacter(character, drawer);
            }
          }
        }
      }

      // Visualize the graphical elements.
      for (Page page : pdf.getPages()) {
        for (Figure figure : page.getFigures()) {
          // Visualize the figure (if types filter includes figures).
          if (hasRelevantElementType(figure)) {
            visualizeFigure(figure, drawer);
          }
        }
        for (Shape shape : page.getShapes()) {
          // Visualize the shape (if types filter includes shapes).
          if (hasRelevantElementType(shape)) {
            visualizeShape(shape, drawer);
          }
        }
      }

      try {
        return drawer.toByteArray();
      } catch (IOException e) {
        throw new PdfActVisualizeException("Error on visualization.", e);
      }
    }
    return null;
  }

  // ==========================================================================

  /**
   * Visualizes the given paragraph.
   * 
   * @param paragraph
   *        The paragraph to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeParagraph(Paragraph paragraph, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(paragraph, drawer, Color.RED);
  }

  /**
   * Visualizes the given text block.
   * 
   * @param block
   *        The text block to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeTextBlock(TextBlock block, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(block, drawer, Color.BLUE);
    try {
      String roleStr = block.getSemanticRole() != null ? block.getSemanticRole().getName() : "";
      drawer.drawText(roleStr,
          block.getPosition().getPage().getPageNumber(),
          block.getPosition().getRectangle().getUpperLeft(), Color.BLACK, 9f);
    } catch (IOException e) {

    }
  }

  /**
   * Visualizes the given text line.
   * 
   * @param line
   *        The text line to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeTextLine(TextLine line, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(line, drawer, Color.GREEN);
  }

  /**
   * Visualizes the given word.
   * 
   * @param word
   *        The word to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeWord(Word word, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(word, drawer, Color.ORANGE);
  }

  /**
   * Visualizes the given character.
   * 
   * @param character
   *        The character to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeCharacter(Character character, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(character, drawer, Color.BLACK);
  }

  /**
   * Visualizes the given figure.
   * 
   * @param figure
   *        The figure to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeFigure(Figure figure, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(figure, drawer, Color.CYAN);
  }

  /**
   * Visualizes the given shape.
   * 
   * @param shape
   *        The shape to visualize.
   * @param drawer
   *        The drawer to use.
   * 
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizeShape(Shape shape, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(shape, drawer, Color.MAGENTA);
  }

  // ==========================================================================

  /**
   * Visualizes the given PDF element using the given drawer.
   * 
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param color
   *        The color to use.
   * @throws PdfActVisualizeException
   *         If something went wrong on visualization.
   */
  protected void visualizePdfElement(Element element, PdfDrawer drawer,
      Color color) throws PdfActVisualizeException {
    if (element instanceof HasPositions) {
      HasPositions hasPositions = (HasPositions) element;
      List<Position> positions = hasPositions.getPositions();

      visualizePositions(drawer, color, positions);
    }

    if (element instanceof HasPosition) {
      HasPosition hasPosition = (HasPosition) element;
      Position position = hasPosition.getPosition();

      visualizePositions(drawer, color, position);
    }
  }

  /**
   * Visualizes the given PDF positions with the given color.
   * 
   * @param drawer
   *        The drawer to use.
   * @param color
   *        The color to use.
   * @param positions
   *        The positions to visualize.
   * @throws PdfActVisualizeException
   *         If something went wrong on visualization.
   */
  protected void visualizePositions(PdfDrawer drawer, Color color,
      Position... positions) throws PdfActVisualizeException {
    visualizePositions(drawer, color, Arrays.asList(positions));
  }

  /**
   * Visualizes the given PDF positions with the given color.
   * 
   * @param drawer
   *        The drawer to use.
   * @param color
   *        The color to use.
   * @param positions
   *        The positions to visualize.
   * @throws PdfActVisualizeException
   *         If something went wrong on visualization.
   */
  protected void visualizePositions(PdfDrawer drawer, Color color,
      List<Position> positions) throws PdfActVisualizeException {
    if (drawer == null) {
      return;
    }

    if (positions == null) {
      return;
    }

    for (Position position : positions) {
      visualizePosition(position, drawer, color);
    }
  }

  /**
   * Visualizes the given PDF position.
   * 
   * @param position
   *        The position to visualize.
   * @param drawer
   *        The drawer to use.
   * @param color
   *        The color to use.
   * @throws PdfActVisualizeException
   *         If something went wrong on visualization.
   */
  private void visualizePosition(Position position, PdfDrawer drawer,
      Color color) throws PdfActVisualizeException {
    if (position != null) {
      Page page = position.getPage();
      Rectangle rect = position.getRectangle();

      if (page != null && rect != null) {
        int pageNum = page.getPageNumber();
        try {
          drawer.drawBoundingBox(rect, pageNum, color, null, 1f);
        } catch (IOException e) {
          throw new PdfActVisualizeException(
              "Couldn't visualize the PDF document", e);
        }
      }
    }
  }

  // ==========================================================================

  @Override
  public Set<ElementType> getElementTypeFilters() {
    return this.typesFilter;
  }

  @Override
  public void setElementTypeFilters(Set<ElementType> types) {
    this.typesFilter = types;
  }

  // ==========================================================================

  @Override
  public Set<SemanticRole> getSemanticRolesFilter() {
    return this.rolesFilter;
  }

  @Override
  public void setSemanticRolesFilter(Set<SemanticRole> roles) {
    this.rolesFilter = roles;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given element matches the semantic roles
   * filter of this serializer.
   * 
   * @param element
   *        The element to check.
   * 
   * @return True, if the role of the given element matches the semantic roles
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(HasSemanticRole element) {
    if (element == null) {
      return false;
    }

    if (this.rolesFilter == null || this.rolesFilter.isEmpty()) {
      // No filter is given -> The paragraph is relevant.
      return true;
    }

    SemanticRole role = element.getSemanticRole();
    if (role == null) {
      return false;
    }

    return this.rolesFilter.contains(role);
  }

  /**
   * Checks if the type of the given PDF element matches the element type filter
   * of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the type of the given PDF element matches the element type
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantElementType(HasElementType element) {
    if (element == null) {
      return false;
    }

    if (this.typesFilter == null || this.typesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    ElementType elementType = element.getType();
    if (elementType == null) {
      return false;
    }

    return this.typesFilter.contains(elementType);
  }
}
