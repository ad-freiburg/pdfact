package model;

import java.util.HashSet;

/**
 * Collection of some helper methods about characters.
 * 
 * @author Claudius Korzen
 */
public class Characters {
  /**
   * The punctuation marks.
   */
  public static final HashSet<Character> PUNCTUATION_MARKS = new HashSet<>();
  /**
   * The characters that aren't aligned to the baseline of a text line.
   */
  public static final HashSet<Character> DESCENDERS = new HashSet<>();
  /**
   * The characters that aren't aligned to the meanline of a text line.
   */
  public static final HashSet<Character> ASCENDERS = new HashSet<>();
  /**
   * The characters that are aligned to the baseline of a text line.
   */
  public static final HashSet<Character> BASELINE_CHARACTERS = new HashSet<>();
  /**
   * The characters that are aligned to the baseline of a text line.
   */
  public static final HashSet<Character> MEANLINE_CHARACTERS = new HashSet<>();
  /**
   * The characters that represents a cap of a character.
   */
  public static final HashSet<Character> CAPS = new HashSet<>();
  
  static {
    PUNCTUATION_MARKS.add('.');
    PUNCTUATION_MARKS.add('?');
    PUNCTUATION_MARKS.add('!');
    PUNCTUATION_MARKS.add(':');
    PUNCTUATION_MARKS.add(';');
    PUNCTUATION_MARKS.add('-');
    PUNCTUATION_MARKS.add('—');
    PUNCTUATION_MARKS.add(',');
    PUNCTUATION_MARKS.add('\'');
    PUNCTUATION_MARKS.add('"');
    PUNCTUATION_MARKS.add('`');
    PUNCTUATION_MARKS.add('´');
    PUNCTUATION_MARKS.add('”');
    PUNCTUATION_MARKS.add('’');
    
    DESCENDERS.add('g');
    DESCENDERS.add('j');
    DESCENDERS.add('p');
    DESCENDERS.add('q');
    DESCENDERS.add('y');

    ASCENDERS.add('b');
    ASCENDERS.add('d');
    ASCENDERS.add('f');
    ASCENDERS.add('h');
    ASCENDERS.add('i');
    ASCENDERS.add('j');
    ASCENDERS.add('k');
    ASCENDERS.add('l');
    ASCENDERS.add('t');
    ASCENDERS.add('β');

    BASELINE_CHARACTERS.add('A');
    BASELINE_CHARACTERS.add('B');
    BASELINE_CHARACTERS.add('C');
    BASELINE_CHARACTERS.add('D');
    BASELINE_CHARACTERS.add('E');
    BASELINE_CHARACTERS.add('F');
    BASELINE_CHARACTERS.add('G');
    BASELINE_CHARACTERS.add('H');
    BASELINE_CHARACTERS.add('I');
    BASELINE_CHARACTERS.add('K');
    BASELINE_CHARACTERS.add('L');
    BASELINE_CHARACTERS.add('M');
    BASELINE_CHARACTERS.add('N');
    BASELINE_CHARACTERS.add('O');
    BASELINE_CHARACTERS.add('P');
    BASELINE_CHARACTERS.add('R');
    BASELINE_CHARACTERS.add('S');
    BASELINE_CHARACTERS.add('T');
    BASELINE_CHARACTERS.add('U');
    BASELINE_CHARACTERS.add('V');
    BASELINE_CHARACTERS.add('W');
    BASELINE_CHARACTERS.add('X');
    BASELINE_CHARACTERS.add('Y');
    BASELINE_CHARACTERS.add('Z');
    BASELINE_CHARACTERS.add('a');
    BASELINE_CHARACTERS.add('b');
    BASELINE_CHARACTERS.add('c');
    BASELINE_CHARACTERS.add('d');
    BASELINE_CHARACTERS.add('e');
    BASELINE_CHARACTERS.add('f');
    BASELINE_CHARACTERS.add('h');
    BASELINE_CHARACTERS.add('i');
    BASELINE_CHARACTERS.add('k');
    BASELINE_CHARACTERS.add('l');
    BASELINE_CHARACTERS.add('m');
    BASELINE_CHARACTERS.add('n');
    BASELINE_CHARACTERS.add('o');
    BASELINE_CHARACTERS.add('r');
    BASELINE_CHARACTERS.add('s');
    BASELINE_CHARACTERS.add('t');
    BASELINE_CHARACTERS.add('u');
    BASELINE_CHARACTERS.add('v');
    BASELINE_CHARACTERS.add('w');
    BASELINE_CHARACTERS.add('x');
    BASELINE_CHARACTERS.add('z');
    BASELINE_CHARACTERS.add('1');
    BASELINE_CHARACTERS.add('2');
    BASELINE_CHARACTERS.add('3');
    BASELINE_CHARACTERS.add('4');
    BASELINE_CHARACTERS.add('5');
    BASELINE_CHARACTERS.add('6');
    BASELINE_CHARACTERS.add('7');
    BASELINE_CHARACTERS.add('8');
    BASELINE_CHARACTERS.add('9');
    BASELINE_CHARACTERS.add('0');
    
    MEANLINE_CHARACTERS.add('a');
    MEANLINE_CHARACTERS.add('c');
    MEANLINE_CHARACTERS.add('e');
    MEANLINE_CHARACTERS.add('g');
    MEANLINE_CHARACTERS.add('m');
    MEANLINE_CHARACTERS.add('n');
    MEANLINE_CHARACTERS.add('o');
    MEANLINE_CHARACTERS.add('p');
    MEANLINE_CHARACTERS.add('q');
    MEANLINE_CHARACTERS.add('r');
    MEANLINE_CHARACTERS.add('s');
    MEANLINE_CHARACTERS.add('u');
    MEANLINE_CHARACTERS.add('v');
    MEANLINE_CHARACTERS.add('w');
    MEANLINE_CHARACTERS.add('x');
    MEANLINE_CHARACTERS.add('y');
    MEANLINE_CHARACTERS.add('z');
    
    CAPS.add('→');
    CAPS.add('~');
  }
  
