package asd.project.domain.dto.serializable;

import java.io.Serializable;
import java.util.UUID;

public record WinGameDTO(UUID winnerUuid) implements Serializable {

}
