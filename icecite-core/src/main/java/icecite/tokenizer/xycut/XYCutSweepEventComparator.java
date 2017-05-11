package icecite.tokenizer.xycut;

import java.util.Comparator;

/**
 * A comparator to compare instances of {@link XYCutSweepEvent}.
 * 
 * @author Claudius Korzen
 */
public class XYCutSweepEventComparator implements Comparator<XYCutSweepEvent> {

  @Override
  public int compare(XYCutSweepEvent event1, XYCutSweepEvent event2) {
    // Compare the events on their values.
    if (event1.getValue() != event2.getValue()) {
      return Float.compare(event1.getValue(), event2.getValue());
    }
    // If the values are equal, prefer end events over start events.
    if (event1.isEndEvent() && event2.isStartEvent()) {
      return -1;
    }
    if (event1.isStartEvent() && event2.isEndEvent()) {
      return 1;
    }
    return 0;
  }
}
