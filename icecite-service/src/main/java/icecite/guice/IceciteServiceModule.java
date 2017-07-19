package icecite.guice;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.google.inject.AbstractModule;

import icecite.Icecite;

/**
 * A module that defines the basic Guice bindings for the Icecite services.
 * 
 * @author Claudius Korzen
 */
public class IceciteServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Icecite.class);
    
    bind(CommandLineParser.class).to(DefaultParser.class);
    bind(Options.class);
    bind(HelpFormatter.class);
  }
}
