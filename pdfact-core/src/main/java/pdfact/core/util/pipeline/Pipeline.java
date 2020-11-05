package pdfact.core.util.pipeline;

import java.util.List;

import pdfact.core.model.PdfDocument;
import pdfact.core.util.exception.PdfActException;

/**
 * A pipeline to process a chain of pipes sequentially.
 * 
 * @author Claudius Korzen
 */
public interface Pipeline {
  /**
   * Processes the pipes of this pipeline sequentially, with the given PDF
   * document as input.
   * 
   * @param pdf
   *        The input PDF document.
   * @return The state of the PDF document after processing the pipeline.
   * 
   * @throws PdfActException
   *         If something went wrong while processing this pipeline.
   */
  PdfDocument process(PdfDocument pdf) throws PdfActException;

  // ==============================================================================================

  /**
   * Returns the registered pipes of this pipeline.
   * 
   * @return The list of registered pipes of this pipeline.
   */
  List<Pipe> getPipes();

  // ==============================================================================================

  /**
   * Registers the pipes to be execute on processing this pipeline.
   * 
   * @param pipes
   *        The list of pipes to execute.
   */
  void setPipes(List<Pipe> pipes);

  /**
   * Registers the given pipes to this pipeline.
   * 
   * @param pipes
   *        The list of pipes to register to this pipeline.
   */
  void addPipes(List<Pipe> pipes);

  /**
   * Registers the given pipe to this pipeline.
   * 
   * @param pipe
   *        The pipe to register to this pipeline.
   */
  void addPipe(Pipe pipe);

  // ==============================================================================================

  /**
   * Returns the number of pipes in this pipeline.
   * 
   * @return The number of pipes in this pipeline.
   */
  int size();
}
