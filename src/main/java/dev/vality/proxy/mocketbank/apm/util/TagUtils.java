package dev.vality.proxy.mocketbank.apm.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class TagUtils {

    public static String addPrefix(String string, String prefix) {
        return prefix + string;
    }

}
