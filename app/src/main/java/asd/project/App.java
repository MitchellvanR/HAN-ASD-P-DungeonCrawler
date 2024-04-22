package asd.project;

import com.google.inject.Guice;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    var injector = Guice.createInjector(new GuiceModule());
    INetwork network = injector.getInstance(INetwork.class);
    IStorage storage = injector.getInstance(IStorage.class);
    IAgent agent = injector.getInstance(IAgent.class);
    IUI ui = injector.getInstance(IUI.class);
    IWorldGenerator worldGenerator = injector.getInstance(IWorldGenerator.class);

    new Game(agent, network, storage, ui, worldGenerator).start();
  }
}
