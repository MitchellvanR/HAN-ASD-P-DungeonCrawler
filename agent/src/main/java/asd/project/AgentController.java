package asd.project;

import asd.project.compiler.Compiler;
import asd.project.domain.entity.Player;
import com.google.inject.Inject;

public class AgentController implements IAgent {

  private final Compiler compiler;
  private final Agent agent;

  @Inject
  public AgentController(Compiler compiler) {
    this.compiler = compiler;
    agent = new Agent();
    compiler.setAgent(agent);
  }

  @Override
  public void setBehaviour(String behaviour) {
    compiler.compile(behaviour);
  }

  @Override
  public void startAgent(Player player) {
    agent.perform(player);
  }
}
