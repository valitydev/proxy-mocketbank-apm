package dev.vality.proxy.mocketbank.apm.service;

import dev.vality.damsel.proxy_provider.PaymentContext;
import dev.vality.damsel.proxy_provider.PaymentProxyResult;

public interface PaymentCheckoutService {

    PaymentProxyResult checkout(PaymentContext context);
}
