package icecite.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import icecite.drawer.PdfDrawer;
import icecite.drawer.PdfDrawerFactory;
import icecite.drawer.pdfbox.PdfBoxDrawer;
import icecite.visualizer.PdfVisualizer;
import icecite.visualizer.PlainPdfVisualizer;

/**
 * A module that defines the basic Guice bindings for the Icecite services.
 * 
 * @author Claudius Korzen
 */
public class IceciteServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PdfVisualizer.class).to(PlainPdfVisualizer.class);
    
    fac(PdfDrawer.class, PdfDrawerFactory.class, PdfBoxDrawer.class);
  }

  /**
   * Binds the given interface and the given factory to the given
   * implementation.
   * 
   * @param c
   *        The interface to process.
   * @param f
   *        The factory to process.
   * @param i
   *        The implementation to process.
   */
  protected <T, F> void fac(Class<T> c, Class<F> f, Class<? extends T> i) {
    install(new FactoryModuleBuilder().implement(c, i).build(f));
  }
}
