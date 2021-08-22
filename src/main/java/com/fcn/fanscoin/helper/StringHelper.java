package com.fcn.fanscoin.helper;

import org.apache.commons.lang3.RandomStringUtils;

public final class StringHelper {

    private StringHelper() {
    }

    public static String generateNoByNumeric(final String prefix, final Integer numberOfDigits) {
        return prefix.concat(RandomStringUtils.randomNumeric(numberOfDigits));
    }

    public static String generateNoByNumeric(final Integer numberOfDigits) {
        return RandomStringUtils.randomNumeric(numberOfDigits);
    }

    public static String generateNoByAlphanumeric(final Integer numberOfCharacters) {
        return RandomStringUtils.randomAlphanumeric(numberOfCharacters);
    }
}
