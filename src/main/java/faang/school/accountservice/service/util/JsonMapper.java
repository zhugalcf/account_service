package faang.school.accountservice.service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.exception.SerializeJsonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public <T> String toJsonArray(List<T> list) {
        try {
            Object[] objects = list.toArray();
            return objectMapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            throw new SerializeJsonException("Cannot serialize json");
        }
    }

    public String addToJsonArray(String json, Long newId) {
        try {
            Long[] ids = objectMapper.readValue(json, Long[].class);
            ids = Arrays.copyOf(ids, ids.length + 1);
            ids[ids.length - 1] = newId;
            return objectMapper.writeValueAsString(ids);
        } catch (JsonProcessingException e) {
            throw new SerializeJsonException("Cannot serialize json");
        }
    }
}
