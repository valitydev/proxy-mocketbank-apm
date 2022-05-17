package dev.vality.proxy.mocketbank.apm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutRequest {

    private String transactionId;
    private String amount;
    private String orderDescription;
    private String redirectUrl;
    private String notificationUrl;
    private String currency;
    private String paymentMode;
    private String paymentBrand;

}
