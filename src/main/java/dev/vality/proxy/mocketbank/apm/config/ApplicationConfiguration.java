package dev.vality.proxy.mocketbank.apm.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.damsel.proxy_provider.ProviderProxyHostSrv;
import dev.vality.error.mapping.ErrorMapping;
import dev.vality.proxy.mocketbank.apm.config.properties.ErrorMappingProperties;
import dev.vality.proxy.mocketbank.apm.config.properties.HellgateAdapterClientProperties;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ProviderProxyHostSrv.Iface providerProxySrv(HellgateAdapterClientProperties properties) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(properties.getUrl().getURI())
                .withNetworkTimeout(properties.getNetworkTimeout())
                .build(ProviderProxyHostSrv.Iface.class);
    }

    @Bean
    public ErrorMapping errorMapping(ErrorMappingProperties errorMappingProperties) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        ErrorMapping errorMapping = new ErrorMapping(
                errorMappingProperties.getFilePath().getInputStream(),
                errorMappingProperties.getPattern(),
                mapper
        );
        errorMapping.validateMapping();
        return errorMapping;
    }
}
