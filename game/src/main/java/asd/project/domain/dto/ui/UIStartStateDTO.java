package asd.project.domain.dto.ui;

import static asd.project.domain.dto.ui.UIState.START_STATE;

import java.util.Arrays;

public class UIStartStateDTO extends UIConfigurationStateDTO {

  public UIStartStateDTO() {
    super(START_STATE);
    UILogoDTO logoDTO = new UILogoDTO();
    String[] logo = logoDTO.getLogo();
    promptList.addAll(Arrays.asList(logo));
    promptList.add("Do you wish to join or host?");
    promptList.add("Type [/host] or [/join {roomCode} {your_name}]");
  }

}
