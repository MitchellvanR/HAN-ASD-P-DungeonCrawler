package asd.project.world;

import asd.project.config.world.ChunkGenerationConfiguration;
import asd.project.config.world.WorldGeneratorConfiguration;

public record WorldGenerationConfiguration(WorldGeneratorConfiguration worldConfiguration,
                                           ChunkGenerationConfiguration chunkConfiguration) {

}
