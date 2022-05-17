package dev.vality.proxy.mocketbank.apm.model;

import dev.vality.adapter.bank.payout.spring.boot.starter.model.EntryStateModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EntryStateModelImpl extends EntryStateModel {

    private ApmDestinationResource resource;

}
