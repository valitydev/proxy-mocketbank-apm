package dev.vality.proxy.mocketbank.apm.service.impl;

import dev.vality.damsel.domain.InvoicePaymentCaptured;
import dev.vality.damsel.domain.TargetInvoicePaymentStatus;
import dev.vality.damsel.proxy_provider.*;
import dev.vality.proxy.mocketbank.apm.TestObjectFactory;
import dev.vality.proxy.mocketbank.apm.model.PaymentMode;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;
import dev.vality.proxy.mocketbank.apm.serde.Serializer;
import dev.vality.proxy.mocketbank.apm.service.ProviderProxyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.ByteBuffer;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProviderProxyServiceImplTest {

    @Autowired
    private ProviderProxyService providerProxyService;

    @Autowired
    private Serializer<ProviderResponse> responseSerializer;

    @Test
    void processProcessedPayment() throws Exception {
        PaymentContext paymentContext = TestObjectFactory.testPaymentContext();

        PaymentProxyResult paymentProxyResult = providerProxyService.processPayment(paymentContext);

        assertTrue(paymentProxyResult.getIntent().isSetSuspend());
        SuspendIntent suspendIntent = paymentProxyResult.getIntent().getSuspend();
        assertTrue(suspendIntent.isSetUserInteraction());
        Map<String, String> form = suspendIntent.getUserInteraction().getRedirect().getPostRequest().getForm();
        assertEquals(paymentContext.getPaymentInfo().getPayment().getPaymentService().getCategory(),
                PaymentMode.NBI.getCategory());
        assertEquals(paymentContext.getPaymentInfo().getPayment().getPaymentService().getBrandName(),
                form.get("paymentBrand"));
        assertTrue(suspendIntent.isSetTimeoutBehaviour());
        assertTrue(suspendIntent.getTimeoutBehaviour().isSetCallback());
    }

    @Test
    void processCapturedPayment() throws Exception {
        PaymentContext paymentContext = TestObjectFactory.testPaymentContext();
        Session session = new Session(TargetInvoicePaymentStatus.captured(new InvoicePaymentCaptured()))
                .setState(new byte[0]);
        paymentContext.setSession(session);

        PaymentProxyResult paymentProxyResult = providerProxyService.processPayment(paymentContext);

        assertTrue(paymentProxyResult.getIntent().isSetFinish());
        FinishIntent finishIntent = paymentProxyResult.getIntent().getFinish();
        assertTrue(finishIntent.isSetStatus());
        assertTrue(finishIntent.getStatus().isSetSuccess());
    }

    @Test
    void processCallbackFailureTest() throws Exception {
        PaymentContext paymentContext = TestObjectFactory.testPaymentContext();
        ProviderResponse callback = TestObjectFactory.testProviderResponse(TransactionStatus.FAILURE);
        ByteBuffer callbackBuffer = ByteBuffer.wrap(responseSerializer.writeByte(callback));

        PaymentCallbackResult paymentCallbackResult =
                providerProxyService.handlePaymentCallback(callbackBuffer, paymentContext);

        assertNotNull(paymentCallbackResult);
        assertTrue(paymentCallbackResult.getResult().getIntent().isSetFinish());
        assertTrue(paymentCallbackResult.getResult().getIntent().getFinish().getStatus().isSetFailure());
        assertEquals(callback.getTransactionId(), paymentCallbackResult.getResult().getTrx().getId());
    }

    @Test
    void processCallbackSuccessTest() throws Exception {
        PaymentContext paymentContext = TestObjectFactory.testPaymentContext();
        ProviderResponse callback = TestObjectFactory.testProviderResponse(TransactionStatus.SUCCESS);
        ByteBuffer callbackBuffer = ByteBuffer.wrap(responseSerializer.writeByte(callback));

        PaymentCallbackResult paymentCallbackResult =
                providerProxyService.handlePaymentCallback(callbackBuffer, paymentContext);

        assertNotNull(paymentCallbackResult);
        assertTrue(paymentCallbackResult.getResult().getIntent().isSetFinish());
        assertTrue(paymentCallbackResult.getResult().getIntent().getFinish().getStatus().isSetSuccess());
        assertEquals(callback.getTransactionId(), paymentCallbackResult.getResult().getTrx().getId());
    }
}