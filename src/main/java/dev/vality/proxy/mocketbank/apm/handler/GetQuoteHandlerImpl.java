package dev.vality.proxy.mocketbank.apm.handler;

import dev.vality.adapter.bank.payout.spring.boot.starter.handler.GetQuoteHandler;
import dev.vality.damsel.withdrawals.provider_adapter.GetQuoteParams;
import dev.vality.damsel.withdrawals.provider_adapter.Quote;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetQuoteHandlerImpl implements GetQuoteHandler {

    @Override
    public Quote handle(GetQuoteParams getQuoteParams, Map<String, String> map) {
        throw new RuntimeException("Not Supported handle");
    }
}
