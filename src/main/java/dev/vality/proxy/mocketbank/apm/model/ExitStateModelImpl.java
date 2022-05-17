package dev.vality.proxy.mocketbank.apm.model;

import dev.vality.adapter.bank.payout.spring.boot.starter.model.ExitStateModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Класс с отличающимися от основных свойствами ExitStateModel.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExitStateModelImpl extends ExitStateModel {

    private String transactionID;
    private TransactionStatus status;
}
