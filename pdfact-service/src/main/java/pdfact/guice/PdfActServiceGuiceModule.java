package pdfact.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

import pdfact.PdfActCore;
import pdfact.model.PdfSerializationFormat;
import pdfact.serialize.PdfJsonSerializer;
import pdfact.serialize.PdfJsonSerializer.PdfJsonSerializerFactory;
import pdfact.serialize.PdfSerializer.PdfSerializerFactory;
import pdfact.serialize.PdfTxtSerializer;
import pdfact.serialize.PdfTxtSerializer.PdfTxtSerializerFactory;
import pdfact.serialize.PdfXmlSerializer;
import pdfact.serialize.PdfXmlSerializer.PdfXmlSerializerFactory;
import pdfact.serialize.plain.PlainPdfJsonSerializer;
import pdfact.serialize.plain.PlainPdfTxtSerializer;
import pdfact.serialize.plain.PlainPdfXmlSerializer;
import pdfact.visualize.PdfDrawer;
import pdfact.visualize.PdfDrawer.PdfDrawerFactory;
import pdfact.visualize.PdfVisualizer;
import pdfact.visualize.PdfVisualizer.PdfVisualizerFactory;
import pdfact.visualize.PlainPdfVisualizer;
import pdfact.visualize.pdfbox.PdfBoxDrawer;

/**
 * A module that defines the basic Guice bindings for the PdfAct services.
 * 
 * @author Claudius Korzen
 */
public class PdfActServiceGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PdfActCore.class);

    // Install the serializer factories.
    install(new FactoryModuleBuilder()
        .implement(PdfTxtSerializer.class, PlainPdfTxtSerializer.class)
        .build(PdfTxtSerializerFactory.class));

    install(new FactoryModuleBuilder()
        .implement(PdfXmlSerializer.class, PlainPdfXmlSerializer.class)
        .build(PdfXmlSerializerFactory.class));

    install(new FactoryModuleBuilder()
        .implement(PdfJsonSerializer.class, PlainPdfJsonSerializer.class)
        .build(PdfJsonSerializerFactory.class));

    MapBinder<PdfSerializationFormat, PdfSerializerFactory> binder = MapBinder
        .newMapBinder(binder(), PdfSerializationFormat.class,
            PdfSerializerFactory.class);

    binder.addBinding(PdfSerializationFormat.TXT)
        .to(PdfTxtSerializerFactory.class);
    binder.addBinding(PdfSerializationFormat.XML)
        .to(PdfXmlSerializerFactory.class);
    binder.addBinding(PdfSerializationFormat.JSON)
        .to(PdfJsonSerializerFactory.class);

    // Bind the visualizers.
    install(new FactoryModuleBuilder()
        .implement(PdfDrawer.class, PdfBoxDrawer.class)
        .build(PdfDrawerFactory.class));

    install(new FactoryModuleBuilder()
        .implement(PdfVisualizer.class, PlainPdfVisualizer.class)
        .build(PdfVisualizerFactory.class));
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
  protected <T, F> void fc(Class<T> c, Class<F> f, Class<? extends T> i) {
    install(new FactoryModuleBuilder().implement(c, i).build(f));
  }
}
