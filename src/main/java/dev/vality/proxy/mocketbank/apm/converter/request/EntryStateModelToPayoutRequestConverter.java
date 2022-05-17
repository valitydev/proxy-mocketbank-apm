package dev.vality.proxy.mocketbank.apm.converter.request;

import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.PayoutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class EntryStateModelToPayoutRequestConverter
        implements Converter<EntryStateModelImpl, PayoutRequest> {

    @Override
    public PayoutRequest convert(EntryStateModelImpl model) {
        String formattedAmount = (new BigDecimal(model.getAmount())).movePointLeft(2).toString();
        return PayoutRequest.builder()
                .transactionId(model.getWithdrawalId())
                .amount(formattedAmount)
                .currency(model.getCurrencyCode())
                .accountId(model.getResource().getId())
                .build();
    }
}
