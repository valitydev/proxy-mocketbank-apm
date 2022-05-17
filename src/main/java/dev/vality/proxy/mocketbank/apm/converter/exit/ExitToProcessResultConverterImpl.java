package dev.vality.proxy.mocketbank.apm.converter.exit;

import dev.vality.adapter.bank.payout.spring.boot.starter.converter.ExitStateToProcessResultConverter;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.AdapterState;
import dev.vality.adapter.bank.payout.spring.boot.starter.model.TransactionInfo;
import dev.vality.adapter.bank.payout.spring.boot.starter.state.serializer.AdapterStateSerializer;
import dev.vality.damsel.domain.Failure;
import dev.vality.damsel.msgpack.Value;
import dev.vality.damsel.withdrawals.provider_adapter.*;
import dev.vality.error.mapping.ErrorMapping;
import dev.vality.proxy.mocketbank.apm.model.ExitStateModelImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExitToProcessResultConverterImpl implements ExitStateToProcessResultConverter<ExitStateModelImpl> {

    private final AdapterStateSerializer stateSerializer;
    private final ErrorMapping errorMapping;

    @Override
    public ProcessResult convert(ExitStateModelImpl exitStateModel) {
        log.info("ExitState converter. Exit state model: {}", exitStateModel);
        if (exitStateModel.getErrorCode() != null) {
            Failure failure = errorMapping.mapFailure(exitStateModel.getErrorCode(), exitStateModel.getErrorMessage());
            return new ProcessResult().setIntent(Intent.finish(new FinishIntent(FinishStatus.failure(failure))));
        }
        AdapterState nextState = buildAdapterState(exitStateModel);
        Intent intent = Intent.finish(new FinishIntent(FinishStatus.success(new Success())));
        ProcessResult processResult = new ProcessResult();
        processResult.setNextState(Value.bin(stateSerializer.writeByte(nextState)));
        processResult.setIntent(intent);
        log.info("ExitState converter. ProcessResult: {}", processResult);
        return processResult;
    }

    private AdapterState buildAdapterState(ExitStateModelImpl exitStateModel) {
        AdapterState nextState = exitStateModel.getNextState();
        TransactionInfo trxInfo = new TransactionInfo();
        trxInfo.setTrxId(exitStateModel.getTransactionID());
        trxInfo.setTrxExtra(Collections.emptyMap());
        nextState.setTrxInfo(trxInfo);
        return nextState;
    }
}

