package pdfact.guice;

import com.google.inject.AbstractModule;

import pdfact.Icecite;

/**
 * A module that defines the basic Guice bindings for the Icecite services.
 * 
 * @author Claudius Korzen
 */
public class IceciteServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Icecite.class);
  }
}
