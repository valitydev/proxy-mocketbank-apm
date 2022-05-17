package dev.vality.proxy.mocketbank.apm.serde.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.serde.Serializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProviderResponseSerializer implements Serializer<ProviderResponse> {

    private final ObjectMapper mapper;

    @Override
    public byte[] writeByte(ProviderResponse obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
