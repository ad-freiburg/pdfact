package pdfact.util.lexicon;

import java.util.HashSet;
import java.util.Set;

import gnu.trove.set.TCharSet;
import gnu.trove.set.hash.TCharHashSet;
import pdfact.model.Character;

/**
 * A collection of utility methods that deal with Character objects.
 * 
 * @author Claudius Korzen
 */
public class CharacterLexicon {
  /**
   * The null character.
   */
  public static final char NULL = '\u0000';

  // ==========================================================================
  // Letters.
  
  /**
   * Upper case letters.
   */
  public static final TCharSet UPPERCASE_LETTERS = new TCharHashSet();
  
  static {
    for (char i = 'A'; i <= 'Z'; i++) {
      UPPERCASE_LETTERS.add(i);
    }
  }
  
  /**
   * Lower case letters.
   */
  public static final TCharSet LOWERCASE_LETTERS = new TCharHashSet();
  
  static {
    for (char i = 'a'; i <= 'z'; i++) {
      LOWERCASE_LETTERS.add(i);
    }
  }
  
  /**
   * All letters.
   */
  public static final TCharSet LETTERS = new TCharHashSet();
  
  static {
    LETTERS.addAll(UPPERCASE_LETTERS);
    LETTERS.addAll(LOWERCASE_LETTERS);
  }
  
  /**
   * Digits.
   */
  public static final TCharSet DIGITS = new TCharHashSet();
  
  static {
    for (char i = '0'; i <= '9'; i++) {
      DIGITS.add(i);
    }
  }
  
  // ==========================================================================
  
  /**
   * The characters that aren't aligned to the baseline of a text line.
   */
  public static final TCharSet DESCENDERS;

  static {
    DESCENDERS = new TCharHashSet();
    DESCENDERS.add('g');
    DESCENDERS.add('j');
    DESCENDERS.add('p');
    DESCENDERS.add('q');
    DESCENDERS.add('y');
    DESCENDERS.add('f'); // If f is italic.
    DESCENDERS.add('i'); // In combination with f (ligature fi).
    DESCENDERS.add('Q');
    DESCENDERS.add('J');
  }
  
  /**
   * The characters that aren't aligned to the meanline of a text line.
   */
  public static final TCharSet ASCENDERS;

  static {
    ASCENDERS = new TCharHashSet();
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
  }
  
  /**
   * The characters that are aligned to the baseline of a text line.
   */
  public static final TCharSet BASELINE_CHARACTERS;

  static {
    BASELINE_CHARACTERS = new TCharHashSet();
    BASELINE_CHARACTERS.addAll(DIGITS);
    BASELINE_CHARACTERS.addAll(UPPERCASE_LETTERS);
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
  }
  
  /**
   * The characters that are aligned to the baseline of a text line.
   */
  public static final TCharSet MEANLINE_CHARACTERS;
  
  static {
    MEANLINE_CHARACTERS = new TCharHashSet();
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
  }
  
  // ==========================================================================
  // Punctuation marks.
  
  /**
   * The punctuation marks that are aligned to the base line.
   */
  public static final TCharSet BASELINE_PUNCTUATION_MARKS;
  
  static {
    BASELINE_PUNCTUATION_MARKS = new TCharHashSet();
    BASELINE_PUNCTUATION_MARKS.add('.');
    BASELINE_PUNCTUATION_MARKS.add('?');
    BASELINE_PUNCTUATION_MARKS.add('!');
    BASELINE_PUNCTUATION_MARKS.add(':');
    BASELINE_PUNCTUATION_MARKS.add(';');
    BASELINE_PUNCTUATION_MARKS.add(',');
  }
  
  /**
   * The punctuation marks that are aligned to the mean line.
   */
  public static final TCharSet MEANLINE_PUNCTUATION_MARKS;
  
  static {
    MEANLINE_PUNCTUATION_MARKS = new TCharHashSet();
    MEANLINE_PUNCTUATION_MARKS.add('\'');
    MEANLINE_PUNCTUATION_MARKS.add('"');
    MEANLINE_PUNCTUATION_MARKS.add('“');
    MEANLINE_PUNCTUATION_MARKS.add('”');
    MEANLINE_PUNCTUATION_MARKS.add('`');
    MEANLINE_PUNCTUATION_MARKS.add('´');
    MEANLINE_PUNCTUATION_MARKS.add('’');
  }
  
  /**
   * The characters that represent a hyphen.
   */
  public static final TCharSet HYPHENS;
  
  static {
    HYPHENS = new TCharHashSet();
    HYPHENS.add('-');
    HYPHENS.add('–');
  }
  
  /**
   * All punctuation marks.
   */
  public static final TCharSet PUNCTUATION_MARKS;
  
  static {
    PUNCTUATION_MARKS = new TCharHashSet();
    PUNCTUATION_MARKS.addAll(BASELINE_CHARACTERS);
    PUNCTUATION_MARKS.addAll(MEANLINE_CHARACTERS);
    PUNCTUATION_MARKS.addAll(HYPHENS);
  }
  
  // ==========================================================================

  /**
   * The math characters.
   */
  public static final Set<String> MATH_SYMBOLS;

  /**
   * The math operators (all symbols that must be surrounded by white spaces).
   */
  public static final Set<String> MATH_OPERATORS;

