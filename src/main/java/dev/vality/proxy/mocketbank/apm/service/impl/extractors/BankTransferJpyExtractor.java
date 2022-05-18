package dev.vality.proxy.mocketbank.apm.service.impl.extractors;

import dev.vality.proxy.mocketbank.apm.model.ApmDestinationResource;
import dev.vality.proxy.mocketbank.apm.service.DestinationExtractor;
import dev.vality.swag.wallets.model.wallets.BankTransferJPY;
import dev.vality.swag.wallets.model.wallets.DestinationResource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankTransferJpyExtractor implements DestinationExtractor {

    private final DestinationExtractor next;

    @Override
    public ApmDestinationResource extractSource(DestinationResource destinationResource) {
        if (destinationResource.getType().equals(DestinationResource.TypeEnum.BANKTRANSFERJPY)) {
            BankTransferJPY bankTransfer = (BankTransferJPY) destinationResource;
            return ApmDestinationResource.builder()
                    .id(bankTransfer.getAccountNumber())
                    .accountName(bankTransfer.getAccountName())
                    .bankCode(bankTransfer.getBank().getCode())
                    .bankName(bankTransfer.getBank().getName())
                    .build();
        }
        return next.extractSource(destinationResource);
    }
}
