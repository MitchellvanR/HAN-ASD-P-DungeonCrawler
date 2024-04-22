package asd.project;

import asd.project.domain.Message;
import asd.project.domain.dto.ui.UIStateDTO;
import asd.project.domain.entity.CharacterEntity;
import asd.project.domain.event.Event;
import io.reactivex.rxjava3.core.Observable;

/**
 * Interface for the UI
 */
public interface IUI {

  /**
   * Returns the observable for UI events.
   *
   * @return The observable for UI events.
   */
  Observable<Event> getObservable();

  /**
   * Set the state painter. This is to change what the user sees.
   *
   * @param stateDTO The state to which it has to be changed
   */
  void setStatePainter(UIStateDTO stateDTO);

  /**
   * Set the current UIStateDto.
   *
   * @param stateDTO The UIStateDto to which you want to set it.
   */
  void setStateDTO(UIStateDTO stateDTO);

  /**
   * Update the user stats. This is for showing the current stats of the user
   *
   * @param characterEntity The character of which the stats have to be updated
   */
  void updateUserStats(CharacterEntity characterEntity);

  /**
   * Show the username in the UI
   *
   * @param name The username that you want to show in the ui
   */
  void updateUsername(String name);

  /**
   * Show a new message in the UI
   *
   * @param message the message that was sent.
   */
  void updateMessage(Message message);
}
