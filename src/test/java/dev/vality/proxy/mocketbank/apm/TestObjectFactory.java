package dev.vality.proxy.mocketbank.apm;

import dev.vality.damsel.base.Content;
import dev.vality.damsel.domain.*;
import dev.vality.damsel.proxy_provider.Cash;
import dev.vality.damsel.proxy_provider.Invoice;
import dev.vality.damsel.proxy_provider.InvoicePayment;
import dev.vality.damsel.proxy_provider.*;
import dev.vality.damsel.withdrawals.domain.Destination;
import dev.vality.damsel.withdrawals.provider_adapter.Withdrawal;
import dev.vality.proxy.mocketbank.apm.model.PaymentMode;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;
import dev.vality.swag.wallets.model.wallets.BankIND;
import dev.vality.swag.wallets.model.wallets.BankTransferIND;
import dev.vality.swag.wallets.model.wallets.DestinationResource;

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

    public static Withdrawal testWithdrawalWithoutBody() {
        return new Withdrawal()
                .setId(randomString())
                .setBody(new dev.vality.damsel.withdrawals.provider_adapter.Cash()
                        .setAmount(3200)
                        .setCurrency(new Currency()
                                .setSymbolicCode("INR")
                                .setExponent((short) 2)))
                .setDestination(new Destination(Destination.generic(new GenericPaymentTool()
                        .setData(new Content()))));
    }

    public static Withdrawal testWithdrawal(byte[] content) {
        return new Withdrawal()
                .setSessionId(randomString())
                .setId(randomString())
                .setBody(new dev.vality.damsel.withdrawals.provider_adapter.Cash()
                        .setAmount(3200)
                        .setCurrency(new Currency()
                                .setSymbolicCode("INR")
                                .setExponent((short) 2)))
                .setDestination(new Destination(Destination.generic(new GenericPaymentTool()
                        .setData(new Content().setData(content)))));
    }

    public static DestinationResource testBankTransferResource(String id) {
        DestinationResource destinationResource = testBankTransfer(id);
        destinationResource.setType(DestinationResource.TypeEnum.BANKTRANSFERIND);
        return destinationResource;
    }

    public static BankTransferIND testBankTransfer(String id) {
        BankTransferIND bankTransfer = new BankTransferIND();
        bankTransfer.setAccountNumber(id);
        bankTransfer.setAccountName(randomString());
        bankTransfer.setBank(testBank());
        bankTransfer.setTransferType(BankTransferIND.TransferTypeEnum.NEFT);
        return bankTransfer;
    }

    public static BankIND testBank() {
        BankIND bank = new BankIND();
        bank.setBranchCode(randomString());
        return bank;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
