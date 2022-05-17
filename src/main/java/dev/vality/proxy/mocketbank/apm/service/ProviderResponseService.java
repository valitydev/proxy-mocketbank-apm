package dev.vality.proxy.mocketbank.apm.service;

import dev.vality.damsel.proxy_provider.PaymentCallbackResult;
import dev.vality.damsel.proxy_provider.PaymentContext;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;

public interface ProviderResponseService {

    PaymentCallbackResult handle(PaymentContext context, ProviderResponse providerResponse);

}
