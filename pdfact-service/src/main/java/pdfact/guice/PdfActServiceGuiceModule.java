package pdfact.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;

import pdfact.model.SerializationFormat;
import pdfact.pipes.PdfActServicePipe;
import pdfact.pipes.PdfActServicePipe.PdfActServicePipeFactory;
import pdfact.pipes.PlainPdfActServicePipe;
import pdfact.pipes.serialize.PdfJsonSerializer;
import pdfact.pipes.serialize.PdfJsonSerializer.PdfJsonSerializerFactory;
import pdfact.pipes.serialize.PdfSerializer.PdfSerializerFactory;
import pdfact.pipes.serialize.PdfTxtSerializer;
import pdfact.pipes.serialize.PdfTxtSerializer.PdfTxtSerializerFactory;
import pdfact.pipes.serialize.PdfXmlSerializer;
import pdfact.pipes.serialize.PdfXmlSerializer.PdfXmlSerializerFactory;
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
 * A module that defines the basic Guice bindings for the PdfAct services.
 * 
 * @author Claudius Korzen
 */
public class PdfActServiceGuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new PdfActCoreGuiceModule());

    // Install the factory of the service pipe.
    install(new FactoryModuleBuilder()
        .implement(PdfActServicePipe.class, PlainPdfActServicePipe.class)
        .build(PdfActServicePipeFactory.class));

    // Install the factory of the pipe to validate paths to write to.
    install(new FactoryModuleBuilder()
        .implement(ValidatePathToWritePipe.class,
            PlainValidatePathToWritePipe.class)
        .build(ValidatePathToWritePipeFactory.class));

    // Install the factory of the pipe to serialize PDF documents.
    install(new FactoryModuleBuilder()
        .implement(SerializePdfPipe.class, PlainSerializePdfPipe.class)
        .build(SerializePdfPipeFactory.class));

    // Install the factory of the pipe to visualize PDF documents.
    install(new FactoryModuleBuilder()
        .implement(VisualizePdfPipe.class, PlainVisualizePdfPipe.class)
        .build(VisualizePdfPipeFactory.class));

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

    MapBinder<SerializationFormat, PdfSerializerFactory> binder = MapBinder
        .newMapBinder(binder(), SerializationFormat.class,
            PdfSerializerFactory.class);

    binder.addBinding(SerializationFormat.TXT)
        .to(PdfTxtSerializerFactory.class);
    binder.addBinding(SerializationFormat.XML)
        .to(PdfXmlSerializerFactory.class);
    binder.addBinding(SerializationFormat.JSON)
        .to(PdfJsonSerializerFactory.class);

    // Bind the visualizers.
    install(new FactoryModuleBuilder()
        .implement(PdfDrawer.class, PdfBoxDrawer.class)
        .build(PdfDrawerFactory.class));

    install(new FactoryModuleBuilder()
        .implement(PdfVisualizer.class, PlainPdfVisualizer.class)
        .build(PdfVisualizerFactory.class));
  }
}
