package dev.vality.proxy.mocketbank.apm.service;

import dev.vality.proxy.mocketbank.apm.model.ApmDestinationResource;
import dev.vality.swag.wallets.model.wallets.DestinationResource;

public interface DestinationExtractor {

    ApmDestinationResource extractSource(DestinationResource destinationResource);
}
