package dev.vality.proxy.mocketbank.apm.service.impl;

import dev.vality.damsel.proxy_provider.PaymentCallbackResult;
import dev.vality.damsel.proxy_provider.PaymentContext;
import dev.vality.damsel.proxy_provider.PaymentProxyResult;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.serde.Deserializer;
import dev.vality.proxy.mocketbank.apm.service.PaymentCheckoutService;
import dev.vality.proxy.mocketbank.apm.service.ProviderProxyService;
import dev.vality.proxy.mocketbank.apm.service.ProviderResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderProxyServiceImpl implements ProviderProxyService {

    private final PaymentCheckoutService paymentCheckoutService;
    private final Deserializer<ProviderResponse> responseDeserializer;
    private final ProviderResponseService providerResponseService;

    @Override
    public PaymentProxyResult processPayment(PaymentContext paymentContext) {
        return paymentCheckoutService.checkout(paymentContext);
    }

    @Override
    public PaymentCallbackResult handlePaymentCallback(ByteBuffer byteBuffer, PaymentContext paymentContext) {
        ProviderResponse callback = responseDeserializer.read(byteBuffer.array());
        log.info("Received callback response: {}", callback);
        return providerResponseService.handle(paymentContext, callback);
    }
}
