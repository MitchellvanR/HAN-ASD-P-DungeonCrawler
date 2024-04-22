package asd.project.domain.dto.ui;

import static asd.project.domain.dto.ui.UIState.START_STATE;

public class UILogoDTO extends UIStateDTO {

  private final String[] logo = {
      "______                                    _____                    _           ",

      "|  _  \\                                  /  __ \\                  | |          ",
      "| | | |_   _ _ __   __ _  ___  ___  _ __ | /  \\/_ __ __ ___      _| | ___ _ __ ",

      "| | | | | | | '_ \\ / _` |/ _ \\/ _ \\| '_ \\| |   | '__/ _` \\ \\ /\\ / / |/ _ \\ '__|",

      "| |/ /| |_| | | | | (_| |  __/ (_) | | | | \\__/\\ | | (_| |\\ V  V /| |  __/ |   ",

      "|___/  \\__,_|_| |_|\\__, |\\___|\\___/|_| |_|\\____/_|  \\__,_| \\_/\\_/ |_|\\___|_|   ",
      "                    __/ |                                                      ",
      "                   |___/                                                       "};

  public UILogoDTO() {
    super(START_STATE);

  }

  public String[] getLogo() {
    return logo;
  }
}
