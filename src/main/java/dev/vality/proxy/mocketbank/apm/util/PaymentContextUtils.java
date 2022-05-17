package dev.vality.proxy.mocketbank.apm.util;

import dev.vality.damsel.proxy_provider.PaymentInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class PaymentContextUtils {

    private static final String SEPARATOR = ".";

    public static String getInvoicePaymentId(PaymentInfo paymentInfo) {
        return paymentInfo.getInvoice().getId() + SEPARATOR + paymentInfo.getPayment().getId();
    }
}
