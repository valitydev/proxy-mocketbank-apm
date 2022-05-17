package dev.vality.proxy.mocketbank.apm.validator;

import dev.vality.adapter.bank.payout.spring.boot.starter.exception.ValidationException;
import dev.vality.adapter.bank.payout.spring.boot.starter.validator.WithdrawalValidator;
import dev.vality.damsel.msgpack.Value;
import dev.vality.damsel.withdrawals.provider_adapter.Withdrawal;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class ValidatorImpl implements WithdrawalValidator {

    @Override
    public void validate(Withdrawal withdrawal, Value state, Map<String, String> options) throws ValidationException {
        Objects.requireNonNull(withdrawal.getId(), "Field 'withdrawalId' can't be null");
    }
}
