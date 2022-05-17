package dev.vality.proxy.mocketbank.apm.service.impl.extractors;

import dev.vality.proxy.mocketbank.apm.model.ApmDestinationResource;
import dev.vality.proxy.mocketbank.apm.service.DestinationExtractor;
import dev.vality.swag.wallets.model.wallets.BankTransferSEPA;
import dev.vality.swag.wallets.model.wallets.DestinationResource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankTransferSepaExtractor implements DestinationExtractor {

    private final DestinationExtractor next;

    @Override
    public ApmDestinationResource extractSource(DestinationResource destinationResource) {
        if (destinationResource.getType().equals(DestinationResource.TypeEnum.BANKTRANSFERSEPA)) {
            BankTransferSEPA bankTransfer = (BankTransferSEPA) destinationResource;
            return ApmDestinationResource.builder()
                    .id(bankTransfer.getAccountNumber())
                    .build();
        }
        return next.extractSource(destinationResource);
    }
}