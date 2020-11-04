package pdfact.cli.pipes.visualize;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import pdfact.cli.pipes.PdfActServicePipe;
import pdfact.core.model.Character;
import pdfact.core.model.Line;
import pdfact.core.model.Page;
import pdfact.core.model.Paragraph;
import pdfact.core.model.PdfDocument;
import pdfact.core.model.Point;
import pdfact.core.model.Position;
import pdfact.core.model.Rectangle;
import pdfact.core.model.TextBlock;
import pdfact.core.model.TextLine;
import pdfact.core.model.Word;

/**
 * A class to create visualizations needed for our DFG proposal.
 *
 * @author Claudius Korzen
 */
public class DFGPdfVisualizer {
  /**
   * The visualization to create.
   */
  @Arg(dest = "v")
  protected String visalization;

  /**
   * The path to the input PDF file.
   */
  @Arg(dest = "input")
  protected String inputPath;

  /**
   * The path to the target PDF file.
   */
  @Arg(dest = "output")
  protected String outputPath;

  /**
   * The factory to create instances of PdfDrawer.
   */
  protected PdfDrawer.PdfDrawerFactory pdfDrawerFactory;

  /**
   * The factory to create instances of PdfActServicePipe.
   */
  protected PdfActServicePipe.PdfActServicePipeFactory serviceFactory;

  // ==============================================================================================

  /**
   * Runs this visualizer.
   *
   * @param args The command line arguments.
   *
   * @throws Exception If something went wrong.
   */
  public void run(String[] args) throws Exception {
    ArgumentParser parser = ArgumentParsers.newFor("DFGPdfVisualizer").build();
    parser.description("Creates visualizations needed for our DFG proposal.");

    parser.addArgument("inputPath").dest("input").required(true).metavar("<input-path>")
        .help("Defines the path to the input PDF file.");

    parser.addArgument("outputPath").dest("output").required(true).metavar("<output-path>")
        .help("Defines the path to the target PDF file.");

    parser.parseArgs(args, this);

    PdfDocument pdf = new PdfDocument(this.inputPath);
    PdfDrawer drawer = this.pdfDrawerFactory.create(pdf.getFile());

    // Create a service pipe.
    this.serviceFactory.create().execute(pdf);

    // Create visualizations.
    // createWordBoundariesVisualization(pdf, drawer);
    // createLigaturesVisualization(pdf, drawer);
    // createDiacriticsVisualization(pdf, drawer);
    // createHyphenationVisualization(pdf, drawer);
    // createBlockBoundariesVisualization(pdf, drawer);
    // createReadingOrderVisualization(pdf, drawer);
    // createSemanticRoleVisualization(pdf, drawer);

    // createSegmentationVisualization(pdf, drawer);
    createLineSegmentationVisualization(pdf, drawer);

    Path output = Paths.get(outputPath);
    Files.write(output, drawer.toByteArray());
  }

  // ==============================================================================================

