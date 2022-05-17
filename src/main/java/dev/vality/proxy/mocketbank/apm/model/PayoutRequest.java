package dev.vality.proxy.mocketbank.apm.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PayoutRequest {

    private String transactionId;
    private String amount;
    private String currency;
    private String accountId;
}
