package dev.vality.proxy.mocketbank.apm.controller;

import dev.vality.proxy.mocketbank.apm.config.properties.AdapterProperties;
import dev.vality.proxy.mocketbank.apm.model.CheckoutRequest;
import dev.vality.proxy.mocketbank.apm.model.ProviderResponse;
import dev.vality.proxy.mocketbank.apm.model.TransactionStatus;
import dev.vality.proxy.mocketbank.apm.serde.Serializer;
import dev.vality.proxy.mocketbank.apm.service.HellgateAdapterService;
import dev.vality.proxy.mocketbank.apm.util.TagUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static dev.vality.proxy.mocketbank.apm.constant.UrlPaths.*;

@Slf4j
@RestController
@RequestMapping("/${server.rest.endpoint}")
@RequiredArgsConstructor
public class AdapterController {

    private final HellgateAdapterService hellgateAdapterService;
    private final Serializer<ProviderResponse> responseSerializer;
    private final AdapterProperties properties;
    private final RestTemplate restTemplate;

    @Value("${server.rest.endpoint}")
    private String endpoint;

    @PostMapping(value = CALLBACK_PATH)
    public ResponseEntity<Object> processCallback(@RequestBody ProviderResponse callback) {
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

    @PostMapping(value = CHECKOUT_PATH, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView checkout(@ModelAttribute CheckoutRequest request) {
        log.info("Provider receive checkout request: {}", request);
        ModelAndView model = new ModelAndView();
        model.setViewName("apm");
        model.addObject("action", "/" + endpoint + CONFIRMATION_PATH);
        model.addObject("TrxId", request.getTransactionId());
        return model;
    }

    @PostMapping(value = CONFIRMATION_PATH, params = "success")
    public String successConfirm(HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) {
        try {
            String transactionId = servletRequest.getParameter("transactionId");
            log.info("Success payment with transactionId: {}", transactionId);
            ProviderResponse response = ProviderResponse.builder()
                    .status(TransactionStatus.SUCCESS)
                    .transactionId(transactionId)
                    .build();
            callback(response);
            String redirectUrl = properties.getFinishRedirectUrl();
            servletResponse.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.warn("Redirecting request error", e);
        }
        return "";
    }

    private void callback(ProviderResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = properties.getUrl() + CALLBACK_PATH;
        restTemplate.postForEntity(url, new HttpEntity<>(response, headers), Object.class);
    }

    @PostMapping(value = CONFIRMATION_PATH, params = "decline")
    public String declineConfirm(HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) {
        try {
            String transactionId = servletRequest.getParameter("transactionId");
            log.info("Decline payment with transactionId: {}", transactionId);
            ProviderResponse response = ProviderResponse.builder()
                    .status(TransactionStatus.FAILURE)
                    .transactionId(transactionId)
                    .errorCode("ERROR")
                    .errorMessage("ERROR")
                    .build();
            callback(response);
            String redirectUrl = properties.getFinishRedirectUrl();
            servletResponse.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.warn("Redirecting request error", e);
        }
        return "";
    }
}