  /**
   * Creates a visualization as needed for the challenge "word boundaries".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createWordBoundariesVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highlightedCharacterColor = Color.BLUE;
    float highlightedCharacterThickness = 0.1f;

    Page page = pdf.getPages().get(1);
    for (int i = 0; i < 9; i++) {
      Character character = page.getCharacters().get(i);
      Rectangle rect = character.getPosition().getRectangle();
      int pageNum = page.getPageNumber();

      Color characterColor = Color.LIGHT_GRAY;
      float characterThickness = 0.1f;

      if (i == 0) { // s
        rect.setMinX(rect.getMinX() + 0.7f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 1) { // e
        rect.setMinX(rect.getMinX() + 0.7f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 2) { // r
        rect.setMinX(rect.getMinX() + 0.7f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 3) { // i
        rect.setMinX(rect.getMinX() + 1f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 4) { // f
        rect.setMaxX(rect.getMaxX() - 0.2f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 5) { // f
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 6) { // o
        rect.setMinX(rect.getMinX() + 0.5f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 7) { // n
        rect.setMinX(rect.getMinX() + 0.7f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      if (i == 8) { // t
        rect.setMinX(rect.getMinX() + 0.8f);
        characterColor = highlightedCharacterColor;
        characterThickness = highlightedCharacterThickness;
      }

      drawer.drawRectangle(rect, pageNum, characterColor, null, characterThickness);
    }
  }

  // ==============================================================================================

  /**
   * Creates a visualization for the challenge "ligatures".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createLigaturesVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highColor = Color.BLUE;
    float highThickness = 0.1f;

    Page page = pdf.getPages().get(1);
    for (int i = 9; i < 16; i++) {
      Character character = page.getCharacters().get(i);
      Rectangle rect = character.getPosition().getRectangle();
      int pageNum = page.getPageNumber();

      Color color = Color.LIGHT_GRAY;
      float thickness = 0.1f;

      if (i == 9) { // e
        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.2f);
      }

      if (i == 10) { // ffi
        rect.setMinX(rect.getMinX() + 0.2f);
        rect.setMaxX(rect.getMaxX() - 0.1f);
        color = highColor;
        thickness = highThickness;
      }

      if (i == 11) { // c
        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.1f);
      }

      if (i == 13) { // e
        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.2f);
      }

      if (i == 14) { // n
        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.1f);
      }

      drawer.drawRectangle(rect, pageNum, color, null, thickness);
    }
  }

  // ==============================================================================================

  /**
   * Creates a visualization for the challenge "ligatures".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createDiacriticsVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highColor = Color.BLUE;
    float highThickness = 0.1f;

    Page page = pdf.getPages().get(1);
    for (int i = 16; i < 27; i++) {
      Character character = page.getCharacters().get(i);
      Rectangle rect = character.getPosition().getRectangle();
      int pageNum = page.getPageNumber();

      Color color = Color.LIGHT_GRAY;
      float thickness = 0.1f;

      if (i == 16) { // c
        rect.setMinX(rect.getMinX() + 0.3f);
      }

      if (i == 17) { // r
        rect.setMinX(rect.getMinX() + 0.3f);
      }

      if (i == 18) { // é
        color = highColor;
        thickness = highThickness;

        // draw rectangle for the diacritic.
        Rectangle diacritic = new Rectangle(rect);
        diacritic.setMinX(diacritic.getMinX() + 0.3f);
        diacritic.setMaxX(diacritic.getMaxX() - 0.2f);
        diacritic.setMinY(diacritic.getMinY() + 4.5f);
        drawer.drawRectangle(diacritic, pageNum, color, null, thickness);

        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.2f);
        rect.setMaxY(rect.getMaxY() - 2f);
        drawer.drawRectangle(rect, pageNum, color, null, thickness);

        continue;
      }

      if (i == 19) { // m
        rect.setMinX(rect.getMinX() + 0.3f);
      }

      if (i == 20) { // e
        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.2f);
      }

      if (i == 21) { // b
        rect.setMaxX(rect.getMaxX() - 0.2f);
      }

      if (i == 22) { // r
        rect.setMinX(rect.getMinX() + 0.3f);
      }

      if (i == 23) { // ú
        color = highColor;
        thickness = highThickness;

        // draw rectangle for the diacritic.
        Rectangle diacritic = new Rectangle(rect);
        diacritic.setMaxX(diacritic.getMaxX() - 0.3f);
        diacritic.setMinY(diacritic.getMinY() + 4.5f);
        drawer.drawRectangle(diacritic, pageNum, color, null, thickness);

        rect.setMaxX(rect.getMaxX() - 0.1f);
        rect.setMaxY(rect.getMaxY() - 2f);
        drawer.drawRectangle(rect, pageNum, color, null, thickness);
        continue;
      }

      if (i == 24) { // l
        rect.setMinX(rect.getMinX() + 0.1f);
        rect.setMaxX(rect.getMaxX() - 0.1f);
      }

      if (i == 25) { // è
        color = highColor;
        thickness = highThickness;

        // draw rectangle for the diacritic.
        Rectangle diacritic = new Rectangle(rect);
        diacritic.setMinX(diacritic.getMinX() + 0.3f);
        diacritic.setMaxX(diacritic.getMaxX() - 0.2f);
        diacritic.setMinY(diacritic.getMinY() + 4.5f);
        drawer.drawRectangle(diacritic, pageNum, color, null, thickness);

        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.2f);
        rect.setMaxY(rect.getMaxY() - 2f);
        drawer.drawRectangle(rect, pageNum, color, null, thickness);
        continue;
      }

      if (i == 26) { // e
        rect.setMinX(rect.getMinX() + 0.3f);
        rect.setMaxX(rect.getMaxX() - 0.2f);
      }

      drawer.drawRectangle(rect, pageNum, color, null, thickness);
    }
  }

  // ==============================================================================================

  /**
   * Creates a visualization for the challenge "hyphenation".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createHyphenationVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highColor = Color.BLUE;
    float highThickness = 0.3f;

    Paragraph paragraph = pdf.getParagraphs().get(17);
    for (int i = 64; i < 91; i++) {
      Word word = paragraph.getWords().get(i);

      Color color = Color.LIGHT_GRAY;
      float wordThickness = 0.1f;

      if (i == 75 || i == 86) {
        color = highColor;
        wordThickness = highThickness;
      }

      for (Position pos : word.getPositions()) {
        Rectangle rect = pos.getRectangle();
        int pageNum = pos.getPageNumber();

        drawer.drawRectangle(rect, pageNum, color, null, wordThickness);
      }
    }
  }

  // ==============================================================================================

  /**
   * Creates a visualization for the challenge "block boundaries".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createBlockBoundariesVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highColor = Color.BLUE;

    Color color = Color.LIGHT_GRAY;
    float thickness = 0.1f;

    Page page = pdf.getPages().get(1);

    Rectangle rect = page.getTextBlocks().get(7).getPosition().getRectangle();
    drawer.drawRectangle(rect, page.getPageNumber(), highColor, null, thickness);

    rect = page.getTextBlocks().get(8).getPosition().getRectangle();
    drawer.drawRectangle(rect, page.getPageNumber(), color, null, thickness);

    // Summarize the rects of the tree to one rect.
    Rectangle treeRect = page.getTextBlocks().get(9).getPosition().getRectangle();
    // for (int i = 10; i < 21; i++) {
    // rect = page.getTextBlocks().get(i).getPosition().getRectangle();
    // treeRect.extend(rect);
    // }
    drawer.drawRectangle(treeRect, page.getPageNumber(), color, null, thickness);

    rect = page.getTextBlocks().get(10).getPosition().getRectangle();
    drawer.drawRectangle(rect, page.getPageNumber(), highColor, null, thickness);

    rect = page.getTextBlocks().get(11).getPosition().getRectangle();
    drawer.drawRectangle(rect, page.getPageNumber(), color, null, thickness);

    // rect = page.getTextBlocks().get(23).getPosition().getRectangle();
    // drawer.drawRectangle(rect, page.getPageNumber(), color, null, thickness);
  }

  // ==============================================================================================

  /**
   * Creates a visualization for the challenge "reading order".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createReadingOrderVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highColor = Color.BLUE;
    float highThickness = 2f;

    Color color = Color.LIGHT_GRAY;
    float thickness = 0.1f;

    Page page = pdf.getPages().get(0);

    for (TextBlock block : page.getTextBlocks()) {
      Rectangle rect = block.getPosition().getRectangle();
      int pageNum = block.getPosition().getPageNumber();
      drawer.drawRectangle(rect, pageNum, color, null, thickness);
    }

    // For reading order.
    Point r0 = page.getTextBlocks().get(0).getPosition().getRectangle().getMidpoint();
    Point r1 = page.getTextBlocks().get(1).getPosition().getRectangle().getMidpoint();
    Point r2 = page.getTextBlocks().get(2).getPosition().getRectangle().getMidpoint();
    Point r3 = page.getTextBlocks().get(13).getPosition().getRectangle().getMidpoint();
    Point r4 = page.getTextBlocks().get(14).getPosition().getRectangle().getMidpoint();
    Point r5 = page.getTextBlocks().get(3).getPosition().getRectangle().getMidpoint();
    Point r6 = page.getTextBlocks().get(4).getPosition().getRectangle().getMidpoint();
    Point r7 = page.getTextBlocks().get(5).getPosition().getRectangle().getMidpoint();
    Point r8 = page.getTextBlocks().get(8).getPosition().getRectangle().getMidpoint();
    Point r9 = page.getTextBlocks().get(15).getPosition().getRectangle().getMidpoint();
    Point r10 = page.getTextBlocks().get(16).getPosition().getRectangle().getMidpoint();
    Point r11 = page.getTextBlocks().get(17).getPosition().getRectangle().getMidpoint();

    int pageNum = page.getPageNumber();
    try {
      drawer.drawLine(new Line(r0, r1), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r0.getX() - 5f, r0.getY() - 5f, r0.getX() + 5f, r0.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("A", pageNum, new Point(r0.getX() - 2.75f, r0.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r1, r2), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r1.getX() - 5f, r1.getY() - 5f, r1.getX() + 5f, r1.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("B", pageNum, new Point(r1.getX() - 2.75f, r1.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r2, r3), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r2.getX() - 5f, r2.getY() - 5f, r2.getX() + 5f, r2.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("C", pageNum, new Point(r2.getX() - 2.75f, r2.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r3, r4), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r3.getX() - 5f, r3.getY() - 5f, r3.getX() + 5f, r3.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("D", pageNum, new Point(r3.getX() - 2.75f, r3.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r4, r5), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r4.getX() - 5f, r4.getY() - 5f, r4.getX() + 5f, r4.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("E", pageNum, new Point(r4.getX() - 2.75f, r4.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r5, r6), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r5.getX() - 5f, r5.getY() - 5f, r5.getX() + 5f, r5.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("F", pageNum, new Point(r5.getX() - 2.75f, r5.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r6, r7), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r6.getX() - 5f, r6.getY() - 5f, r6.getX() + 5f, r6.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawText("G", pageNum, new Point(r6.getX() - 3.25f, r6.getY() - 3f), Color.WHITE, 8f);
      drawer.drawLine(new Line(r8, r9), pageNum, highColor, highThickness);
      drawer.drawRectangle(new Rectangle(r9.getX() - 5f, r9.getY() - 5f, r9.getX() + 5f, r9.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawLine(new Line(r9, r10), pageNum, highColor, highThickness);
      drawer.drawText("O", pageNum, new Point(r9.getX() - 3.25f, r9.getY() - 3f), Color.WHITE, 8f);
      drawer.drawRectangle(new Rectangle(r10.getX() - 5f, r10.getY() - 5f, r10.getX() + 5f, r10.getY() + 5f), pageNum,
          highColor, highColor);
      drawer.drawLine(new Line(r10, r11), pageNum, highColor, highThickness);
      drawer.drawText("P", pageNum, new Point(r10.getX() - 2.75f, r10.getY() - 3f), Color.WHITE, 8f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // ==============================================================================================

  /**
   * Creates a visualization for the challenge "semantic role".
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createSemanticRoleVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Color highColor = Color.BLUE;
    float highThickness = 1f;

    Color color = Color.LIGHT_GRAY;
    float fontsize = 10;

    Page page = pdf.getPages().get(2);

    for (TextBlock block : page.getTextBlocks()) {
      Rectangle rect = block.getPosition().getRectangle();
      int pageNum = block.getPosition().getPageNumber();

      drawer.drawRectangle(rect, pageNum, color, null, highThickness);
    }

    // Title.
    TextBlock block = page.getTextBlocks().get(0);
    Rectangle rect = block.getPosition().getRectangle();
    int pageNum = block.getPosition().getPageNumber();
    rect.setMinX(rect.getMinX() - 20f);
    rect.setMinY(rect.getMinY() + 2f);
    drawer.drawText("title", pageNum, rect.getLowerLeft(), highColor, fontsize);

    // Author 1.
    block = page.getTextBlocks().get(1);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMinX(rect.getMinX() - 62f);
    drawer.drawText("author name", pageNum, rect.getLowerLeft(), highColor, fontsize);

    block = page.getTextBlocks().get(2);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMinX(rect.getMinX() - 75f);
    rect.setMinY(rect.getMinY() + 14f);
    drawer.drawText("author affiliation", pageNum, rect.getLowerLeft(), highColor, fontsize);

    // Author 2.
    block = page.getTextBlocks().get(14);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMaxX(rect.getMaxX() + 5f);
    drawer.drawText("author name", pageNum, rect.getLowerRight(), highColor, fontsize);

    block = page.getTextBlocks().get(15);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMaxX(rect.getMaxX() + 5f);
    rect.setMinY(rect.getMinY() + 14f);
    drawer.drawText("author affiliation", pageNum, rect.getLowerRight(), highColor, fontsize);

    // Abstract heading.
    block = page.getTextBlocks().get(3);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMinX(rect.getMinX() - 40f);
    drawer.drawText("heading", pageNum, rect.getLowerLeft(), highColor, fontsize);

    // Abstract paragraph.
    block = page.getTextBlocks().get(4);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMinX(rect.getMinX() - 50f);
    rect.setMinY(rect.getMinY() + 35f);
    drawer.drawText("paragraph", pageNum, rect.getLowerLeft(), highColor, fontsize);

    // Abstract paragraph.
    block = page.getTextBlocks().get(5);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMinX(rect.getMinX() - 50f);
    rect.setMinY(rect.getMinY() + 57f);
    drawer.drawText("paragraph", pageNum, rect.getLowerLeft(), highColor, fontsize);

    // Heading.
    block = page.getTextBlocks().get(16);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMaxX(rect.getMaxX() + 4f);
    rect.setMinY(rect.getMinY() + 6f);
    drawer.drawText("paragraph", pageNum, rect.getLowerRight(), highColor, fontsize);

    // Paragraph.
    block = page.getTextBlocks().get(17);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMaxX(rect.getMaxX() + 4f);
    rect.setMinY(rect.getMinY() + 20f);
    drawer.drawText("paragraph", pageNum, rect.getLowerRight(), highColor, fontsize);

    // Paragraph.
    block = page.getTextBlocks().get(18);
    rect = block.getPosition().getRectangle();
    pageNum = block.getPosition().getPageNumber();
    rect.setMaxX(rect.getMaxX() + 4f);
    rect.setMinY(rect.getMinY() + 18f);
    drawer.drawText("paragraph", pageNum, rect.getLowerRight(), highColor, fontsize);
  }

  /**
   * Creates a visualization of page segmentation.
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createSegmentationVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    // First horizontal line.
    Point line1Point1 = new Point(40, 688);
    Point line1Point2 = new Point(570, 688);
    drawer.drawLine(new Line(line1Point1, line1Point2), 1, Color.BLUE, 4f);

    // Second horizontal line.
    Point line2Point1 = new Point(40, 631);
    Point line2Point2 = new Point(570, 631);
    drawer.drawLine(new Line(line2Point1, line2Point2), 1, Color.BLUE, 4f);

    // First vertical line.
    Point line3Point1 = new Point(306, 641);
    Point line3Point2 = new Point(306, 678);
    drawer.drawLine(new Line(line3Point1, line3Point2), 1, Color.BLUE, 4f);

    // Second vertical line.
    Point line4Point1 = new Point(306, 621);
    Point line4Point2 = new Point(306, 75);
    drawer.drawLine(new Line(line4Point1, line4Point2), 1, Color.BLUE, 4f);
  }

  /**
   * Creates a visualization how to segement a set of characters int lines.
   *
   * @param pdf    The PDF file to process.
   * @param drawer The drawer to use to create the visualization.
   *
   * @throws IOException If something went wrong.
   */
  protected void createLineSegmentationVisualization(PdfDocument pdf, PdfDrawer drawer) throws IOException {
    Page firstPage = pdf.getFirstPage();

    for (Character character : firstPage.getCharacters()) {
      Rectangle rect = character.getPosition().getRectangle();
      drawer.drawRectangle(rect, character.getPosition().getPageNumber(), Color.GRAY, null, 0.1f);
    }

    for (TextLine line : firstPage.getTextLines()) {
      System.out.println(line.getText());
      System.out.println(line.getPosition().getRectangle());
    }

    Rectangle rect = new Rectangle(75.8, 697.1, 536.1, 709.3);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);
    rect = new Rectangle(151.9, 674.5, 470.0, 683.0);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);
    rect = new Rectangle(138.3, 660.3, 474.7, 669.6);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);
    rect = new Rectangle(132.0, 648.3, 481, 657.7);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);
    rect = new Rectangle(134.7, 636.4, 484, 645.7);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);
    rect = new Rectangle(52.5, 617.8, 496.3, 626.7);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 2f);
    rect = new Rectangle(53.8, 602.9, 559.7, 611.3);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);
    rect = new Rectangle(53.8, 592.0, 454.3, 600.4);
    drawer.drawRectangle(rect, 1, Color.BLUE, null, 1f);

  }

  // ==============================================================================================

  /**
   * The main method.
   *
   * @param args The command line arguments.
   *
   * @throws Exception If something went wrong.
   */
  public static void main(String[] args) throws Exception {
    new DFGPdfVisualizer().run(args);
  }
}
