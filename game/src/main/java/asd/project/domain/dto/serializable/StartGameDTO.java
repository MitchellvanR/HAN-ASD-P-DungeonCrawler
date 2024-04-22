package asd.project.domain.dto.serializable;

import java.io.Serializable;
import java.util.UUID;

public record StartGameDTO(int chunkWidth, int chunkHeight, int worldSeed,
                           UUID chunkUUID) implements Serializable {

}
