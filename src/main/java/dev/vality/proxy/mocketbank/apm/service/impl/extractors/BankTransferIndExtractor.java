package dev.vality.proxy.mocketbank.apm.service.impl.extractors;

import dev.vality.proxy.mocketbank.apm.model.ApmDestinationResource;
import dev.vality.proxy.mocketbank.apm.service.DestinationExtractor;
import dev.vality.swag.wallets.model.wallets.BankTransferIND;
import dev.vality.swag.wallets.model.wallets.DestinationResource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankTransferIndExtractor implements DestinationExtractor {

    private final DestinationExtractor next;

    @Override
    public ApmDestinationResource extractSource(DestinationResource destinationResource) {
        if (destinationResource.getType().equals(DestinationResource.TypeEnum.BANKTRANSFERIND)) {
            BankTransferIND bankTransfer = (BankTransferIND) destinationResource;
            return ApmDestinationResource.builder()
                    .id(bankTransfer.getAccountNumber())
                    .accountName(bankTransfer.getAccountName())
                    .bankCode(bankTransfer.getBank().getBranchCode())
                    .bankName(bankTransfer.getBank().getName())
                    .build();
        }
        return next.extractSource(destinationResource);
    }
}
