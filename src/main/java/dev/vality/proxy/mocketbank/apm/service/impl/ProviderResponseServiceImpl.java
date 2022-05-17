package dev.vality.proxy.mocketbank.apm.service.impl;

import dev.vality.damsel.domain.Failure;
import dev.vality.damsel.domain.TransactionInfo;
import dev.vality.damsel.proxy_provider.*;
import dev.vality.error.mapping.ErrorMapping;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;
import dev.vality.proxy.mocketbank.apm.service.ProviderResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static dev.vality.proxy.mocketbank.apm.util.CallbackUtils.createCallbackResult;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderResponseServiceImpl implements ProviderResponseService {

    private final ErrorMapping errorMapping;

    @Override
    public PaymentCallbackResult handle(PaymentContext context, ProviderResponse providerResponse) {
        log.info("Process provider response with transactionId: {}", providerResponse.getTransactionId());
        TransactionStatus transactionStatus = providerResponse.getStatus();
        return switch (transactionStatus) {
            case SUCCESS -> createSuccessResult(context, providerResponse);
            case FAILURE -> createFailureCallbackResult(context, providerResponse);
        };
    }

    private PaymentCallbackResult createSuccessResult(PaymentContext context,
                                                      ProviderResponse providerResponse) {
        log.info("The payment with transactionId {} is SUCCESSFUL", providerResponse.getTransactionId());
        return createCallbackResult(
                Intent.finish(new FinishIntent(FinishStatus.success(new Success()))),
                getState(context),
                createTransactionInfo(providerResponse)
        );
    }

    private byte[] getState(Object context) {
        if (context instanceof RecurrentTokenContext) {
            if (((RecurrentTokenContext) context).getSession() == null) {
                return new byte[0];
            }
            return ((RecurrentTokenContext) context).getSession().getState();
        }
        return ((PaymentContext) context).getSession().getState();
    }

    private TransactionInfo createTransactionInfo(ProviderResponse providerResponse) {
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setId(providerResponse.getTransactionId());
        transactionInfo.setExtra(Collections.emptyMap());
        return transactionInfo;
    }

    private PaymentCallbackResult createFailureCallbackResult(PaymentContext context,
                                                              ProviderResponse providerResponse) {
        Failure failure = errorMapping.mapFailure(providerResponse.getErrorCode(), providerResponse.getErrorMessage());
        log.info("The payment with transactionId {} is FAILING", providerResponse.getTransactionId());
        return createCallbackResult(
                Intent.finish(new FinishIntent(FinishStatus.failure(failure))),
                getState(context),
                createTransactionInfo(providerResponse)
        );
    }
}
