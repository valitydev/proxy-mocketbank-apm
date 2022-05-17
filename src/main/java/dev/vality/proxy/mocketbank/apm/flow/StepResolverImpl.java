package dev.vality.proxy.mocketbank.apm.flow;

import dev.vality.adapter.bank.payout.spring.boot.starter.flow.StepResolver;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.Step;
import dev.vality.proxy.mocketbank.apm.model.EntryStateModelImpl;
import dev.vality.proxy.mocketbank.apm.model.ExitStateModelImpl;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class StepResolverImpl implements StepResolver<EntryStateModelImpl, ExitStateModelImpl> {

    @Override
    public Step resolveEntry(EntryStateModelImpl entryStateModel) {
        return Objects.requireNonNullElse(entryStateModel.getState().getStep(), Step.PAYOUT);
    }

    @Override
    public Step resolveExit(ExitStateModelImpl exitStateModel) {
        return exitStateModel.getEntryStateModel().getState().getStep();
    }

}
