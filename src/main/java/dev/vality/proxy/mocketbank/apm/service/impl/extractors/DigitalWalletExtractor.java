package dev.vality.proxy.mocketbank.apm.service.impl.extractors;

import dev.vality.proxy.mocketbank.apm.model.ApmDestinationResource;
import dev.vality.proxy.mocketbank.apm.service.DestinationExtractor;
import dev.vality.swag.wallets.model.wallets.DestinationResource;
import dev.vality.swag.wallets.model.wallets.DigitalWalletDestinationResource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DigitalWalletExtractor implements DestinationExtractor {

    private final DestinationExtractor next;

    @Override
    public ApmDestinationResource extractSource(DestinationResource destinationResource) {
        if (destinationResource.getType().equals(DestinationResource.TypeEnum.DIGITALWALLETDESTINATIONRESOURCE)) {
            DigitalWalletDestinationResource digitalWallet = (DigitalWalletDestinationResource) destinationResource;
            return ApmDestinationResource.builder()
                    .id(digitalWallet.getId())
                    .build();
        }
        return next.extractSource(destinationResource);
    }
}
