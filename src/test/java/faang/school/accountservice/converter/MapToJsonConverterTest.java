package faang.school.accountservice.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MapToJsonConverterTest {

    @InjectMocks
    private MapToJsonConverter converter;

    @Test
    public void testConvertToDatabaseColumn() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("key1", "value1");
        testMap.put("key2", 123);

        String expectedJson = "{\"key1\":\"value1\",\"key2\":123}";

        String json = converter.convertToDatabaseColumn(testMap);

        assertEquals(expectedJson, json);
    }

    @Test
    public void testConvertToEntityAttribute() {
        String json = "{\"key1\":\"value1\",\"key2\":123}";

        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", 123);

        Map<String, Object> map = converter.convertToEntityAttribute(json);

        assertEquals(expectedMap, map);
    }

    @Test
    public void testConvertToEntityAttribute_WithInvalidJson() {
        String invalidJson = "invalid-json";
        assertThrows(RuntimeException.class, () -> converter.convertToEntityAttribute(invalidJson), "Error converting JSON to Map");
    }
}
