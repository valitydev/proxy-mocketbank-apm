package dev.vality.proxy.mocketbank.apm.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Getter
@Setter
@ConfigurationProperties(prefix = "hellgate.client.adapter")
public class HellgateAdapterClientProperties {

    @NotNull
    private Resource url;

    @NotNull
    private int networkTimeout = 5000;
}


