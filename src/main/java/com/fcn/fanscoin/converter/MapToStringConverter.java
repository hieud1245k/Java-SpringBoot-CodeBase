package www.juiceclub.me.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Slf4j
@Converter
public class MapToStringConverter
    implements AttributeConverter<Map<String, String>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                                        .configure(ALLOW_UNQUOTED_FIELD_NAMES, false);

    @Override
    public String convertToDatabaseColumn(final Map<String, String> data) {
        String value = "";

        try {
            if (data != null) {
                value = OBJECT_MAPPER.writeValueAsString(data);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to convert data to String. {}", e.getMessage());
        }

        return value;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(final String data) {
        Map<String, String> mapValue = new HashMap<>();

        try {
            if (data != null) {
                mapValue = OBJECT_MAPPER.readValue(data, HashMap.class);
            }
        } catch (IOException e) {
            log.error("Failed to convert data to entity attribute. {}", e.getMessage());
        }

        return mapValue;
    }
}
