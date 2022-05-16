package dev.vality.proxy.mocketbank.apm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ProxyMocketbankApmApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyMocketbankApmApplication.class, args);
    }

}
