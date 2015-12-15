package model;

import org.json.JSONObject;

/**
 * Interface to flag an object as serializable, that means that it can be
 * serialized to TSV, XML and JSON.
 *
 * @author Claudius Korzen
 *
 */
public interface Serializable {
  /**
   * Serializes the object to TSV.
   */
  public String toTsv();
  
  /**
   * Serializes the object to XML.
   */
  public String toXml(int indentLevel, int indentLength);
  
  /**
   * Serializes the object to JSON.
   */
  public JSONObject toJson();
}
