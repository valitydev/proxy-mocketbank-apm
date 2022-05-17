package dev.vality.proxy.mocketbank.apm.util;

import dev.vality.damsel.domain.TransactionInfo;
import dev.vality.damsel.proxy_provider.Intent;
import dev.vality.damsel.proxy_provider.PaymentContext;
import dev.vality.damsel.proxy_provider.PaymentProxyResult;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentResults {

    public static PaymentProxyResult buildDefaultPaymentProxyResult(PaymentContext context,
                                                                    Intent intent) {
        String invoicePaymentId = PaymentContextUtils.getInvoicePaymentId(context.getPaymentInfo());
        return new PaymentProxyResult()
                .setIntent(intent)
                .setTrx(
                        new TransactionInfo()
                                .setId(invoicePaymentId)
                                .setExtra(context.getPaymentInfo().getPayment().getTrx().getExtra())
                );
    }
}
