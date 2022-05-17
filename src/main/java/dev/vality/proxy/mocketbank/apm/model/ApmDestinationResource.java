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
public class ApmDestinationResource {

    private String accountName;
    private String accountNumber;
    private String bankName;
    private String bankCode;
    private String id;
    private String provider;
    private String token;
}
