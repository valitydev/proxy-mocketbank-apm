package dev.vality.proxy.mocketbank.apm.handler;

import dev.vality.adapter.bank.payout.spring.boot.starter.handler.HandleCallbackHandler;
import dev.vality.damsel.msgpack.Value;
import dev.vality.damsel.withdrawals.provider_adapter.Callback;
import dev.vality.damsel.withdrawals.provider_adapter.CallbackResult;
import dev.vality.damsel.withdrawals.provider_adapter.Withdrawal;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CallbackHandler implements HandleCallbackHandler {

    @Override
    public CallbackResult handleCallback(Callback callback, Withdrawal withdrawal, Value value,
                                         Map<String, String> map) {
        throw new RuntimeException("Not Supported handle");
    }
}
