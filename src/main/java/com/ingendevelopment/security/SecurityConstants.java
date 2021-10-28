package com.ingendevelopment.security;

/**
 * Created by Brian Smith on 10/25/21.
 * Description:
 */
public class SecurityConstants {

    public static final String SECRET = "スプリングボート";
    public static final long EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
}
