package asd.project.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonSerializer implements ISerializer {

  private final Logger logger = Logger.getLogger(JsonSerializer.class.getName());
  private final JsonMapper mapper;

  @Inject
  public JsonSerializer(JsonMapper mapper) {
    this.mapper = mapper;
    configureMapper();
  }

  @Override
  public <T> String serialize(T obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException exception) {
      String exceptionMessage = String.format("Exception thrown in JsonSerializer.serialize: %s",
          exception.getMessage());
      logger.log(Level.SEVERE, exceptionMessage);
      return null;
    }
  }

  @Override
  public <T> T deserialize(Class<T> t, String json) {
    try {
      var type = mapper.constructType(t);
      return mapper.readerFor(type).readValue(json);
    } catch (JsonProcessingException exception) {
      String exceptionMessage = String.format("Exception thrown in JsonSerializer.deserialize: %s",
          exception.getMessage());
      logger.log(Level.SEVERE, exceptionMessage);
      return null;
    }
  }

  private void configureMapper() {
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
  }
}
