package dev.vality.proxy.mocketbank.apm.service;

import dev.vality.damsel.proxy_provider.ProviderProxySrv;
import dev.vality.damsel.proxy_provider.RecurrentTokenCallbackResult;
import dev.vality.damsel.proxy_provider.RecurrentTokenContext;
import dev.vality.damsel.proxy_provider.RecurrentTokenProxyResult;

import java.nio.ByteBuffer;

public interface ProviderProxyService extends ProviderProxySrv.Iface {

    @Override
    default RecurrentTokenProxyResult generateToken(RecurrentTokenContext recurrentTokenContext) {
        throw new IllegalStateException("Unsupported method");
    }

    @Override
    default RecurrentTokenCallbackResult handleRecurrentTokenCallback(ByteBuffer byteBuffer,
                                                                      RecurrentTokenContext tokenContext) {
        throw new IllegalStateException("Unsupported method");
    }

}
