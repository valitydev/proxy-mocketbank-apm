package dev.vality.proxy.mocketbank.apm;

import dev.vality.proxy.mocketbank.apm.config.properties.AdapterProperties;
import dev.vality.proxy.mocketbank.apm.config.properties.ErrorMappingProperties;
import dev.vality.proxy.mocketbank.apm.config.properties.HellgateAdapterClientProperties;
import dev.vality.proxy.mocketbank.apm.config.properties.RestTemplateProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
@EnableConfigurationProperties(value = {ErrorMappingProperties.class, HellgateAdapterClientProperties.class,
        RestTemplateProperties.class, AdapterProperties.class})
public class ProxyMocketbankApmApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyMocketbankApmApplication.class, args);
    }

}
