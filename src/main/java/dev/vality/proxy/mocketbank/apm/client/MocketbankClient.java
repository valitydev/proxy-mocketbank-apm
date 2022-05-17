package dev.vality.proxy.mocketbank.apm.client;

import dev.vality.proxy.mocketbank.apm.model.PayoutRequest;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;
import org.springframework.stereotype.Component;

@Component
public class MocketbankClient implements RemoteClient {

    public static final String SUCCESS_ID = "124701000032";
    public static final String FAILURE_ID = "124701000033";

    @Override
    public ProviderResponse payout(PayoutRequest request) {
        if (FAILURE_ID.equals(request.getAccountId())) {
            return ProviderResponse.builder()
                    .status(TransactionStatus.FAILURE)
                    .errorCode("ERROR")
                    .errorMessage("ERROR")
                    .transactionId(request.getTransactionId())
                    .build();
        }
        if (SUCCESS_ID.equals(request.getAccountId())) {
            return ProviderResponse.builder()
                    .status(TransactionStatus.SUCCESS)
                    .transactionId(request.getTransactionId())
                    .build();
        }
        return ProviderResponse.builder()
                .status(TransactionStatus.FAILURE)
                .errorCode("400")
                .errorMessage("Bad request")
                .transactionId(request.getTransactionId())
                .build();
    }
}
