package com.igitras.uaa.custom.security;

import static org.springframework.util.StringUtils.isEmpty;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.Assert;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Redis Implementation of CSRF Token Repository.
 *
 * @author mason
 */
public class RedisCsrfTokenRepository implements CsrfTokenRepository {
    private static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";
    private static final String DEFAULT_CSRF_HEADER_NAME = "X-CSRF-TOKEN";
    private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = RedisCsrfTokenRepository.class.getName()
            .concat(".CSRF_TOKEN");
    private static final int DEFAULT_CSRF_TOKEN_CACHE_TIME = 300;

    private static final String CSRF_TOKEN_DELIMITER = "::";

    private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;
    private String headerName = DEFAULT_CSRF_HEADER_NAME;
    private String sessionAttributeName = DEFAULT_CSRF_TOKEN_ATTR_NAME;
    private int tokenCacheTimeInSecond = DEFAULT_CSRF_TOKEN_CACHE_TIME;

    private RedisTemplate<String, CsrfToken> redisTemplate;

    public RedisCsrfTokenRepository(RedisTemplate<String, CsrfToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*
         * (non-Javadoc)
         *
         * @see
         * org.springframework.security.web.csrf.CsrfTokenRepository#generateToken(javax.servlet
         * .http.HttpServletRequest)
         */
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(headerName, parameterName, createNewToken());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.web.csrf.CsrfTokenRepository#saveToken(org.springframework
     * .security.web.csrf.CsrfToken, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token != null) {
            String tokenString = token.getToken();
            String[] tokenPair = tokenString.split(CSRF_TOKEN_DELIMITER, 2);
            redisTemplate.opsForValue()
                    .set(buildKey(tokenPair[0]), token, tokenCacheTimeInSecond, SECONDS);
        }
    }

    private String buildKey(String tokenKey) {
        return format("%s.%s", sessionAttributeName, tokenKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.web.csrf.CsrfTokenRepository#loadToken(javax.servlet
     * .http.HttpServletRequest)
     */
    public CsrfToken loadToken(HttpServletRequest request) {
        String tokenPairString = request.getHeader(headerName);

        if (isEmpty(tokenPairString)) {
            tokenPairString = request.getParameter(parameterName);
        }

        if (isEmpty(tokenPairString)) {
            return null;
        }

        String[] tokenPair = tokenPairString.split(CSRF_TOKEN_DELIMITER, 2);

        CsrfToken token = redisTemplate.opsForValue().get(buildKey(tokenPair[0]));
        if (token != null) {
            redisTemplate.delete(buildKey(tokenPair[0]));
        }
        return token;
    }


    /**
     * Sets the {@link HttpServletRequest} parameter name that the {@link CsrfToken} is
     * expected to appear on
     *
     * @param parameterName the new parameter name to use
     */
    public void setParameterName(String parameterName) {
        Assert.hasLength(parameterName, "parameterName cannot be null or empty");
        this.parameterName = parameterName;
    }

    /**
     * Sets the header name that the {@link CsrfToken} is expected to appear on and the
     * header that the response will contain the {@link CsrfToken}.
     *
     * @param headerName the new header name to use
     */
    public void setHeaderName(String headerName) {
        Assert.hasLength(headerName, "headerName cannot be null or empty");
        this.headerName = headerName;
    }

    /**
     * Sets the {@link HttpSession} attribute name that the {@link CsrfToken} is stored in
     *
     * @param sessionAttributeName the new attribute name to use
     */
    public void setSessionAttributeName(String sessionAttributeName) {
        Assert.hasLength(sessionAttributeName, "sessionAttributename cannot be null or empty");
        this.sessionAttributeName = sessionAttributeName;
    }

    public RedisCsrfTokenRepository setTokenCacheTimeInSecond(int tokenCacheTimeInSecond) {
        this.tokenCacheTimeInSecond = tokenCacheTimeInSecond;
        return this;
    }

    private String createNewToken() {
        return format("%s%s%s", UUID.randomUUID()
                .toString(), CSRF_TOKEN_DELIMITER, UUID.randomUUID()
                .toString());
    }
}
