package dev.vality.proxy.mocketbank.apm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.adapter.bank.payout.spring.boot.starter.converter.WithdrawalToEntryStateConverter;
import dev.vality.adapter.bank.payout.spring.boot.starter.flow.StepResolver;
import dev.vality.adapter.bank.payout.spring.boot.starter.handler.CommonHandler;
import dev.vality.adapter.bank.payout.spring.boot.starter.handler.GetQuoteHandler;
import dev.vality.adapter.bank.payout.spring.boot.starter.handler.HandleCallbackHandler;
import dev.vality.adapter.bank.payout.spring.boot.starter.service.PayoutAdapterService;
import dev.vality.adapter.bank.payout.spring.boot.starter.service.PayoutAdapterServiceLogDecorator;
import dev.vality.adapter.bank.payout.spring.boot.starter.state.deserializer.AdapterStateDeserializer;
import dev.vality.adapter.bank.payout.spring.boot.starter.state.serializer.AdapterStateSerializer;
import dev.vality.adapter.bank.payout.spring.boot.starter.validator.WithdrawalValidator;
import dev.vality.damsel.proxy_provider.ProviderProxyHostSrv;
import dev.vality.damsel.withdrawals.provider_adapter.AdapterSrv;
import dev.vality.proxy.mocketbank.apm.config.properties.HellgateAdapterClientProperties;
import dev.vality.proxy.mocketbank.apm.converter.exit.ExitToProcessResultConverterImpl;
import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ExitStateModelImpl;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public ProviderProxyHostSrv.Iface providerProxySrv(HellgateAdapterClientProperties properties) throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(properties.getUrl().getURI())
                .withNetworkTimeout(properties.getNetworkTimeout())
                .build(ProviderProxyHostSrv.Iface.class);
    }

    @Bean
    public AdapterStateDeserializer adapterStateDeserializer(ObjectMapper objectMapper) {
        return new AdapterStateDeserializer(objectMapper);
    }

    @Bean
    public AdapterStateSerializer stateSerializer(ObjectMapper objectMapper) {
        return new AdapterStateSerializer(objectMapper);
    }

    @Bean
    public AdapterSrv.Iface payoutAdapterService(
            WithdrawalToEntryStateConverter<EntryStateModelImpl> withdrawalToEntryStateConverter,
            ExitToProcessResultConverterImpl exitStateToProcessResultConverter,
            List<CommonHandler<EntryStateModelImpl, ExitStateModelImpl>> handlers,
            StepResolver<EntryStateModelImpl, ExitStateModelImpl> stepResolver,
            WithdrawalValidator validator,
            GetQuoteHandler getQuoteHandler,
            HandleCallbackHandler callbackResult
    ) {
        return new PayoutAdapterService<>(
                withdrawalToEntryStateConverter,
                exitStateToProcessResultConverter,
                handlers,
                stepResolver,
                validator,
                getQuoteHandler,
                callbackResult
        );
    }

    @Bean
    @Primary
    public AdapterSrv.Iface payoutAdapterServiceLogDecorator(AdapterSrv.Iface payoutAdapterService) {
        return new PayoutAdapterServiceLogDecorator(payoutAdapterService);
    }
}
