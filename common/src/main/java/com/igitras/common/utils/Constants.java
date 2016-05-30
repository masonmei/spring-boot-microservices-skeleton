package com.igitras.common.utils;

/**
 * @author mason
 */
public class Constants {

    public static class Profile {
        public static final String DEV = "dev";
        public static final String TEST = "test";
        public static final String PROD = "product";
    }

    public static class DataFormatter {
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String DATE_TIME_FORMAT = DATE_FORMAT
                + "'T'"
                + TIME_FORMAT
                + ".SSS'Z'";
    }

    public static class Constrains {
        public static final String LOGIN_REGEX = "^[A-Za-z0-9-_@.]*$";
    }
}
