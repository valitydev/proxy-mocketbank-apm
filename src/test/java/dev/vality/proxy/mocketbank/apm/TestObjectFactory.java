package dev.vality.proxy.mocketbank.apm;

import dev.vality.damsel.domain.*;
import dev.vality.damsel.proxy_provider.Cash;
import dev.vality.damsel.proxy_provider.Invoice;
import dev.vality.damsel.proxy_provider.InvoicePayment;
import dev.vality.damsel.proxy_provider.*;
import dev.vality.proxy.mocketbank.apm.model.PaymentMode;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public abstract class TestObjectFactory {

    public static PaymentContext testPaymentContext() {
        InvoicePayment invoicePayment = new InvoicePayment();
        invoicePayment.setId(randomString());
        invoicePayment.setContactInfo(new ContactInfo().setPhoneNumber(randomString()));
        PaymentService paymentService = new PaymentService();
        paymentService.setBrandName(randomString());
        paymentService.setCategory(PaymentMode.NBI.getCategory());
        invoicePayment.setPaymentService(paymentService);
        Cash cost = new Cash();
        cost.setAmount(100);
        cost.setCurrency(new Currency().setSymbolicCode("RUB"));
        invoicePayment.setCost(cost);
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setExtra(Collections.emptyMap());
        invoicePayment.setTrx(transactionInfo);
        Invoice invoice = new Invoice()
                .setDetails(new InvoiceDetails()
                        .setDescription(randomString())
                )
                .setId(randomString());
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPayment(invoicePayment);
        paymentInfo.setInvoice(invoice);
        Map<String, String> options = Collections.emptyMap();
        Session session = new Session(TargetInvoicePaymentStatus.processed(new InvoicePaymentProcessed()))
                .setState(new byte[0]);
        return new PaymentContext(session, paymentInfo)
                .setOptions(options);

    }

    public static ProviderResponse testProviderResponse(TransactionStatus trxStatus) {
        ProviderResponse response = new ProviderResponse();
        response.setTransactionId(randomString());
        response.setStatus(trxStatus);
        response.setErrorCode("ERROR");
        response.setErrorMessage("ERROR");
        return response;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
