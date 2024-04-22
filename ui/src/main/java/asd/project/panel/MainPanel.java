package asd.project.panel;

import static asd.project.domain.dto.ui.UIState.ACTIVE_STATE;
import static asd.project.domain.dto.ui.UIState.DEATH_STATE;
import static asd.project.domain.dto.ui.UIState.END_STATE;
import static asd.project.domain.dto.ui.UIState.HOST_GAME_STATE;
import static asd.project.domain.dto.ui.UIState.LOBBY_STATE;
import static asd.project.domain.dto.ui.UIState.START_STATE;

import asd.project.domain.dto.ui.UIStartStateDTO;
import asd.project.domain.dto.ui.UIState;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.statepainter.ActiveStatePainter;
import asd.project.statepainter.ConfigurationStatePainter;
import asd.project.statepainter.DeathStatePainter;
import asd.project.statepainter.IStatePainter;
import asd.project.statepainter.LobbyStatePainter;
import asd.project.statepainter.WinStatePainter;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

public class MainPanel extends Panel {

  private final Map<UIState, IStatePainter> statePainterMap;
  private IStatePainter statePainter;
  private UIStateDTO stateDTO;

  public MainPanel(int width, int height) {
    super(width, height);
    statePainterMap = new HashMap<>();
    statePainterMap.put(START_STATE, new ConfigurationStatePainter());
    statePainterMap.put(HOST_GAME_STATE, new ConfigurationStatePainter());
    statePainterMap.put(LOBBY_STATE, new LobbyStatePainter(this));
    statePainterMap.put(ACTIVE_STATE, new ActiveStatePainter());
    statePainterMap.put(DEATH_STATE, new DeathStatePainter(this));
    statePainterMap.put(END_STATE, new WinStatePainter(this));
    setStatePainter(new UIStartStateDTO());
  }

  @Override
  public final void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    statePainter.paintComponent(graphics, stateDTO);
  }

  public void setStatePainter(UIStateDTO stateDTO) {
    statePainter = statePainterMap.get(stateDTO.getUIState());
    this.stateDTO = stateDTO;
  }

  public void setStateDTO(UIStateDTO stateDTO) {
    this.stateDTO = stateDTO;
  }
}
