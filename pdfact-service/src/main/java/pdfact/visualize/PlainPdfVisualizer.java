package pdfact.visualize;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import pdfact.exception.PdfActVisualizeException;
import pdfact.models.HasElementType;
import pdfact.models.HasPosition;
import pdfact.models.HasPositions;
import pdfact.models.HasRole;
import pdfact.models.PdfCharacter;
import pdfact.models.PdfDocument;
import pdfact.models.PdfElement;
import pdfact.models.PdfElementType;
import pdfact.models.PdfFigure;
import pdfact.models.PdfPage;
import pdfact.models.PdfParagraph;
import pdfact.models.PdfPosition;
import pdfact.models.PdfRole;
import pdfact.models.PdfShape;
import pdfact.models.PdfTextBlock;
import pdfact.models.PdfTextLine;
import pdfact.models.PdfWord;
import pdfact.utils.geometric.Rectangle;
import pdfact.visualize.PdfDrawer.PdfDrawerFactory;

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
   * The element types filter.
   */
  protected Set<PdfElementType> typesFilter;

  /**
   * The semantic roles filter.
   */
  protected Set<PdfRole> rolesFilter;

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
      @Assisted Set<PdfElementType> typesFilter,
      @Assisted Set<PdfRole> rolesFilter) {
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
      for (PdfParagraph paragraph : pdf.getParagraphs()) {
        // Ignore the paragraph if its role doesn't match the roles filter.
        if (!hasRelevantRole(paragraph)) {
          continue;
        }

        // Serialize the paragraph if paragraphs match the types filter.
        if (hasRelevantElementType(paragraph)) {
          visualizeParagraph(paragraph, drawer);
        }

        for (PdfTextBlock block : paragraph.getTextBlocks()) {
          // Serialize the text block if text blocks match the types filter.
          if (hasRelevantElementType(block)) {
            visualizeTextBlock(block, drawer);
          }
          for (PdfTextLine line : block.getTextLines()) {
            // Serialize the text line if text lines match the types filter.
            if (hasRelevantElementType(line)) {
              visualizeTextLine(line, drawer);
            }
            for (PdfWord word : line.getWords()) {
              // Serialize the word if words match the types filter.
              if (hasRelevantElementType(word)) {
                visualizeWord(word, drawer);
              }
              for (PdfCharacter character : word.getCharacters()) {
                // Serialize the character if characters match the type filter.
                if (hasRelevantElementType(character)) {
                  visualizeCharacter(character, drawer);
                }
              }
            }
          }
        }
      }

      // Visualize the graphical elements.
      for (PdfPage page : pdf.getPages()) {
        for (PdfFigure figure : page.getFigures()) {
          if (hasRelevantElementType(figure)) {
            visualizeFigure(figure, drawer);
          }
        }
        for (PdfShape shape : page.getShapes()) {
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
  protected void visualizeParagraph(PdfParagraph paragraph, PdfDrawer drawer)
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
  protected void visualizeTextBlock(PdfTextBlock block, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(block, drawer, Color.BLUE);
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
  protected void visualizeTextLine(PdfTextLine line, PdfDrawer drawer)
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
  protected void visualizeWord(PdfWord word, PdfDrawer drawer)
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
  protected void visualizeCharacter(PdfCharacter character, PdfDrawer drawer)
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
  protected void visualizeFigure(PdfFigure figure, PdfDrawer drawer)
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
  protected void visualizeShape(PdfShape shape, PdfDrawer drawer)
      throws PdfActVisualizeException {
    visualizePdfElement(shape, drawer, Color.MAGENTA);
  }

  // ==========================================================================

  /**
   * Visualizes the given list of rectangles using the given drawer.
   * 
   * @param element
   *        The element to visualize.
   * @param drawer
   *        The drawer to use.
   * @param color
   *        The color to use.
   * @throws PdfActVisualizeException
   *         If the drawing failed.
   */
  protected void visualizePdfElement(PdfElement element, PdfDrawer drawer,
      Color color) throws PdfActVisualizeException {
    if (element instanceof HasPositions) {
      HasPositions hasPositions = (HasPositions) element;
      List<PdfPosition> positions = hasPositions.getPositions();

      visualizePositions(drawer, color, positions);
    }

    if (element instanceof HasPosition) {
      HasPosition hasPosition = (HasPosition) element;
      PdfPosition position = hasPosition.getPosition();

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
      PdfPosition... positions) throws PdfActVisualizeException {
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
      List<PdfPosition> positions) throws PdfActVisualizeException {
    if (drawer == null) {
      return;
    }

    if (positions == null) {
      return;
    }

    for (PdfPosition position : positions) {
      visualizePosition(position, drawer, color);
    }
  }

  /**
   * Visualizes the given position.
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
  private void visualizePosition(PdfPosition position, PdfDrawer drawer,
      Color color) throws PdfActVisualizeException {
    if (position != null) {
      PdfPage page = position.getPage();
      Rectangle rect = position.getRectangle();

      if (page != null && rect != null) {
        int pageNum = page.getPageNumber();
        try {
          drawer.drawBoundingBox(rect, pageNum, color, color, 1f);
        } catch (IOException e) {
          throw new PdfActVisualizeException(
              "Couldn't visualize the PDF document", e);
        }
      }
    }
  }

  // ==========================================================================

  @Override
  public Set<PdfElementType> getElementTypeFilters() {
    return this.typesFilter;
  }

  @Override
  public void setElementTypeFilters(Set<PdfElementType> types) {
    this.typesFilter = types;
  }

  // ==========================================================================

  @Override
  public Set<PdfRole> getElementRoleFilters() {
    return this.rolesFilter;
  }

  @Override
  public void setElementRoleFilters(Set<PdfRole> roles) {
    this.rolesFilter = roles;
  }

  // ==========================================================================

  /**
   * Checks if the semantic role of the given element matches the semantic
   * roles filter of this serializer.
   * 
   * @param element
   *        The element to check.
   * 
   * @return True, if the role of the given element matches the semantic roles
   *         filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantRole(HasRole element) {
    if (element == null) {
      return false;
    }

    if (this.rolesFilter == null || this.rolesFilter.isEmpty()) {
      // No filter is given -> The paragraph is relevant.
      return true;
    }

    PdfRole role = element.getRole();
    if (role == null) {
      return false;
    }

    return this.rolesFilter.contains(role);
  }

  /**
   * Checks if the type of the given PDF element matches the element type
   * filter of this serializer.
   * 
   * @param element
   *        The PDF element to check.
   *
   * @return True, if the type of the given PDF element matches the element
   *         type filter of this serializer, false otherwise.
   */
  protected boolean hasRelevantElementType(HasElementType element) {
    if (element == null) {
      return false;
    }

    if (this.typesFilter == null || this.typesFilter.isEmpty()) {
      // No filter is given -> The element is relevant.
      return true;
    }

    PdfElementType elementType = element.getType();
    if (elementType == null) {
      return false;
    }

    return this.typesFilter.contains(elementType);
  }
}
