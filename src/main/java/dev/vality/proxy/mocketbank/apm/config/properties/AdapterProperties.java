package dev.vality.proxy.mocketbank.apm.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ConfigurationProperties(prefix = "adapter")
@Validated
public class AdapterProperties {

    @NotNull
    private String url;

    @NotNull
    private String finishRedirectUrl;

    @NotNull
    private String tagPrefix;

    @NotNull
    private Integer redirectTimeout;
}
