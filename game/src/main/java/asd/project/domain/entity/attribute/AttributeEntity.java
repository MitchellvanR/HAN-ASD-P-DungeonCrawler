package asd.project.domain.entity.attribute;

import asd.project.config.CharacterConfiguration;
import asd.project.domain.Chunk;
import asd.project.domain.IInteractable;
import asd.project.domain.Position;
import asd.project.domain.entity.Entity;

/**
 * AttributeEntity is the base class for all entities that can be picked up by the player.
 * Implementing classes should override the pickUp method to define the effect of the attribute.
 */
public abstract class AttributeEntity extends Entity implements IInteractable {

  protected AttributeEntity(CharacterConfiguration character, Chunk chunk, Position position) {
    super(character, chunk, position);
  }
}

