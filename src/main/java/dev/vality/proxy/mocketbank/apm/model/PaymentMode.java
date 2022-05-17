package dev.vality.proxy.mocketbank.apm.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@ToString
public enum PaymentMode {

    DC("Debit Card", "bankcard"),
    UPI("UPI", "upi"),
    NBI("NetBanking India", "netbanking");

    private final String fullName;
    private final String category;
    private static final Map<String, PaymentMode> CATEGORY_MAP = new HashMap<>();

    static {
        for (PaymentMode value : values()) {
            CATEGORY_MAP.put(value.getCategory(), value);
        }
    }

    public static PaymentMode getPaymentModeByCategory(String category) {
        PaymentMode paymentMode = CATEGORY_MAP.get(category.toLowerCase());
        if (paymentMode == null) {
            throw new IllegalArgumentException(String.format("PaymentMode for category '%s' not found!", category));
        }
        return paymentMode;
    }

}
