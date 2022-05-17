package dev.vality.proxy.mocketbank.apm.controller;

import dev.vality.proxy.mocketbank.apm.config.properties.AdapterProperties;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.serde.Serializer;
import dev.vality.proxy.mocketbank.apm.service.HellgateAdapterService;
import dev.vality.proxy.mocketbank.apm.util.TagUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/${server.rest.endpoint}")
@RequiredArgsConstructor
public class AdapterController {

    private final HellgateAdapterService hellgateAdapterService;
    private final Serializer<ProviderResponse> responseSerializer;
    private final AdapterProperties properties;

    @PostMapping(value = "/v1/callback")
    public ResponseEntity<Object> processCallback(@RequestBody ProviderResponse callback,
                                                  @RequestHeader HttpHeaders headers) {
        log.info("Receive provider callback: {}", callback);
        try {
            String paymentId = callback.getTransactionId();
            String tag = TagUtils.addPrefix(paymentId, properties.getTagPrefix());
            ByteBuffer callbackBuffer = ByteBuffer.wrap(responseSerializer.writeByte(callback));
            log.info("Get HG response for callback with tag: {}", tag);
            ByteBuffer response = hellgateAdapterService.processPaymentCallback(tag, callbackBuffer);
            log.info("Callback was processed with paymentId: {}, hgCallback: {}", paymentId,
                    new String(response.array(), StandardCharsets.UTF_8));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed handle callback for payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/v1/redirect")
    @ResponseStatus(HttpStatus.OK)
    public String receiveIncomingParameters(HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse) {
        String redirectUrl = properties.getFinishRedirectUrl();
        log.info("Redirect to page: {}", redirectUrl);
        try {
            servletResponse.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.warn("Redirecting request error", e);
        }
        return Strings.EMPTY;
    }
}
