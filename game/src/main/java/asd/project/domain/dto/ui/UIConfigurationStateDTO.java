package asd.project.domain.dto.ui;

import java.util.ArrayList;
import java.util.List;

public class UIConfigurationStateDTO extends UIStateDTO {

  protected final List<String> promptList;

  public UIConfigurationStateDTO(UIState uiState) {
    super(uiState);
    promptList = new ArrayList<>();
  }

  public List<String> getPromptList() {
    return promptList;
  }
}
