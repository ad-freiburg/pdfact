package icecite.utils.character;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import icecite.models.PdfCharacter;

/**
 * A collection of utility methods that deal with PdfCharacter objects.
 * 
 * @author Claudius Korzen
 */
public class PdfCharacterUtils {
  /**
   * The punctuation marks that are aligned to the base line.
   */
  static final TCharSet BASELINE_PUNCTUATION_MARKS = new TCharHashSet();

  /**
   * The punctuation marks that are aligned to the mean line.
   */
  static final TCharSet MEANLINE_PUNCTUATION_MARKS = new TCharHashSet();

  /**
   * The characters that aren't aligned to the baseline of a text line.
   */
  static final TCharSet DESCENDERS = new TCharHashSet();

  /**
   * The characters that aren't aligned to the meanline of a text line.
   */
  static final TCharSet ASCENDERS = new TCharHashSet();

  /**
   * The characters that are aligned to the baseline of a text line.
   */
  static final TCharSet BASELINE_CHARACTERS = new TCharHashSet();

  /**
   * The characters that are aligned to the baseline of a text line.
   */
  static final TCharSet MEANLINE_CHARACTERS = new TCharHashSet();

  /**
   * The math character.
   */
  static final Set<String> MATH_SYMBOLS = new HashSet<>();

  /**
   * The math operators (all symbols that must be surrounded by white spaces).
   */
  static final Set<String> MATH_OPERATORS = new HashSet<>();

