package asd.project.domain.dto.ui;

import static asd.project.domain.dto.ui.UIState.HOST_GAME_STATE;

import java.util.Arrays;

public class UIHostGameStateDTO extends UIConfigurationStateDTO {

  public UIHostGameStateDTO() {
    super(HOST_GAME_STATE);
    UILogoDTO logoDTO = new UILogoDTO();
    String[] logo = logoDTO.getLogo();
    promptList.addAll(Arrays.asList(logo));
    promptList.add("You've chosen to host a game!");
    promptList.add("Do you wish to host an existing game or a new game?");
    promptList.add("Type [/existing {roomCode}] or [/new {your_name}]");
  }


}
