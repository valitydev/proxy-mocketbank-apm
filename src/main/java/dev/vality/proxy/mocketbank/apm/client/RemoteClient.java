package dev.vality.proxy.mocketbank.apm.client;

import dev.vality.proxy.mocketbank.apm.model.PayoutRequest;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;

public interface RemoteClient {

    ProviderResponse payout(PayoutRequest request);


}