  static {
    BASELINE_PUNCTUATION_MARKS.add('.');
    BASELINE_PUNCTUATION_MARKS.add('?');
    BASELINE_PUNCTUATION_MARKS.add('!');
    BASELINE_PUNCTUATION_MARKS.add(':');
    BASELINE_PUNCTUATION_MARKS.add(';');
    // PUNCTUATION_MARKS.add('-');
    // PUNCTUATION_MARKS.add('—');
    BASELINE_PUNCTUATION_MARKS.add(',');
    MEANLINE_PUNCTUATION_MARKS.add('\'');
    MEANLINE_PUNCTUATION_MARKS.add('"');
    MEANLINE_PUNCTUATION_MARKS.add('“');
    MEANLINE_PUNCTUATION_MARKS.add('”');
    MEANLINE_PUNCTUATION_MARKS.add('`');
    MEANLINE_PUNCTUATION_MARKS.add('´');
    MEANLINE_PUNCTUATION_MARKS.add('’');

    DESCENDERS.add('g');
    DESCENDERS.add('j');
    DESCENDERS.add('p');
    DESCENDERS.add('q');
    DESCENDERS.add('y');
    DESCENDERS.add('f'); // If f is italic.
    DESCENDERS.add('i'); // In combination with f (ligature fi).
    DESCENDERS.add('Q');
    DESCENDERS.add('J');

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

    // Basic symbols
    MATH_OPERATORS.add("+");
    MATH_OPERATORS.add("-");
    MATH_OPERATORS.add("−");
    MATH_OPERATORS.add("±");
    MATH_OPERATORS.add("∓");
    MATH_OPERATORS.add("×");
    MATH_OPERATORS.add("⋅");
    MATH_OPERATORS.add("·");
    MATH_OPERATORS.add("÷");
    MATH_OPERATORS.add("/");
    MATH_OPERATORS.add("⁄");
    MATH_SYMBOLS.add("√");
    MATH_SYMBOLS.add("∑");
    MATH_SYMBOLS.add("∫");
    MATH_SYMBOLS.add("∮");
    MATH_OPERATORS.add("∴");
    MATH_OPERATORS.add("∵");
    MATH_SYMBOLS.add("¬");
    MATH_SYMBOLS.add("˜");
    MATH_SYMBOLS.add("∝");
    MATH_OPERATORS.add("∞");
    MATH_SYMBOLS.add("■");
    MATH_SYMBOLS.add("□");
    MATH_SYMBOLS.add("∎");
    MATH_SYMBOLS.add("▮");
    MATH_SYMBOLS.add("‣");

    // Digits
    MATH_SYMBOLS.add("1");
    MATH_SYMBOLS.add("2");
    MATH_SYMBOLS.add("3");
    MATH_SYMBOLS.add("4");
    MATH_SYMBOLS.add("5");
    MATH_SYMBOLS.add("6");
    MATH_SYMBOLS.add("7");
    MATH_SYMBOLS.add("8");
    MATH_SYMBOLS.add("9");
    MATH_SYMBOLS.add("0");

    // Symbols based on equality.
    MATH_OPERATORS.add("=");
    MATH_OPERATORS.add("≠");
    MATH_OPERATORS.add("≈");
    MATH_OPERATORS.add("~");
    MATH_OPERATORS.add("≡");
    MATH_OPERATORS.add("≜");
    MATH_OPERATORS.add("≝");
    MATH_OPERATORS.add("≐");
    MATH_OPERATORS.add("≅");
    MATH_OPERATORS.add("≡");
    MATH_OPERATORS.add("⇔");
    MATH_OPERATORS.add("↔");

    // Symbols that point left or right
    MATH_OPERATORS.add("<");
    MATH_OPERATORS.add(">");
    MATH_OPERATORS.add("≪");
    MATH_OPERATORS.add("≫");
    MATH_OPERATORS.add("≤");
    MATH_OPERATORS.add("≥");
    MATH_OPERATORS.add("≦");
    MATH_OPERATORS.add("≧");
    MATH_OPERATORS.add("≺");
    MATH_OPERATORS.add("≻");
    MATH_OPERATORS.add("◅");
    MATH_OPERATORS.add("▻");
    MATH_OPERATORS.add("⇒");
    MATH_OPERATORS.add("→");
    MATH_OPERATORS.add("⊃");
    MATH_OPERATORS.add("⊆");
    MATH_OPERATORS.add("⊂");
    MATH_OPERATORS.add("⊇");
    MATH_OPERATORS.add("⊃");
    MATH_OPERATORS.add("→");
    MATH_OPERATORS.add("↦");
    MATH_OPERATORS.add("⊧");
    MATH_OPERATORS.add("⊢");

    // Brackets
    MATH_SYMBOLS.add("{");
    MATH_SYMBOLS.add("}");
    MATH_SYMBOLS.add("⌊");
    MATH_SYMBOLS.add("⌋");
    MATH_SYMBOLS.add("⌈");
    MATH_SYMBOLS.add("⌉");
    MATH_SYMBOLS.add("[");
    MATH_SYMBOLS.add("]");
    MATH_SYMBOLS.add("(");
    MATH_SYMBOLS.add(")");
    MATH_SYMBOLS.add("⟨");
    MATH_SYMBOLS.add("⟩");
    MATH_SYMBOLS.add("|");

    // Other non-letter symbols
    MATH_OPERATORS.add("*");
    MATH_OPERATORS.add("∝");
    MATH_OPERATORS.add("∖");
    MATH_OPERATORS.add("∤");
    MATH_OPERATORS.add("∥");
    MATH_OPERATORS.add("∦");
    MATH_OPERATORS.add("⋕");
    MATH_OPERATORS.add("#");
    MATH_OPERATORS.add("≀");
    MATH_OPERATORS.add("↯");
    MATH_OPERATORS.add("※");
    MATH_OPERATORS.add("⊕");
    MATH_OPERATORS.add("⊻");
    MATH_OPERATORS.add("□");

    // Letter-based-symbols
    MATH_OPERATORS.add("•");
    MATH_SYMBOLS.add("∀");
    MATH_SYMBOLS.add("ℂ");
    MATH_SYMBOLS.add("𝔠");
    MATH_SYMBOLS.add("∂");
    MATH_SYMBOLS.add("𝔼");
    MATH_SYMBOLS.add("∃");
    MATH_SYMBOLS.add("∈");
    MATH_SYMBOLS.add("∉");
    MATH_SYMBOLS.add("∋");
    MATH_SYMBOLS.add("ℍ");
    MATH_SYMBOLS.add("ℕ");
    MATH_SYMBOLS.add("∘");
    MATH_SYMBOLS.add("ℙ");
    MATH_SYMBOLS.add("ℚ");
    MATH_SYMBOLS.add("ǫ");
    MATH_SYMBOLS.add("ℝ");
    MATH_SYMBOLS.add("†");
    MATH_OPERATORS.add("⊤");
    MATH_OPERATORS.add("⊥");
    MATH_OPERATORS.add("∪");
    MATH_OPERATORS.add("∩");
    MATH_OPERATORS.add("∨");
    MATH_OPERATORS.add("∧");
    MATH_OPERATORS.add("×");
    MATH_OPERATORS.add("⊗");
    MATH_OPERATORS.add("⋉");
    MATH_OPERATORS.add("⋊");
    MATH_OPERATORS.add("⋈");
    MATH_SYMBOLS.add("ℤ");

    MATH_SYMBOLS.add("α");
    MATH_SYMBOLS.add("β");
    MATH_SYMBOLS.add("γ");
    MATH_SYMBOLS.add("Δ");
    MATH_SYMBOLS.add("δ");
    MATH_SYMBOLS.add("ε");
    MATH_SYMBOLS.add("η");
    MATH_SYMBOLS.add("λ");
    MATH_SYMBOLS.add("μ");
    MATH_SYMBOLS.add("π");
    MATH_SYMBOLS.add("ρ");
    MATH_SYMBOLS.add("σ");
    MATH_SYMBOLS.add("Σ");
    MATH_SYMBOLS.add("τ");
    MATH_SYMBOLS.add("φ");
    MATH_SYMBOLS.add("χ");
    MATH_SYMBOLS.add("Φ");
    MATH_SYMBOLS.add("ω");
    MATH_SYMBOLS.add("Ω");

    MATH_OPERATORS.add("sin");
    MATH_OPERATORS.add("cos");
    MATH_OPERATORS.add("tan");
    MATH_OPERATORS.add("exp");
    MATH_OPERATORS.add("log");
    MATH_OPERATORS.add("ln");
    MATH_OPERATORS.add("sec");
    MATH_OPERATORS.add("csc");
    MATH_OPERATORS.add("cot");
    MATH_OPERATORS.add("arcsin");
    MATH_OPERATORS.add("arccos");
    MATH_OPERATORS.add("arctan");
    MATH_OPERATORS.add("arcsec");
    MATH_OPERATORS.add("arccsc");
    MATH_OPERATORS.add("arccot");
    MATH_OPERATORS.add("sinh");
    MATH_OPERATORS.add("cosh");
    MATH_OPERATORS.add("tanh");
    MATH_OPERATORS.add("coth");
    MATH_OPERATORS.add("mod");
    MATH_OPERATORS.add("min");
    MATH_OPERATORS.add("max");
    MATH_OPERATORS.add("inf");
    MATH_OPERATORS.add("sup");
    MATH_OPERATORS.add("lim");
    MATH_OPERATORS.add("lim inf");
    MATH_OPERATORS.add("lim sup");
    MATH_OPERATORS.add("arg");
    MATH_OPERATORS.add("sgn");
    MATH_OPERATORS.add("deg");
    MATH_OPERATORS.add("dim");
    MATH_OPERATORS.add("hom");
    MATH_OPERATORS.add("ker");
    MATH_OPERATORS.add("gcd");
    MATH_OPERATORS.add("det");
    MATH_OPERATORS.add("Pr");
  }

