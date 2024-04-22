package asd.project.domain.dto.serializable;

import java.awt.Color;
import java.io.Serializable;

public class MessageDTO implements Serializable {

  private final String value;
  private final Color color;

  public MessageDTO(String value, Color color) {
    this.value = value;
    this.color = color;
  }

  public MessageDTO(String value) {
    this(value, Color.WHITE);
  }

  public String getValue() {
    return value;
  }

  public Color getColor() {
    return color;
  }
}