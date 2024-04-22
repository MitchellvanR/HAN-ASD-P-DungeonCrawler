package asd.project.domain.dto.ui;

import static asd.project.domain.dto.ui.UIState.LOBBY_STATE;

import java.util.Arrays;

public class UILobbyStateDTO extends UIConfigurationStateDTO {

  private final String roomCode;

  public UILobbyStateDTO(String roomCode) {
    super(LOBBY_STATE);
    this.roomCode = roomCode;
    UILogoDTO logoDTO = new UILogoDTO();
    String[] logo = logoDTO.getLogo();
    promptList.addAll(Arrays.asList(logo));
  }

  public String getRoomCode() {
    return roomCode;
  }
}
