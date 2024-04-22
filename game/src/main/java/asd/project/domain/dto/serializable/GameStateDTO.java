package asd.project.domain.dto.serializable;

import java.io.Serializable;
import java.util.UUID;

public record GameStateDTO(int x, int y, UUID playerUUID, UUID chunkUUID, int power, int health,
                           int money, int flagCount) implements Serializable {

}