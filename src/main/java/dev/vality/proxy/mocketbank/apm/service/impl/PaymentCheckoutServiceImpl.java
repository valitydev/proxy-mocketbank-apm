package dev.vality.proxy.mocketbank.apm.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vality.damsel.base.Timer;
import dev.vality.damsel.domain.Failure;
import dev.vality.damsel.domain.TargetInvoicePaymentStatus;
import dev.vality.damsel.domain.TransactionInfo;
import dev.vality.damsel.proxy_provider.*;
import dev.vality.damsel.timeout_behaviour.TimeoutBehaviour;
import dev.vality.damsel.user_interaction.BrowserHTTPRequest;
import dev.vality.damsel.user_interaction.BrowserPostRequest;
import dev.vality.damsel.user_interaction.UserInteraction;
import dev.vality.error.mapping.ErrorMapping;
import dev.vality.proxy.mocketbank.apm.config.properties.AdapterProperties;
import dev.vality.proxy.mocketbank.apm.model.CheckoutRequest;
import dev.vality.proxy.mocketbank.apm.service.PaymentCheckoutService;
import dev.vality.proxy.mocketbank.apm.util.PaymentContextUtils;
import dev.vality.proxy.mocketbank.apm.util.PaymentResults;
import dev.vality.proxy.mocketbank.apm.util.TagUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static dev.vality.proxy.mocketbank.apm.constant.UrlPaths.CHECKOUT_PATH;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCheckoutServiceImpl implements PaymentCheckoutService {

    public static final String UNKNOWN_ERROR = "UNKNOWN";

    private final AdapterProperties adapterProperties;
    private final ObjectMapper objectMapper;
    private final Converter<PaymentContext, CheckoutRequest> checkoutRequestConverter;
    private final ErrorMapping errorMapping;

    @Override
    public PaymentProxyResult checkout(PaymentContext context) {
        TargetInvoicePaymentStatus targetPaymentStatus = context.getSession().getTarget();
        String invoicePaymentId = PaymentContextUtils.getInvoicePaymentId(context.getPaymentInfo());
        log.info("Current invoice payment status for paymentId: {} is {}", invoicePaymentId, targetPaymentStatus);
        if (targetPaymentStatus == null) {
            return handleProcessedPayment(context);
        }
        return switch (targetPaymentStatus.getSetField()) {
            case PROCESSED -> handleProcessedPayment(context);
            case CAPTURED -> handleCapturedPayment(context);
            default -> handleUnknownStatusPayment(context);
        };
    }

    private PaymentProxyResult handleProcessedPayment(PaymentContext context) {
        CheckoutRequest checkoutRequest = checkoutRequestConverter.convert(context);
        Intent intent = createIntentWithSuspendIntent(checkoutRequest);
        PaymentProxyResult paymentProxyResult = new PaymentProxyResult(intent);
        String merchantTransactionId = checkoutRequest.getTransactionId();
        paymentProxyResult.setTrx(
                new TransactionInfo()
                        .setId(merchantTransactionId)
                        .setExtra(new HashMap<>())
        );
        log.info("Process of a checkout creating is finished (transactionId: {}, paymentProxyResult: {})",
                merchantTransactionId, paymentProxyResult);
        return paymentProxyResult;
    }

    private Intent createIntentWithSuspendIntent(CheckoutRequest checkoutRequest) {
        Map<String, String> params = objectMapper.convertValue(checkoutRequest, new TypeReference<>() {
        });
        String tag = TagUtils.addPrefix(
                checkoutRequest.getTransactionId(),
                adapterProperties.getTagPrefix()
        );
        BrowserPostRequest browserPostRequest
                = new BrowserPostRequest(adapterProperties.getUrl() + CHECKOUT_PATH, params);
        UserInteraction userInteraction = UserInteraction.redirect(BrowserHTTPRequest.post_request(browserPostRequest));
        TimeoutBehaviour timeoutBehaviour = new TimeoutBehaviour();
        timeoutBehaviour.setCallback(ByteBuffer.wrap(new byte[0]));
        return Intent.suspend(
                new SuspendIntent(tag, Timer.timeout(adapterProperties.getRedirectTimeout()))
                        .setUserInteraction(userInteraction)
                        .setTimeoutBehaviour(timeoutBehaviour)
        );
    }

    private PaymentProxyResult handleCapturedPayment(PaymentContext context) {
        String invoicePaymentId = PaymentContextUtils.getInvoicePaymentId(context.getPaymentInfo());
        log.info("Process with paymentId {} is CAPTURED payment state", invoicePaymentId);
        Intent intent = Intent.finish(new FinishIntent(FinishStatus.success(new Success())));
        return PaymentResults.buildDefaultPaymentProxyResult(context, intent);
    }

    private PaymentProxyResult handleUnknownStatusPayment(PaymentContext context) {
        String invoicePaymentId = PaymentContextUtils.getInvoicePaymentId(context.getPaymentInfo());
        log.error("Unknown target payment status: '{}' for paymentId: {}",
                context.getSession().getTarget(), invoicePaymentId);
        Failure failure = errorMapping.mapFailure(UNKNOWN_ERROR, UNKNOWN_ERROR);
        Intent intent = Intent.finish(new FinishIntent(FinishStatus.failure(failure)));
        return PaymentResults.buildDefaultPaymentProxyResult(context, intent);
    }
}
