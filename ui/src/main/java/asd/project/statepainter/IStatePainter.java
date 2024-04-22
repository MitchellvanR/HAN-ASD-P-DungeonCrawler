package asd.project.statepainter;

import asd.project.domain.dto.ui.UIStateDTO;
import java.awt.Graphics;

/**
 * Interface for state painters which are necessary for the UI to render different states of the
 * game.
 */
public interface IStatePainter {

  void paintComponent(Graphics graphics, UIStateDTO stateDTO);
}
