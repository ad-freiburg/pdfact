package icecite.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

import icecite.drawer.PdfDrawer;
import icecite.drawer.PdfDrawerFactory;
import icecite.drawer.pdfbox.PdfBoxDrawer;
import icecite.serializer.JsonPdfSerializer;
import icecite.serializer.PdfSerializer;
import icecite.serializer.TxtPdfSerializer;
import icecite.serializer.XmlPdfSerializer;
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
    // Bind the serializers.
    bind(PdfSerializer.class).annotatedWith(Names.named("txt"))
        .to(TxtPdfSerializer.class);
    bind(PdfSerializer.class).annotatedWith(Names.named("xml"))
        .to(XmlPdfSerializer.class);
    bind(PdfSerializer.class).annotatedWith(Names.named("json"))
        .to(JsonPdfSerializer.class);

    // Bind the visualizer.
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
