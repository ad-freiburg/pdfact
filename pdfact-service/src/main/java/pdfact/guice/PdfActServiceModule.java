package pdfact.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

import pdfact.PdfActCore;
import pdfact.serialize.JsonPdfSerializer;
import pdfact.serialize.PdfActSerializationFormat;
import pdfact.serialize.PdfSerializer;
import pdfact.serialize.TxtPdfSerializer;
import pdfact.serialize.XmlPdfSerializer;
import pdfact.visualize.PdfDrawer;
import pdfact.visualize.PdfDrawerFactory;
import pdfact.visualize.PdfVisualizer;
import pdfact.visualize.PdfVisualizer.PdfVisualizerFactory;
import pdfact.visualize.PlainPdfVisualizer;
import pdfact.visualize.pdfbox.PdfBoxDrawer;

/**
 * A module that defines the basic Guice bindings for the PdfAct services.
 * 
 * @author Claudius Korzen
 */
public class PdfActServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PdfActCore.class);

    MapBinder<PdfActSerializationFormat, PdfSerializer> serializerBinder = MapBinder
        .newMapBinder(binder(), PdfActSerializationFormat.class,
            PdfSerializer.class);

    // Bind the serializers.
    serializerBinder.addBinding(TxtPdfSerializer.getSerializationFormat())
        .to(TxtPdfSerializer.class);
    serializerBinder.addBinding(XmlPdfSerializer.getSerializationFormat())
        .to(XmlPdfSerializer.class);
    serializerBinder.addBinding(JsonPdfSerializer.getSerializationFormat())
        .to(JsonPdfSerializer.class);

    // Bind the visualizer.
    fc(PdfDrawer.class, PdfDrawerFactory.class, PdfBoxDrawer.class);
    fc(PdfVisualizer.class, PdfVisualizerFactory.class,
        PlainPdfVisualizer.class);
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
