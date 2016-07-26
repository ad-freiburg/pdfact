package rules;

import java.util.Collections;
import java.util.List;

import com.sun.security.auth.callback.TextCallbackHandler;

import de.freiburg.iif.counter.FloatCounter;
import de.freiburg.iif.math.MathUtils;
import de.freiburg.iif.model.Rectangle;
import model.Characters;
import model.Comparators;
import model.PdfArea;
import model.PdfCharacter;
import model.SweepDirection.HorizontalSweepDirection;
import model.SweepDirection.VerticalSweepDirection;

/**
 * The rules to blockify a line into several words.
 *
 * @author Claudius Korzen
 */
public class BlockifyTextLineRule implements BlockifyRule {
  
  @Override
  public HorizontalSweepDirection getHorizontalLaneSweepDirection() {
    return HorizontalSweepDirection.TOP_TO_BOTTOM;
  }
  
  @Override
  public float getHorizontalLaneHeight(PdfArea area) {
    return 0;
  }

  @Override
  public boolean isValidHorizontalLane(PdfArea area, Rectangle lane) {
    return false;
  }
  
  @Override
  public VerticalSweepDirection getVerticalLaneSweepDirection() {
    return VerticalSweepDirection.LEFT_TO_RIGHT;
  }
  
  @Override
  public float getVerticalLaneWidth(PdfArea area) {        
    return 0.5f * estimateWhitespaceWidth(area, 1f, Float.MAX_VALUE);
  }
  
  @Override
  public boolean isValidVerticalLane(PdfArea area, Rectangle lane) { 
//    List<PdfCharacter> overlapChars = area.getTextCharactersOverlapping(lane);
//    
//    // Lane is allowed to overlap "valid" characters.
//    for (PdfCharacter character : overlapChars) {
//      // TODO: Define valid characters.
//      if (!Characters.isPunctuationMark(character)) {
//        return false;
//      }
//    }
//    
//    return true;
    return area.getTextCharactersOverlapping(lane).isEmpty();
  }
  
  /**
   * Estimates the width of whitespace in the given area. If this distance is 
   * smaller than the given minValue, the value of minValue is returned. 
   * If there is no proper whitespace was found, the given default value is
   * returned.
   */
  public float estimateWhitespaceWidth(PdfArea area, float minValue, 
      float defaultValue) {
    List<PdfCharacter> chars = area.getTextCharacters();
    
    // Sort the characters of area by minX values to be able to obtain the
    // distance of a character to its previous and next character.
    Collections.sort(chars, new Comparators.MinXComparator());
        
    FloatCounter distanceCounter = new FloatCounter();
    
    // Iterate through each character and compute its distance to prev and next.
    for (int i = 0; i < chars.size(); i++) {
      PdfCharacter prev = i > 0 ? chars.get(i - 1) : null;
      PdfCharacter curr = chars.get(i);
      PdfCharacter next = i < chars.size() - 1 ? chars.get(i + 1) : null;
      
      if (prev != null && curr != null && next != null) {
        Rectangle prevRect = prev.getRectangle();
        Rectangle rect = curr.getRectangle();
        Rectangle nextRect = next.getRectangle();
        
        // Compute distance between prev and curr.
        float left = MathUtils.floor(rect.getMinX() - prevRect.getMaxX(), 1);
        // Consider negative distances as "0". 
        left = Math.max(left, 0);
        
        // Compute distance between curr and next.
        float right = MathUtils.floor(nextRect.getMinX() - rect.getMaxX(), 1);
        // Consider negative distances as "0".
        right = Math.max(right, 0);
        
        // If one of the distances is larger than the other, register it in the
        // counter.
        if (MathUtils.isLarger(left, right, 1f)) {
          distanceCounter.add(left);
        }
        
        if (MathUtils.isLarger(right, left, 1f)) {
          distanceCounter.add(right);
        }
      }
    }
    
    // If there was at least one whitespace found, return the most frequent 
    // width, otherwise return the default value.
    if (distanceCounter.size() > 0) {
      return Math.max(distanceCounter.getMostFrequentFloat(), minValue);
    } else {
      return defaultValue;
    }
  }
}