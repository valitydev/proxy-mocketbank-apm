package dev.vality.proxy.mocketbank.apm.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderResponse {

    private TransactionStatus status;
    private String transactionId;
    private String errorCode;
    private String errorMessage;
}
