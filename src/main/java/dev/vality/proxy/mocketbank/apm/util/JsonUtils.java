package dev.vality.proxy.mocketbank.apm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toObject(byte[] stringObject, Class<T> type) {
        try {
            return objectMapper.readValue(stringObject, type);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't convert  json string to object: ", e);
        }
    }
}
