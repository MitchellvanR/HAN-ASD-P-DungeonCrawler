package asd.project.domain;

import asd.project.domain.entity.CharacterEntity;
import java.util.UUID;

/**
 * Interface for all entities that can be interacted with This means that the entity can be picked
 * up by a character, or can be used by a character
 */
public interface IInteractable {

  /**
   * Interact with the entity
   *
   * @param characterEntity The character that interacts with the interactable
   */
  default void interact(CharacterEntity characterEntity) {
    publicInteract(characterEntity);

    var clientPlayerId = characterEntity.getChunk().getWorld().getPlayer().getUUID();
    if (validatePrivateInteraction(characterEntity.getUUID(), clientPlayerId)) {
      privateInteract(characterEntity);
    }
  }

  /**
   * The interaction that should *always* happen for every client in the network
   *
   * @param characterEntity The character that interacts with the interactable
   */
  void publicInteract(CharacterEntity characterEntity);

  /**
   * The interaction that should *only* happen for the local client
   *
   * @param characterEntity The character that interacts with the interactable
   */
  void privateInteract(CharacterEntity characterEntity);

  /**
   * Validate if this interaction is valid. A valid interaction is an interaction that's both public
   * and executed by the local client instance. This method should *NOT* be overridden.
   */
  default boolean validatePrivateInteraction(UUID clientPlayerId, UUID executingPlayerId) {
    return clientPlayerId == executingPlayerId;
  }
}