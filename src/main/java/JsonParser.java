import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    private final ObjectMapper mapper;

    public JsonParser() {
        mapper = new ObjectMapper();
    }

    public <T> T parse(String jsonBody, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(jsonBody, valueType);
    }
}
