package dev.vality.proxy.mocketbank.apm.util;

import dev.vality.damsel.domain.TransactionInfo;
import dev.vality.damsel.proxy_provider.Intent;
import dev.vality.damsel.proxy_provider.PaymentCallbackProxyResult;
import dev.vality.damsel.proxy_provider.PaymentCallbackResult;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CallbackUtils {

    public static PaymentCallbackResult createCallbackResult(Intent intent) {
        return createCallbackResult(intent, null, null);
    }

    public static PaymentCallbackResult createCallbackResult(Intent intent, byte[] nextState) {
        return createCallbackResult(intent, nextState, null);
    }

    public static PaymentCallbackResult createCallbackResult(Intent intent, byte[] nextState, TransactionInfo trx) {
        return new PaymentCallbackResult()
                .setResponse(new byte[0])
                .setResult(
                        new PaymentCallbackProxyResult()
                                .setIntent(intent)
                                .setNextState(nextState)
                                .setTrx(trx)
                );
    }
}
