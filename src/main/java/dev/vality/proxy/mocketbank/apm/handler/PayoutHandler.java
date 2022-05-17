package dev.vality.proxy.mocketbank.apm.handler;

import dev.vality.adapter.bank.payout.spring.boot.starter.handler.CommonHandlerImpl;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.Step;
import dev.vality.adapter.bank.payout.spring.boot.starter.processor.Processor;
import dev.vality.proxy.mocketbank.apm.client.RemoteClient;
import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ExitStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.PayoutRequest;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PayoutHandler
        extends CommonHandlerImpl<PayoutRequest, ProviderResponse, EntryStateModelImpl, ExitStateModelImpl> {

    public PayoutHandler(RemoteClient remoteClient,
                         Converter<EntryStateModelImpl, PayoutRequest> converter,
                         Processor<ProviderResponse, EntryStateModelImpl, ExitStateModelImpl>
                                 responseProcessorChain) {
        super(remoteClient::payout, converter, responseProcessorChain);
    }

    @Override
    public boolean isHandle(EntryStateModelImpl entryStateModel) {
        return entryStateModel.getState().getStep() == Step.PAYOUT;
    }
}
