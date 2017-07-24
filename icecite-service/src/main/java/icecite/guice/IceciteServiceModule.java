package icecite.guice;

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
  }
}
