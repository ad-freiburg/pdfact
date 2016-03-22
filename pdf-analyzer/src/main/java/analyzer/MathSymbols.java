package analyzer;

import java.util.HashSet;

/**
 * Class of math symbols.
 * 
 * @author Claudius Korzen
 *
 */
public class MathSymbols {

  public static final HashSet<String> MATH_SYMBOLS = new HashSet<String>();

  static {
    // Basic symbols
    MATH_SYMBOLS.add("+");
    MATH_SYMBOLS.add("−");
    MATH_SYMBOLS.add("±");
    MATH_SYMBOLS.add("∓");
    MATH_SYMBOLS.add("×");
    MATH_SYMBOLS.add("⋅");
    MATH_SYMBOLS.add("·");
    MATH_SYMBOLS.add("÷");
    MATH_SYMBOLS.add("/");
    MATH_SYMBOLS.add("⁄");
    MATH_SYMBOLS.add("√");
    MATH_SYMBOLS.add("∑");
    MATH_SYMBOLS.add("∫");
    MATH_SYMBOLS.add("∮");
    MATH_SYMBOLS.add("∴");
    MATH_SYMBOLS.add("∵");
    MATH_SYMBOLS.add("¬");
    MATH_SYMBOLS.add("˜");
    MATH_SYMBOLS.add("∝");
    MATH_SYMBOLS.add("∞");
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
    MATH_SYMBOLS.add("=");
    MATH_SYMBOLS.add("≠");
    MATH_SYMBOLS.add("≈");
    MATH_SYMBOLS.add("~");
    MATH_SYMBOLS.add("≡");
    MATH_SYMBOLS.add("≜");
    MATH_SYMBOLS.add("≝");
    MATH_SYMBOLS.add("≐");
    MATH_SYMBOLS.add("≅");
    MATH_SYMBOLS.add("≡");
    MATH_SYMBOLS.add("⇔");
    MATH_SYMBOLS.add("↔");

    // Symbols that point left or right
    MATH_SYMBOLS.add("<");
    MATH_SYMBOLS.add(">");
    MATH_SYMBOLS.add("≪");
    MATH_SYMBOLS.add("≫");
    MATH_SYMBOLS.add("≤");
    MATH_SYMBOLS.add("≥");
    MATH_SYMBOLS.add("≦");
    MATH_SYMBOLS.add("≧");
    MATH_SYMBOLS.add("≺");
    MATH_SYMBOLS.add("≻");
    MATH_SYMBOLS.add("◅");
    MATH_SYMBOLS.add("▻");
    MATH_SYMBOLS.add("⇒");
    MATH_SYMBOLS.add("→");
    MATH_SYMBOLS.add("⊃");
    MATH_SYMBOLS.add("⊆");
    MATH_SYMBOLS.add("⊂");
    MATH_SYMBOLS.add("⊇");
    MATH_SYMBOLS.add("⊃");
    MATH_SYMBOLS.add("→");
    MATH_SYMBOLS.add("↦");
    MATH_SYMBOLS.add("⊧");
    MATH_SYMBOLS.add("⊢");

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
    MATH_SYMBOLS.add("*");
    MATH_SYMBOLS.add("∝");
    MATH_SYMBOLS.add("∖");
    MATH_SYMBOLS.add("∤");
    MATH_SYMBOLS.add("∥");
    MATH_SYMBOLS.add("∦");
    MATH_SYMBOLS.add("⋕");
    MATH_SYMBOLS.add("#");
    MATH_SYMBOLS.add("≀");
    MATH_SYMBOLS.add("↯");
    MATH_SYMBOLS.add("※");
    MATH_SYMBOLS.add("⊕");
    MATH_SYMBOLS.add("⊻");
    MATH_SYMBOLS.add("□");

    // Letter-based-symbols
    MATH_SYMBOLS.add("•");
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
    MATH_SYMBOLS.add("⊤");
    MATH_SYMBOLS.add("⊥");
    MATH_SYMBOLS.add("∪");
    MATH_SYMBOLS.add("∩");
    MATH_SYMBOLS.add("∨");
    MATH_SYMBOLS.add("∧");
    MATH_SYMBOLS.add("×");
    MATH_SYMBOLS.add("⊗");
    MATH_SYMBOLS.add("⋉");
    MATH_SYMBOLS.add("⋊");
    MATH_SYMBOLS.add("⋈");
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
    
    // Words
    MATH_SYMBOLS.add("const");
    MATH_SYMBOLS.add("cos");
    MATH_SYMBOLS.add("dx");
    MATH_SYMBOLS.add("exp");
    MATH_SYMBOLS.add("for");
    MATH_SYMBOLS.add("lim");
    MATH_SYMBOLS.add("ln");
    MATH_SYMBOLS.add("log");
    MATH_SYMBOLS.add("max");
    MATH_SYMBOLS.add("min");
    MATH_SYMBOLS.add("otherwise");
    MATH_SYMBOLS.add("sin");
    MATH_SYMBOLS.add("tan");
  }

  public static boolean containsMathSymbol(String str) {
    for (char character : str.toCharArray()) {
      if (isMathSymbol(character)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isMathSymbol(char symbol) {
    return isMathSymbol(String.valueOf(symbol));
  }
  
  public static boolean isMathSymbol(String symbol) {
    return MATH_SYMBOLS.contains(symbol);
  }
}
