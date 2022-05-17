package dev.vality.proxy.mocketbank.apm.config;

import dev.vality.proxy.mocketbank.apm.service.DestinationExtractor;
import dev.vality.proxy.mocketbank.apm.service.impl.extractors.BankTransferIndExtractor;
import dev.vality.proxy.mocketbank.apm.service.impl.extractors.BankTransferJpyExtractor;
import dev.vality.proxy.mocketbank.apm.service.impl.extractors.BankTransferSepaExtractor;
import dev.vality.proxy.mocketbank.apm.service.impl.extractors.DigitalWalletExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DestinationExtractorConfig {

    @Bean
    public DestinationExtractor destinationExtractorChain() {
        BankTransferJpyExtractor bankTransferJpyExtractor = new BankTransferJpyExtractor(null);
        BankTransferIndExtractor bankTransferIndExtractor = new BankTransferIndExtractor(bankTransferJpyExtractor);
        BankTransferSepaExtractor bankTransferSepaExtractor = new BankTransferSepaExtractor(bankTransferIndExtractor);
        return new DigitalWalletExtractor(bankTransferSepaExtractor);
    }

}
