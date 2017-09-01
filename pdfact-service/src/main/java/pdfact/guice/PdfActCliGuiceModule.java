package pdfact.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

import pdfact.model.SerializeFormat;
import pdfact.pipes.PdfActServicePipe;
import pdfact.pipes.PdfActServicePipe.PdfActServicePipeFactory;
import pdfact.pipes.PlainPdfActServicePipe;
import pdfact.pipes.serialize.PdfJsonSerializer;
import pdfact.pipes.serialize.PdfJsonSerializer.JsonSerializerFactory;
import pdfact.pipes.serialize.PdfSerializer.SerializerFactory;
import pdfact.pipes.serialize.PdfTxtSerializer;
import pdfact.pipes.serialize.PdfTxtSerializer.TxtSerializerFactory;
import pdfact.pipes.serialize.PdfXmlSerializer;
import pdfact.pipes.serialize.PdfXmlSerializer.XmlSerializerFactory;
import pdfact.pipes.serialize.PlainSerializePdfPipe;
import pdfact.pipes.serialize.SerializePdfPipe;
import pdfact.pipes.serialize.SerializePdfPipe.SerializePdfPipeFactory;
import pdfact.pipes.serialize.plain.PlainPdfJsonSerializer;
import pdfact.pipes.serialize.plain.PlainPdfTxtSerializer;
import pdfact.pipes.serialize.plain.PlainPdfXmlSerializer;
import pdfact.pipes.validate.PlainValidatePathToWritePipe;
import pdfact.pipes.validate.ValidatePathToWritePipe;
import pdfact.pipes.validate.ValidatePathToWritePipe.ValidatePathToWritePipeFactory;
import pdfact.pipes.visualize.PdfDrawer;
import pdfact.pipes.visualize.PdfDrawer.PdfDrawerFactory;
import pdfact.pipes.visualize.PdfVisualizer;
import pdfact.pipes.visualize.PdfVisualizer.PdfVisualizerFactory;
import pdfact.pipes.visualize.PlainPdfVisualizer;
import pdfact.pipes.visualize.PlainVisualizePdfPipe;
import pdfact.pipes.visualize.VisualizePdfPipe;
import pdfact.pipes.visualize.VisualizePdfPipe.VisualizePdfPipeFactory;
import pdfact.pipes.visualize.pdfbox.PdfBoxDrawer;

/**
 * A Guice module that defines the basic bindings for the CLI of PdfAct.
 * 
 * @author Claudius Korzen
 */
public class PdfActCliGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new PdfActCoreGuiceModule());

    // Install the factory of the service pipe.
    install(new FactoryModuleBuilder()
        .implement(PdfActServicePipe.class, PlainPdfActServicePipe.class)
        .build(PdfActServicePipeFactory.class));

    // Install the factory of the pipe that validates paths to write to.
    install(new FactoryModuleBuilder()
        .implement(ValidatePathToWritePipe.class,
            PlainValidatePathToWritePipe.class)
        .build(ValidatePathToWritePipeFactory.class));

    // Install the factory of the pipe that serializes PDF documents.
    install(new FactoryModuleBuilder()
        .implement(SerializePdfPipe.class, PlainSerializePdfPipe.class)
        .build(SerializePdfPipeFactory.class));

    // Install the factory of the pipe that visualizes PDF documents.
    install(new FactoryModuleBuilder()
        .implement(VisualizePdfPipe.class, PlainVisualizePdfPipe.class)
        .build(VisualizePdfPipeFactory.class));

    // Install the factory of the TXT serializer.
    install(new FactoryModuleBuilder()
        .implement(PdfTxtSerializer.class, PlainPdfTxtSerializer.class)
        .build(TxtSerializerFactory.class));

    // Install the factory of the XML serializer.
    install(new FactoryModuleBuilder()
        .implement(PdfXmlSerializer.class, PlainPdfXmlSerializer.class)
        .build(XmlSerializerFactory.class));

    // Install the factory of the JSON serializer.
    install(new FactoryModuleBuilder()
        .implement(PdfJsonSerializer.class, PlainPdfJsonSerializer.class)
        .build(JsonSerializerFactory.class));

    // Install the factory of the PDF drawer.
    install(new FactoryModuleBuilder()
        .implement(PdfDrawer.class, PdfBoxDrawer.class)
        .build(PdfDrawerFactory.class));

    // Install the factory of the PDF visualizer.
    install(new FactoryModuleBuilder()
        .implement(PdfVisualizer.class, PlainPdfVisualizer.class)
        .build(PdfVisualizerFactory.class));

    // ========================================================================

    MapBinder<SerializeFormat, SerializerFactory> binder = MapBinder
        .newMapBinder(binder(), SerializeFormat.class, SerializerFactory.class);

    binder.addBinding(SerializeFormat.TXT).to(TxtSerializerFactory.class);
    binder.addBinding(SerializeFormat.XML).to(XmlSerializerFactory.class);
    binder.addBinding(SerializeFormat.JSON).to(JsonSerializerFactory.class);
  }
}
