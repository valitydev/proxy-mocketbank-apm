package dev.vality.proxy.mocketbank.apm.processor;

import dev.vality.adapter.bank.payout.spring.boot.starter.processor.Processor;
import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ExitStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class SuccessProcessor implements Processor<ProviderResponse, EntryStateModelImpl, ExitStateModelImpl> {

    private final Processor<ProviderResponse, EntryStateModelImpl, ExitStateModelImpl> next;

    @Override
    public ExitStateModelImpl process(ProviderResponse response, EntryStateModelImpl entryStateModel) {
        if (isSuccess(response)) {
            log.info("Success response: {}", response);
            ExitStateModelImpl exitStateModel = new ExitStateModelImpl();
            exitStateModel.setEntryStateModel(entryStateModel);
            exitStateModel.setNextState(entryStateModel.getState());
            exitStateModel.setTransactionID(response.getTransactionId());
            exitStateModel.setStatus(response.getStatus());
            return exitStateModel;
        } else {
            log.info("Received not success response: {}", response);
            return next.process(response, entryStateModel);
        }
    }

    private boolean isSuccess(ProviderResponse response) {
        return Objects.nonNull(response)
                && Objects.nonNull(response.getStatus())
                && TransactionStatus.SUCCESS == response.getStatus();
    }
}
