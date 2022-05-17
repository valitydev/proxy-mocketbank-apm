package dev.vality.proxy.mocketbank.apm.config;

import dev.vality.adapter.bank.payout.spring.boot.starter.processor.Processor;
import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ExitStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.processor.ErrorProcessor;
import dev.vality.proxy.mocketbank.apm.processor.SuccessProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfig {

    @Bean
    public Processor<ProviderResponse, EntryStateModelImpl, ExitStateModelImpl> responseProcessorChain() {
        ErrorProcessor errorProcessor = new ErrorProcessor();
        return new SuccessProcessor(errorProcessor);
    }

}
