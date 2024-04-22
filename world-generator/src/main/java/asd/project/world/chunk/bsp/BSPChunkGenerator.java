package asd.project.world.chunk.bsp;

import asd.project.config.world.BSPChunkGenerationConfiguration;
import asd.project.domain.Chunk;
import asd.project.world.BaseWorldGenerator;
import asd.project.world.WorldGenerationConfiguration;
import asd.project.world.connections.DijkstraCorridorConnector;
import asd.project.world.connections.ICorridorConnector;
import asd.project.world.connections.msp.PrimMSPGenerator;
import asd.project.world.rooms.IRoomGenerator;
import asd.project.world.rooms.Room;
import asd.project.world.rooms.SimpleRoomGenerator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BSPChunkGenerator extends BaseWorldGenerator {

  private static final Logger LOGGER = Logger.getLogger(BSPChunkGenerator.class.getName());

  private IRoomGenerator roomGenerator;

  public BSPChunkGenerator() {
  }

  @Override
  protected Chunk internalGenerateChunk(WorldGenerationConfiguration worldGenerationConfiguration) {
    this.roomGenerator = new SimpleRoomGenerator();
    var config = (BSPChunkGenerationConfiguration) worldGenerationConfiguration.chunkConfiguration();

    Chunk chunk = new Chunk(worldGenerationConfiguration.worldConfiguration().chunkWidth(),
        worldGenerationConfiguration.worldConfiguration().chunkHeight(),
        worldGenerationConfiguration.chunkConfiguration().getChunkSeed(),
        worldGenerationConfiguration.chunkConfiguration().getChunkId());

    var roomList = getRoomList(chunk, config);

    var connections = new PrimMSPGenerator().generateConnections(roomList);
    ICorridorConnector roomConnector = new DijkstraCorridorConnector();
    connections.forEach(connection -> roomConnector.connectPositions(chunk, connection));

    return chunk;
  }

  private ArrayList<Room> getRoomList(Chunk chunk, BSPChunkGenerationConfiguration config) {
    BSPArea bspArea = new BSPArea(0, 0, chunk.getWidth(), chunk.getHeight(), chunk.getRandom(),
        config);
    bspArea.splitArea();

    var leafList = new ArrayList<BSPLeaf>();
    bspArea.getLeaves(leafList);

    var roomList = new ArrayList<Room>();

    leafList.forEach(bspLeaf -> {
      try {
        var room = roomGenerator.generateRoom(config.getRoomConfiguration(), chunk.getRandom());
        var roomPosition = bspLeaf.position();

        chunk.getGrid().placeGrid(roomPosition, room.getGrid());
        roomList.add(room);
      } catch (IllegalArgumentException exception) {
        String logMessage = "An error occurred while retrieving the room list: " + exception;
        LOGGER.log(Level.WARNING, logMessage);
      }
    });
    return roomList;
  }

  @Override
  protected boolean isConfigurationValid(WorldGenerationConfiguration configuration) {
    if (!(configuration.chunkConfiguration() instanceof BSPChunkGenerationConfiguration chunkConfiguration)) {
      return false;
    }
    var roomConfiguration = chunkConfiguration.getRoomConfiguration();
    if (roomConfiguration.minRoomHeight() > roomConfiguration.maxRoomHeight()
        || roomConfiguration.minRoomWidth() > roomConfiguration.maxRoomWidth()
        || roomConfiguration.minRoomHeight() < 0 || roomConfiguration.minRoomWidth() < 0) {
      return false;
    }

    if (roomConfiguration.minRoomHeight() > configuration.worldConfiguration().chunkWidth()
        || roomConfiguration.minRoomWidth() > configuration.worldConfiguration().chunkHeight()
        || roomConfiguration.maxRoomHeight() > configuration.worldConfiguration().chunkWidth()
        || roomConfiguration.maxRoomWidth() > configuration.worldConfiguration().chunkHeight()) {
      return false;
    }

    return roomConfiguration.minRoomWidth() <= chunkConfiguration.getWidth()
        && roomConfiguration.minRoomHeight() <= chunkConfiguration.getHeight()
        && roomConfiguration.maxRoomWidth() <= chunkConfiguration.getWidth()
        && roomConfiguration.maxRoomHeight() <= chunkConfiguration.getHeight();
  }
}