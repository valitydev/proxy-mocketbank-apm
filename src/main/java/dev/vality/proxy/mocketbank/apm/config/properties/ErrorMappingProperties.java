package dev.vality.proxy.mocketbank.apm.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Data
@ConfigurationProperties(prefix = "error-mapping")
public class ErrorMappingProperties {

    private Resource filePath;
    private String pattern;
}