  static {
    MATH_OPERATORS = new HashSet<>();
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
    MATH_OPERATORS.add("∴");
    MATH_OPERATORS.add("∵");
    MATH_OPERATORS.add("∞");

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
    MATH_OPERATORS.add("•");

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

  static {
    MATH_SYMBOLS = new HashSet<>();
    MATH_SYMBOLS.add("√");
    MATH_SYMBOLS.add("∑");
    MATH_SYMBOLS.add("∫");
    MATH_SYMBOLS.add("∮");
    MATH_SYMBOLS.add("¬");
    MATH_SYMBOLS.add("˜");
    MATH_SYMBOLS.add("∝");
    MATH_SYMBOLS.add("■");
    MATH_SYMBOLS.add("□");
    MATH_SYMBOLS.add("∎");
    MATH_SYMBOLS.add("▮");
    MATH_SYMBOLS.add("‣");
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
  }
  
  // ==========================================================================

  /**
   * Returns true if the given character is a letter.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a letter; false otherwise.
   */
  public static boolean isLetter(Character character) {
    return java.lang.Character.isLetter(toChar(character));
  }

  /**
   * Returns true if the given character is a digit.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a digit; false otherwise.
   */
  public static boolean isDigit(Character character) {
    return java.lang.Character.isDigit(toChar(character));
  }

  /**
   * Returns true if the given character is a Latin letter.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a latin letter; false otherwise.
   */
  public static boolean isLatinLetter(Character character) {
    char ch = toChar(character);
    return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
  }

  /**
   * Checks if the given character is a letter or a digit.
   * 
   * @param character
   *        The character to check.
   * 
   * @return True, if the given character is a letter or a digit; False
   *         otherwise.
   */
  public static boolean isLetterOrDigit(Character character) {
    return java.lang.Character.isLetterOrDigit(toChar(character));
  }

  /**
   * Returns true if the given character is a Latin letter or a digit.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a latin letter or a digit; false
   *         otherwise.
   */
  public static boolean isLatinLetterOrDigit(Character character) {
    return isLatinLetter(character) || isDigit(character);
  }

  /**
   * Returns true if the given character is a punctuation mark.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a punctuation mark; false otherwise.
   */
  public static boolean isPunctuationMark(Character character) {
    char ch = toChar(character);
    return BASELINE_PUNCTUATION_MARKS.contains(ch)
        || MEANLINE_PUNCTUATION_MARKS.contains(ch);
  }

  /**
   * Returns true if the given character is a punctuation mark that is aligned
   * to the mean line.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a punctuation mark aligned to the
   *         mean line; false otherwise.
   */
  public static boolean isMeanlinePunctuationMark(Character character) {
    return MEANLINE_PUNCTUATION_MARKS.contains(toChar(character));
  }

  /**
   * Returns true if the given character is a punctuation mark that is aligned
   * to the base line.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a punctuation mark aligned to the
   *         base line; false otherwise.
   */
  public static boolean isBaselinePunctuationMark(Character character) {
    return BASELINE_PUNCTUATION_MARKS.contains(toChar(character));
  }

  /**
   * Returns true if the given character is an ascender or a descender.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is an ascender or a descender; false
   *         otherwise.
   */
  public static boolean isAscenderOrDescender(Character character) {
    return isAscender(character) || isDescender(character);
  }

  /**
   * Returns true if the given character is a descender.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a descender.
   */
  public static boolean isDescender(Character character) {
    return DESCENDERS.contains(toChar(character));
  }

  /**
   * Returns true if the given character is an ascender.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is an ascender; false otherwise
   */
  public static boolean isAscender(Character character) {
    char ch = toChar(character);
    return java.lang.Character.isUpperCase(ch)
        || java.lang.Character.isDigit(ch)
        || ASCENDERS.contains(ch);
  }

  /**
   * Returns true if the given character is aligned to the mean line.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is aligned to the mean line; false
   *         otherwise.
   */
  public static boolean isMeanlineCharacter(Character character) {
    return MEANLINE_CHARACTERS.contains(toChar(character));
  }

  /**
   * Returns true if the given character is aligned to the base line.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is aligned to the base line; false
   *         otherwise.
   */
  public static boolean isBaselineCharacter(Character character) {
    return BASELINE_CHARACTERS.contains(toChar(character));
  }

  /**
   * Returns true if the given character is an upper-cased character.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is an upper-cased character; false
   *         otherwise.
   */
  public static boolean isUppercase(Character character) {
    return java.lang.Character.isUpperCase(toChar(character));
  }

  /**
   * Returns true if the given character is a lower-cased character.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a lower-cased character; false
   *         otherwise.
   */
  public static boolean isLowercase(Character character) {
    return java.lang.Character.isLowerCase(toChar(character));
  }

  /**
   * Returns true if the given character is a mathematical symbol.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a mathematical symbol; false
   *         otherwise.
   */
  // TODO: Pass a Word instead of a character here?
  public static boolean isMathSymbol(Character character) {
    String ch = character.getText();
    return MATH_SYMBOLS.contains(ch) || MATH_OPERATORS.contains(ch);
  }

  /**
   * Returns true if the given character is a hyphen.
   * 
   * @param character
   *        The character to process.
   * 
   * @return True if the given character is a hyphen; false otherwise.
   */
  public static boolean isHyphen(Character character) {
    return HYPHENS.contains(toChar(character));
  }

  // ==========================================================================

  /**
   * Translates the given {@link Character} object to a primitive char value.
   * 
   * @param character
   *        The character to translate.
   * 
   * @return The primitive char value, or CharacterUtils.NULL if the given
   *         character is null or empty.
   */
  protected static char toChar(Character character) {
    if (character == null) {
      return NULL;
    }

    String text = character.getText();
    if (text == null || text.isEmpty()) {
      return NULL;
    }

    return text.charAt(0);
  }
}
