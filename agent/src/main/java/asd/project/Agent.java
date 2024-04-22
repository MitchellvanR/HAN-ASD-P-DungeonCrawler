package asd.project;

import asd.project.ast.Behaviour;
import asd.project.ast.behaviour.AggressiveBehaviour;
import asd.project.ast.behaviour.DefensiveBehaviour;
import asd.project.ast.behaviour.PassiveBehaviour;
import asd.project.behaviour.IAggressiveBehaviour;
import asd.project.behaviour.IBehaviour;
import asd.project.behaviour.IDefensiveBehaviour;
import asd.project.behaviour.IPassiveBehaviour;
import asd.project.domain.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agent {

  private static final Logger LOGGER = Logger.getLogger(Agent.class.getName());
  private IBehaviour behaviour;
  private Map<Class<? extends Behaviour>, IBehaviour> behaviourMap;

  public Agent() {
    this.initializeBehaviourMap();
    this.behaviour = new IPassiveBehaviour();
  }

  private void initializeBehaviourMap() {
    behaviourMap = new HashMap<>();
    behaviourMap.put(PassiveBehaviour.class, new IPassiveBehaviour());
    behaviourMap.put(AggressiveBehaviour.class, new IAggressiveBehaviour());
    behaviourMap.put(DefensiveBehaviour.class, new IDefensiveBehaviour());
  }

  public void perform(
      Player player) {
    String logMessage = "Agent performing action for player: " + player.getUUID();
    LOGGER.log(Level.INFO, logMessage);
  }

  public void setBehaviour(Behaviour behaviour) {
    if (behaviourMap.containsKey(behaviour.getClass())) {
      String logMessage =
          "Behaviour changed from " + this.behaviour.getClass().getSimpleName() + " to I"
              + behaviour.getNodeLabel();
      LOGGER.log(Level.INFO, logMessage);

      this.behaviour = behaviourMap.get(behaviour.getClass());
    } else {
      String logWarning = "Unsupported behavior: " + behaviour.getNodeLabel();
      LOGGER.log(Level.WARNING, logWarning);
    }
  }

}
