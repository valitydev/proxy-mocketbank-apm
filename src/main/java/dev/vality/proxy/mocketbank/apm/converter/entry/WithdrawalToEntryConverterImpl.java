package dev.vality.proxy.mocketbank.apm.converter.entry;

import dev.vality.adapter.bank.payout.spring.boot.starter.converter.WithdrawalToEntryStateConverter;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.AdapterState;
import dev.vality.adapter.bank.payout.spring.boot.starter.state.deserializer.AdapterStateDeserializer;
import dev.vality.damsel.base.Content;
import dev.vality.damsel.domain.GenericPaymentTool;
import dev.vality.damsel.msgpack.Value;
import dev.vality.damsel.withdrawals.domain.Destination;
import dev.vality.damsel.withdrawals.provider_adapter.Withdrawal;
import dev.vality.proxy.mocketbank.apm.model.ApmDestinationResource;
import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.service.DestinationExtractor;
import dev.vality.proxy.mocketbank.apm.util.JsonUtils;
import dev.vality.swag.wallets.model.wallets.DestinationResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalToEntryConverterImpl implements WithdrawalToEntryStateConverter<EntryStateModelImpl> {

    private final AdapterStateDeserializer adapterStateDeserializer;
    private final DestinationExtractor destinationExtractorChain;

    @Override
    public EntryStateModelImpl convert(Withdrawal withdrawal, Value state, Map<String, String> options) {
        EntryStateModelImpl entryModel = new EntryStateModelImpl();
        byte[] data = (state != null && state.isSetBin() && state.getBin().length > 0) ? state.getBin() : null;
        AdapterState adapterState = adapterStateDeserializer.read(data);
        entryModel.setState(adapterState);
        ApmDestinationResource apmDestinationResource = getDestinationResource(withdrawal.getDestination());
        entryModel.setResource(apmDestinationResource);
        entryModel.setWithdrawalId(withdrawal.getId());
        entryModel.setAmount(withdrawal.getBody().getAmount());
        entryModel.setCurrencyCode(withdrawal.getBody().getCurrency().getSymbolicCode());
        entryModel.setOptions(options);
        return entryModel;
    }

    private ApmDestinationResource getDestinationResource(Destination destination) {
        if (!destination.isSetGeneric()) {
            log.warn("Not applicable destination");
            throw new IllegalStateException("Not applicable destination for adapter");
        }
        GenericPaymentTool genericPaymentTool = destination.getGeneric();
        Content content = genericPaymentTool.getData();
        if (ArrayUtils.isEmpty(content.getData())) {
            log.warn("PaymentTool is empty");
            throw new IllegalStateException("PaymentTool is empty");
        }
        DestinationResource destinationResource = JsonUtils.toObject(content.getData(), DestinationResource.class);
        return destinationExtractorChain.extractSource(destinationResource);
    }
}
