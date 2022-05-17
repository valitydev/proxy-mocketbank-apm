package dev.vality.proxy.mocketbank.apm.serde.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.serde.Deserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProviderResponseDeserializer implements Deserializer<ProviderResponse> {

    private final ObjectMapper mapper;

    @Override
    public ProviderResponse read(byte[] data) {
        if (data == null) {
            return new ProviderResponse();
        }
        try {
            return mapper.readValue(data, ProviderResponse.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}


