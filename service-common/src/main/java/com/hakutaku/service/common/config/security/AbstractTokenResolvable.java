package com.hakutaku.service.common.config.security;

import com.hakutaku.service.common.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.mobile.device.Device;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public abstract class AbstractTokenResolvable implements TokenResolvable {

    protected String APP_NAME;

    protected String SECRET;

    protected int EXPIRES_IN;

    protected int MOBILE_EXPIRES_IN;

    protected String AUTH_HEADER;

    static final String AUDIENCE_UNKNOWN = "unknown";

    static final String AUDIENCE_WEB = "web";

    static final String AUDIENCE_MOBILE = "mobile";

    static final String AUDIENCE_TABLET = "tablet";

    protected SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public AbstractTokenResolvable(String APP_NAME, String SECRET, int EXPIRES_IN, int MOBILE_EXPIRES_IN, String AUTH_HEADER) {
        this.APP_NAME = APP_NAME;
        this.SECRET = SECRET;
        this.EXPIRES_IN = EXPIRES_IN;
        this.MOBILE_EXPIRES_IN = MOBILE_EXPIRES_IN;
        this.AUTH_HEADER = AUTH_HEADER;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return getAuthHeaderFromHeader(request);
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    @Override
    public String generateToken(Long userId, String username, Device device) {
        String audience = generateAudience(device);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setId(String.valueOf(userId))
                .setSubject(username)
                .setAudience(audience)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(device))
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    @Override
    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            userId = Long.valueOf(claims.getId());
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    protected String generateAudience(Device device) {
        String audience = AUDIENCE_UNKNOWN;
        if (device.isNormal()) {
            audience = AUDIENCE_WEB;
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET;
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE;
        }
        return audience;
    }

    protected Date generateExpirationDate(Device device) {
        long expiresIn = device.isTablet() || device.isMobile() ? MOBILE_EXPIRES_IN : EXPIRES_IN;
        return new Date(System.currentTimeMillis() + expiresIn * 1000);
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    @Override
    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        return username != null;

    }

    @Override
    public User getUserFromToken(String token) {
        String username = getUsernameFromToken(token);
        Long id = getUserIdFromToken(token);
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    @Override
    public String refreshToken(String token) {
        return null;
    }

    @Override
    public int getExpiredIn(Device device) {
        return device.isMobile() || device.isTablet() ? MOBILE_EXPIRES_IN : EXPIRES_IN;
    }


}
