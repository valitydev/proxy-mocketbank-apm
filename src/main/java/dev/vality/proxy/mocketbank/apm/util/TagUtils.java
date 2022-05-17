package dev.vality.proxy.mocketbank.apm.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TagUtils {

    public static String addPrefix(String string, String prefix) {
        return prefix + string;
    }

}
