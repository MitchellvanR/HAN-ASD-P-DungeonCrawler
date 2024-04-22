package asd.project.command;

import asd.project.domain.Message;
import java.awt.Color;

public enum CommandError {
  ERROR_INVALID_COMMAND(
      "The command doesn't exist!"),
  ERROR_INVALID_CODE(
      "Invalid room code!"),
  ERROR_INVALID_NAME(
      "Please enter a valid username!"),
  ERROR_IP_CONNECTION(
      "Can't connect to ip!"),
  INFO_SEARCHING_HOST(
      "Host disconnected. Searching for new host..."),
  INFO_COMMAND_USAGE_WHILE_SEARCHING_HOST(
      "Usage of text commands is inhibited while searching for a new host!"),
  INFO_HOST_FOUND(
      "New host found. Usage of text commands is no longer inhibited!"),
  ERROR_NO_INTERNET(
      "You are not connected to the internet!"),
  ERROR_SEND_MESSAGE(
      "An error occurred while trying to send a message"),
  ERROR_CLIENT_STARTGAME(
      "Only the host can start the game!"),
  ERROR_INVALID_ARGUMENTS(
      "Invalid arguments!"),
  ERROR_CREATE_ROOM(
      "Can't create room!"),
  ERROR_CLIENT_CONFIG(
      "Only the host is allowed to change the configuration settings."
  ),
  ERROR_INVALID_SEED(
      "Seed was invalid!"),
  ;

  private final String errorMessage;

  CommandError(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Message toMessage() {
    return new Message(errorMessage, Color.RED);
  }

  public Message toWarning() { return new Message(errorMessage, Color.ORANGE); }
}
