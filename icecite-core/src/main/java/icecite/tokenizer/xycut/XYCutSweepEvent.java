package icecite.tokenizer.xycut;

import icecite.models.PdfCharacter;

/**
 * An utility class that is needed to represent the "events" of an character to
 * sweep in the priority queue of the XYCut algorithm. It defines (1) an
 * "event value", that is the minX or maxX value of the bounding box if the
 * sweep direction is in X direction or the minY or maxY value of the bounding
 * box if the sweep direction is in Y direction, and (2) the character itself.
 * 
 * @author Claudius Korzen
 */
public class XYCutSweepEvent {
  /**
   * The characters to sweep.
   */
  protected PdfCharacter character;

  /**
   * The event value (i.e., minX or maxX of the bounding box if the sweep
   * direction is in X direction; or minY or maxY of the bounding box if the
   * sweep direction is in Y direction.
   */
  protected float value;

  /**
   * The sweep direction.
   */
  protected XYCutSweepDirection direction;

  /**
   * Constructs a new XYCutSweepEvent.
   * 
   * @param value
   *        The value of the event point (i.e., minX, minY, maxX or maxY of the
   *        bounding box).
   * @param character
   *        The character to wrap.
   * @param direction
   *        The sweep direction.
   */
  public XYCutSweepEvent(PdfCharacter character, float value,
      XYCutSweepDirection direction) {
    this.character = character;
    this.value = value;
    this.direction = direction;
  }

  /**
   * Returns the character to sweep.
   * 
   * @return The character to sweep.
   */
  public PdfCharacter getCharacter() {
    return this.character;
  }

  /**
   * Returns the event value.
   * 
   * @return The event value.
   */
  public float getValue() {
    return this.value;
  }

  /**
   * Returns the sweep direction.
   * 
   * @return The sweep direction.
   */
  public XYCutSweepDirection getSweepDirection() {
    return this.direction;
  }

  /**
   * Returns true if this event represents a start event.
   * 
   * @return True if this event represents a start event.
   */
  public boolean isStartEvent() {
    switch (this.direction) {
      case BOTTOM_TO_TOP:
        return this.value == this.character.getBoundingBox().getMinY();
      case TOP_TO_BOTTOM:
        return this.value == this.character.getBoundingBox().getMaxY();
      case LEFT_TO_RIGHT:
        return this.value == this.character.getBoundingBox().getMinX();
      case RIGHT_TO_LEFT:
        return this.value == this.character.getBoundingBox().getMaxX();
      default:
        return false;
    }
  }

  /**
   * Returns true if this event represents an end event.
   * 
   * @return True if this event represents an end event.
   */
  public boolean isEndEvent() {
    switch (this.direction) {
      case BOTTOM_TO_TOP:
        return this.value == this.character.getBoundingBox().getMaxY();
      case TOP_TO_BOTTOM:
        return this.value == this.character.getBoundingBox().getMinY();
      case LEFT_TO_RIGHT:
        return this.value == this.character.getBoundingBox().getMaxX();
      case RIGHT_TO_LEFT:
        return this.value == this.character.getBoundingBox().getMinX();
      default:
        return false;
    }
  }
}
