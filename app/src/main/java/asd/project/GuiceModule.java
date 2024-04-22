package asd.project;

import asd.project.serializers.ISerializer;
import asd.project.serializers.JsonSerializer;
import asd.project.services.DropboxService;
import asd.project.services.IDropboxService;
import asd.project.sqlite.SQLiteStorage;
import asd.project.world.chunk.bsp.BSPChunkGenerator;
import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    IWorldGenerator worldGenerator = new BSPChunkGenerator();

    bind(INetwork.class).to(Network.class);
    bind(IAgent.class).to(AgentController.class);
    bind(IWorldGenerator.class).toInstance(worldGenerator);
    bind(IUI.class).to(UI.class);
    bind(IStorage.class).to(SQLiteStorage.class);

    bind(IDropboxService.class).to(DropboxService.class);
    bind(ISerializer.class).to(JsonSerializer.class);
  }
}
