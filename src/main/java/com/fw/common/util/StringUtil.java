package com.fw.common.util;

public class StringUtil {
    public static String simplifyErrMsg(String input) {

        String msg = null;
        // Define regex to match all non-alphanumeric characters except space
        String regex = "[^a-zA-Z0-9 .]";
        // Replace all special characters with an empty string
        msg = input.replaceAll(regex, "").trim();

        // If message too long, it need to sub
        if (msg.length() > 100) {
            msg = msg.substring(0, 100);
        }

        return msg;

    }
}
