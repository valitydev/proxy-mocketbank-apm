package dev.vality.proxy.mocketbank.apm.service;

import java.nio.ByteBuffer;

public interface HellgateAdapterService {

    ByteBuffer processPaymentCallback(String tag, ByteBuffer callback);

}
