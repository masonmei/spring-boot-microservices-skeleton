package com.igitras.common.utils;

/**
 * @author mason
 */
public abstract class Constants {

    public abstract static class Property {
        public static final String BASE = "project";
        public static final String ENABLED = "enabled";

        public static final String SWAGGER_ENABLED = BASE + ".swagger." + ENABLED;
        public static final String ACCESS_LOG_ENABLED = BASE + ".accesslog." + ENABLED;
    }

    public abstract static class Profile {
        public static final String DEV = "dev";
        public static final String PROD = "product";
        public static final String NATIVE = "native";

        public static final String NO_SWAGGER = "no-swagger";
    }

    public abstract static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_USER";
        public static final String EDITOR = "ROLE_EDITOR";
        public static final String DEVELOPER = "ROLE_DEVELOPER";

        public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    }

    public abstract static class DataFormatter {
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String DATE_TIME_FORMAT = DATE_FORMAT + "'T'" + TIME_FORMAT + ".SSS'Z'";
    }

    public abstract static class Constrains {
        public static final String LOGIN_REGEX = "^[A-Za-z0-9-_@.]*$";
    }

    public abstract static class Headers {
        public static final String AUTH_HEADER = "Authorization";
        public static final String BEARER = "Bearer";
    }

    public abstract static class Cookies {
        public static final String REFRESH_TOKEN = "RefreshToken";
        public static final int REFRESH_TOKEN_CACHES = 86400 * 30;
    }

    public abstract static class Params {
        public static final String TOKEN = "token";
    }

    public abstract static class Account {
        public static final String SYSTEM = "system";
    }
}
