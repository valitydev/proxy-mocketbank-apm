package dev.vality.proxy.mocketbank.apm.service.impl;

import dev.vality.damsel.proxy_provider.ProviderProxyHostSrv;
import dev.vality.proxy.mocketbank.apm.service.HellgateAdapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
@Slf4j
@RequiredArgsConstructor
public class HellgateAdapterServiceImpl implements HellgateAdapterService {

    private final ProviderProxyHostSrv.Iface providerProxyHostSrv;

    @Override
    public ByteBuffer processPaymentCallback(String tag, ByteBuffer callback) {
        log.info("processPaymentCallback start with tag {}", tag);
        try {
            ByteBuffer callbackResponse = providerProxyHostSrv.processPaymentCallback(tag, callback);
            log.info("processPaymentCallback finish with tag {}", tag);
            return callbackResponse;
        } catch (TException ex) {
            log.error("Error call hellgate with tag {}", tag, ex);
            throw new RuntimeException("Exception call hellgate processPaymentCallback ", ex);
        }
    }

}
