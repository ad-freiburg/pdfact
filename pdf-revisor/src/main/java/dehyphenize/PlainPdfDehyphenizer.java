package dehyphenize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.freiburg.iif.text.StringUtils;
import model.PdfCharacter;
import model.PdfDocument;
import model.PdfPage;
import model.PdfRole;
import model.PdfTextLine;
import model.PdfWord;

public class PlainPdfDehyphenizer implements PdfDehyphenizer {
  @Override
  public void dehyphenize(PdfDocument document) {
    if (document == null) {
      return;
    }
    
    List<PdfPage> pages = document.getPages(); 
    if (pages == null) {
      return;
    }
    
    // To identify intended hyphens, check if there are multiple occurrences of
    // the hyphen zone in the document.
    // Count the frequencies of "hyphen zones" in the document. A hyphen zone
    // is defined as the zone around a hyphen, consisting of the whole 
    // substring of the word in front of the hyphen, the hyphen itself and a 
    // well defined number of characters of the substring after the hyphen.
    // For example, the hyphen zone of the string "quasi-ergodicity" could be 
    // "quasi-erg". This allows to identify hyphens in "quasi-ergodicity" as 
    // well as in "quasi-ergodic".
    Map<String, Integer> hyphenZoneFreqs = computeHyphenZoneFreqs(document);
    
    dehyphenize(document, hyphenZoneFreqs);
  }

  // ___________________________________________________________________________
  
  /**
   * Dehyphenizes the words in the given page. 'hyphenFreqs' contains the 
   * frequencies of the hyphen zones in the document and is needed to identify
   * intended hyphens.
   */
  protected void dehyphenize(PdfDocument document, Map<String, Integer> hyphenFreqs) {
    if (document == null) {
      return;
    }

    Map<PdfRole, PdfTextLine> prevBodyLines = new HashMap<>();
    
    for (PdfPage page : document.getPages()) {
      List<PdfTextLine> lines = page.getTextLines();
    
      if (lines == null) {
        return;
      }
      
      for (PdfTextLine line : lines) {
        if (line == null) {
          continue;
        }
                
        PdfTextLine prevBodyLine = prevBodyLines.get(line.getRole());
        if (prevBodyLine != null) {
          PdfWord lastBodyWord = prevBodyLine.getLastWord();
          if (lastBodyWord != null) {
            PdfCharacter lastBodyCharacter = lastBodyWord.getLastTextCharacter();
            if (lastBodyCharacter != null) {
              // Check, if the last line ends with an hyphen.
              // TODO: Allow another dashes ("--". "---")
              if (lastBodyCharacter.getUnicode().equals("-")) {
                // The last line ends with a hyphen. 
                PdfWord word = line.getFirstWord();
                
                // Decide, if we have to ignore the hyphen or not.
                boolean ignoreHyphen = true;
                
                // Obtain the frequency of the hyphen zone of the word in the 
                // document.
                int hyphenZoneFreq = 0;
                String withHyphen = lastBodyWord.getUnicode() + word.getUnicode();
                String hyphenZone = getHyphenZone(withHyphen, 3);
                if (hyphenFreqs.containsKey(hyphenZone)) {
                  hyphenZoneFreq = hyphenFreqs.get(hyphenZone);
                }
                
                if (hyphenZoneFreq > 0) {
                  // Don't ignore the hyphen, if there are further occurrences of 
                  // the hyphen zone in the document.
                  ignoreHyphen = false;
                } else {
                  // Otherwise, ignore the hyphen, if the first character after
                  // the hyphen isn't a upper case.
                  String firstChar = word.getFirstTextCharacter().toString();
                  ignoreHyphen = !Character.isUpperCase(firstChar.charAt(0));
                }
                
                // Merge the both words.
                lastBodyWord.addAnyElements(word.getElements());
                                
                // Ignore the word (because it was merged with the previous word)
                word.setIgnore(true);  
                // Ignore the hyphen if necessary.
                if (ignoreHyphen) {
                  lastBodyCharacter.setIgnore(true);
                }
              }
            }
          }
        }
        prevBodyLines.put(line.getRole(), line);
      }
    }
  }    
  
  /**
   * Computes the frequencies of hyphen zones in the given document.
   */
  protected Map<String, Integer> computeHyphenZoneFreqs(PdfDocument document) {
    Map<String, Integer> hyphenZoneFreqs = new HashMap<>();
    
    if (document != null) {
      List<PdfPage> pages = document.getPages();
      if (pages != null) {
        for (PdfPage page : pages) {
          if (page == null) {
            continue;
          }
          
          List<PdfWord> words = page.getWords();
          if (words == null) {
            continue;
          }
          
          for (PdfWord word : words) {
            // Find the hyphen zone, if any.
            String hyphenZone = getHyphenZone(word.getUnicode(), 3);
            
            if (hyphenZone != null) {
              int freq = 1;
              if (hyphenZoneFreqs.containsKey(hyphenZone)) {
                freq = hyphenZoneFreqs.get(hyphenZone) + 1;
              }
              hyphenZoneFreqs.put(hyphenZone, freq);
            }
          }
        }
      }
    }
    return hyphenZoneFreqs;
  }
  
  /**
   * Computes the hyphen zone with an appendix of at most k characters. The
   * appendix is the part after the hyphen. For example the hyphen zone of
   * "self-contained" with k=3 is "self-con". 
   */
  protected String getHyphenZone(String word, int k) {
    if (word == null) {
      return null;
    }
    
    // Remove all punctuation marks (except "-").
    String normalized = StringUtils.normalize(word, false, false, true, '-');
    
    if (normalized == null) {
      return null;
    }
    
    String[] parts = normalized.split("-");
      
    if (parts.length == 2) {
      // Build the hyphen zone.
      StringBuilder sb = new StringBuilder();
      sb.append(parts[0]);
      sb.append("-");
      if (parts[1] != null) {
        String appendix = parts[1].trim();
        int lengthAppendix = Math.min(appendix.length(), k);
        sb.append(appendix.substring(0, lengthAppendix));
        return sb.toString();
      }
    }
    return null;
  } 
  
}