  public static boolean isLetter(PdfCharacter character) {
    return isLetter(character.getUnicode());
  }
  
  public static boolean isLetter(String character) {
    return isLetter(character.charAt(0));
  }
  
  public static boolean isLetter(char character) {
    return Character.isLetter(character);
  }
  
  public static boolean isLatinLetter(PdfCharacter character) {
    return isLatinLetter(character.getUnicode());
  }
  
  public static boolean isLatinLetter(String character) {
    return isLatinLetter(character.charAt(0));
  }
  
  public static boolean isLatinLetter(char ch) {
    return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
  }
  
  public static boolean isLetterOrDigit(PdfCharacter character) {
    return isLetterOrDigit(character.getUnicode());
  }
  
  public static boolean isLetterOrDigit(String character) {
    return isLetterOrDigit(character.charAt(0));
  }
  
  public static boolean isLetterOrDigit(char character) {
    return Character.isLetterOrDigit(character);
  }
  
  public static boolean isLatinLetterOrDigit(PdfCharacter character) {
    return isLatinLetterOrDigit(character.getUnicode());
  }
  
  public static boolean isLatinLetterOrDigit(String character) {
    return isLatinLetterOrDigit(character.charAt(0));
  }
  
  public static boolean isLatinLetterOrDigit(char character) {
    return isLatinLetter(character) || Character.isDigit(character);
  }
  
  public static boolean isPunctuationMark(PdfCharacter character) {
    return isPunctuationMark(character.getUnicode());
  }
  
  public static boolean isPunctuationMark(String character) {
    return isPunctuationMark(character.charAt(0));
  }
  
  public static boolean isPunctuationMark(char character) {
    return PUNCTUATION_MARKS.contains(character);
  }
  
  public static boolean isDescender(PdfCharacter character) {
    return isDescender(character.getUnicode());
  }
  
  public static boolean isDescender(String character) {
    return isDescender(character.charAt(0));
  }
  
  public static boolean isDescender(char character) {
    return DESCENDERS.contains(character);
  }
  
  public static boolean isAscender(PdfCharacter character) {
    return isAscender(character.getUnicode());
  }
  
  public static boolean isAscender(String character) {
    return isAscender(character.charAt(0));
  }
  
  public static boolean isAscender(char character) {
    return Character.isUpperCase(character) || Character.isDigit(character) 
        || ASCENDERS.contains(character);
  }
  
  public static boolean isMeanlineCharacter(PdfCharacter character) {
    return isMeanlineCharacter(character.getUnicode());
  }
  
  public static boolean isMeanlineCharacter(String character) {
    return isMeanlineCharacter(character.charAt(0));
  }
  
  public static boolean isMeanlineCharacter(char character) {
    return MEANLINE_CHARACTERS.contains(character);
  }
  
  public static boolean isBaselineCharacter(PdfCharacter character) {
    return isBaselineCharacter(character.getUnicode());
  }
  
  public static boolean isBaselineCharacter(String character) {
    return isBaselineCharacter(character.charAt(0));
  }
  
  public static boolean isBaselineCharacter(char character) {
    return BASELINE_CHARACTERS.contains(character);
  }
  
  public static boolean isCapCharacter(PdfCharacter character) {
    return isCapCharacter(character.getUnicode());
  }
  
  public static boolean isCapCharacter(String character) {
    return isCapCharacter(character.charAt(0));
  }
  
  public static boolean isCapCharacter(char character) {
    return CAPS.contains(character);
  }
}
