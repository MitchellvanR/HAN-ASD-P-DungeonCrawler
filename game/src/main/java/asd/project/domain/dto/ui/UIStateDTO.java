package asd.project.domain.dto.ui;

/**
 * Abstract DTO for sending states to the UI.
 */
public abstract class UIStateDTO {

  private final UIState uiState;

  protected UIStateDTO(UIState uiState) {
    this.uiState = uiState;
  }

  public UIState getUIState() {
    return uiState;
  }
}
