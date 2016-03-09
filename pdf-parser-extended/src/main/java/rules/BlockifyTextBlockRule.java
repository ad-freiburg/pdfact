package rules;

import java.util.HashSet;
import java.util.List;

import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import model.PdfArea;
import model.PdfElement;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a "block" into several text lines.
 *
 * @author Claudius Korzen
 */
public class BlockifyTextBlockRule implements BlockifyRule {
  /**
   * The characters that extend below the baseline of a font. 
   */
  protected static final HashSet<Character> DESCENDERS = new HashSet<>();
  /**
   * The characters that extend above the mean line of a font.
   */
  protected static final HashSet<Character> ASCENDERS = new HashSet<>();

  static {
    DESCENDERS.add('g');
    DESCENDERS.add('p');
    DESCENDERS.add('q');
    DESCENDERS.add('y');
    
    ASCENDERS.add('b');
    ASCENDERS.add('d');
    ASCENDERS.add('f');
    ASCENDERS.add('h');
    ASCENDERS.add('k');
  }
  
  @Override
  public HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return HorizontalSweepDirection.TOP_TO_BOTTOM;
  }
  
  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    return 0.1f;
  }
  
  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
    if (area == null || lane == null) {
      return false;
    }
    
    float mostCommonFontsize = area.getTextStatistics().getMostCommonFontsize();
    
    // Try to decide if the given lane is a valid line separator by only 
    // allowing specific elements intersecting this lane.
    List<PdfElement> overlappingElements = area.getElementsOverlapping(lane);
            
    // Inspect each intersected element.
    for (PdfElement element : overlappingElements) {
      String string = element.toString();
      float fontsize = MathUtils.round(element.getFontsize(), 1);
      
      // Ignore elements whose fontsize is significantly smaller than the most 
      // common fontsize in the given area. This is supposed to allow sub- and 
      // supscripts to intersect the lane.
      if (MathUtils.isSmaller(fontsize, mostCommonFontsize, 0.5f)) {
        continue;
      }
      
      if (string != null && !string.isEmpty()) {
        char c = string.charAt(0);
        
        // It is not allowed that characters other than descenders and ascenders
        // intersect the lane.
        if (Character.isAlphabetic(c) 
            && !DESCENDERS.contains(c) 
            && !ASCENDERS.contains(c)) {
          return false;
        }
        
        // It is not allowed that digits intersect the lane.
        if (Character.isDigit(c)) {
          return false;
        }
      }
    }
    return true;
  }
  
  @Override
  public VerticalSweepDirection getVerticalLaneSweepDirection() {
    return VerticalSweepDirection.LEFT_TO_RIGHT;
  }
  
  @Override
  public float getVerticalLaneWidth(PdfArea area) {
    return Float.MAX_VALUE;
  }
  
  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle lane) {    
    return false;
  }
}