  /**
   * Returns true if the given character is a letter.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a letter; False otherwise.
   */
  public static boolean isLetter(PdfCharacter character) {
    return Character.isLetter(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is a digit.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a digit; False otherwise.
   */
  public static boolean isDigit(PdfCharacter character) {
    return Character.isDigit(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is a Latin letter.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a Latin letter.
   */
  public static boolean isLatinLetter(PdfCharacter character) {
    char ch = character.getText().charAt(0);
    return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
  }

  /**
   * Checks if the given character is a letter or a digit.
   * 
   * @param character
   *        The character to check.
   * @return True, if the given characters is a letter or a digit; False
   *         otherwise.
   */
  public static boolean isLetterOrDigit(PdfCharacter character) {
    return Character.isLetterOrDigit(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is a Latin letter or a digit.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a Latin letter or a digit.
   */
  public static boolean isLatinLetterOrDigit(PdfCharacter character) {
    return isLatinLetter(character) || isDigit(character);
  }

  /**
   * Returns true if the given character is a punctuation mark.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a punctuation mark; False
   *         otherwise.
   */
  public static boolean isPunctuationMark(PdfCharacter character) {
    char ch = character.getText().charAt(0);
    return BASELINE_PUNCTUATION_MARKS.contains(ch)
        || MEANLINE_PUNCTUATION_MARKS.contains(ch);
  }

  /**
   * Returns true if the given character is a punctuation mark that is aligned
   * to the mean line.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a punctuation mark aligned to the
   *         mean line; False otherwise.
   */
  public static boolean isMeanlinePunctuationMark(PdfCharacter character) {
    return MEANLINE_PUNCTUATION_MARKS.contains(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is a punctuation mark that is aligned
   * to the base line.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a punctuation mark aligned to the
   *         base line; False otherwise.
   */
  public static boolean isBaselinePunctuationMark(PdfCharacter character) {
    return BASELINE_PUNCTUATION_MARKS.contains(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is an ascender or a descender.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is an ascender or a descender; False
   *         otherwise.
   */
  public static boolean isAscenderOrDescender(PdfCharacter character) {
    return isAscender(character) || isDescender(character);
  }

  /**
   * Returns true if the given character is a descender.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a descender.
   */
  public static boolean isDescender(PdfCharacter character) {
    return DESCENDERS.contains(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is a ascender.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a ascender.
   */
  public static boolean isAscender(PdfCharacter character) {
    char ch = character.getText().charAt(0);
    return Character.isUpperCase(ch) || Character.isDigit(ch)
        || ASCENDERS.contains(ch);
  }

  /**
   * Returns true if the given character is aligned to the mean line.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is aligned to the mean line; False
   *         otherwise.
   */
  public static boolean isMeanlineCharacter(PdfCharacter character) {
    return MEANLINE_CHARACTERS.contains(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is aligned to the base line.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is aligned to the base line; False
   *         otherwise.
   */
  public static boolean isBaselineCharacter(PdfCharacter character) {
    return BASELINE_CHARACTERS.contains(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is in upper-case.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is in upper-case; False otherwise.
   */
  public static boolean isUppercase(PdfCharacter character) {
    return Character.isUpperCase(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is in lower-case.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is in lower-case; False otherwise.
   */
  public static boolean isLowercase(PdfCharacter character) {
    return Character.isLowerCase(character.getText().charAt(0));
  }

  /**
   * Returns true if the given character is a mathematical symbol.
   * 
   * @param character
   *        The character to process.
   * @return True if the given character is a mathematical symbol; False
   *         otherwise.
   */
  public static boolean isMathSymbol(PdfCharacter character) {
    String ch = character.getText();
    return MATH_SYMBOLS.contains(ch) || MATH_OPERATORS.contains(ch);
  }
}
