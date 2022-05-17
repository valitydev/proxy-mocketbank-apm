package dev.vality.proxy.mocketbank.apm.converter;

import dev.vality.damsel.domain.PaymentService;
import dev.vality.damsel.proxy_provider.Cash;
import dev.vality.damsel.proxy_provider.Invoice;
import dev.vality.damsel.proxy_provider.InvoicePayment;
import dev.vality.damsel.proxy_provider.PaymentContext;
import dev.vality.proxy.mocketbank.apm.config.properties.AdapterProperties;
import dev.vality.proxy.mocketbank.apm.model.CheckoutRequest;
import dev.vality.proxy.mocketbank.apm.model.PaymentMode;
import dev.vality.proxy.mocketbank.apm.util.PaymentContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

import static dev.vality.proxy.mocketbank.apm.constant.OptionalField.PAYMENT_BRAND;
import static dev.vality.proxy.mocketbank.apm.constant.OptionalField.PAYMENT_MODE;

@Component
@RequiredArgsConstructor
public class PaymentContextToCheckoutRequestConverter implements Converter<PaymentContext, CheckoutRequest> {

    private final AdapterProperties properties;

    @Override
    public CheckoutRequest convert(PaymentContext context) {
        var paymentInfo = context.getPaymentInfo();
        Invoice invoice = paymentInfo.getInvoice();
        InvoicePayment invoicePayment = paymentInfo.getPayment();
        Cash cost = invoicePayment.getCost();
        String amount = formatAmount(cost);
        String invoicePaymentId = PaymentContextUtils.getInvoicePaymentId(paymentInfo);
        Map<String, String> options = context.getOptions();
        PaymentService paymentService = invoicePayment.getPaymentService();
        String paymentMode = options.getOrDefault(
                PAYMENT_MODE,
                PaymentMode.getPaymentModeByCategory(paymentService.getCategory()).name());
        String paymentBrand = options.getOrDefault(PAYMENT_BRAND, paymentService.getBrandName());
        return CheckoutRequest.builder()
                .redirectUrl(properties.getMerchantRedirectUrl())
                .notificationUrl(properties.getNotificationUrl())
                .amount(amount)
                .transactionId(invoicePaymentId)
                .orderDescription(invoice.getDetails().getDescription())
                .currency(cost.getCurrency().getSymbolicCode())
                .paymentMode(paymentMode)
                .paymentBrand(paymentBrand)
                .build();
    }

    private String formatAmount(Cash cost) {
        return (new BigDecimal(cost.getAmount())).movePointLeft(2).toString();
    }

}
