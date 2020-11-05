package pdfact.core.pipes.translate.characters;

import pdfact.core.util.pipeline.Pipe;

/**
 * A pipe that standardizes characters, i.e. translates characters with
 * different glyphs but same semantic meaning to a steady characters. For
 * example this pipe translates '“' to '"'.
 * 
 * @author Claudius Korzen
 */
public interface StandardizeCharactersPipe extends Pipe {
  
}
