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
public class ErrorProcessor implements Processor<ProviderResponse, EntryStateModelImpl, ExitStateModelImpl> {

    @Override
    public ExitStateModelImpl process(ProviderResponse response, EntryStateModelImpl entryStateModel) {
        ExitStateModelImpl exitStateModel = new ExitStateModelImpl();
        exitStateModel.setEntryStateModel(entryStateModel);
        exitStateModel.setNextState(entryStateModel.getState());
        if (hasError(response)) {
            exitStateModel.setErrorCode(response.getErrorCode());
            exitStateModel.setErrorMessage(response.getErrorMessage());
        }
        return exitStateModel;
    }

    private boolean hasError(ProviderResponse response) {
        return Objects.nonNull(response.getStatus())
                && TransactionStatus.SUCCESS != response.getStatus();
    }
}
