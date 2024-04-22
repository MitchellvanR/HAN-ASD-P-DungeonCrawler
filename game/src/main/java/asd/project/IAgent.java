package asd.project;

import asd.project.domain.entity.Player;

/**
 * This interface represents an agent. The implementation of this interface must be able to start an
 * agent.
 */
public interface IAgent {

  /**
   * This method sets the Behaviour of the agent.
   *
   * @param behaviour incoming agent behaviour from a Command
   */
  void setBehaviour(String behaviour);

  /**
   * This method starts an agent. This agent will replace the player that is given as parameter.
   *
   * @param player The player that the agent will replace.
   */
  void startAgent(Player player);
}
