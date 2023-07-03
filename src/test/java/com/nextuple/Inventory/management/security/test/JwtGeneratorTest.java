package com.nextuple.Inventory.management.security.test;

import com.nextuple.Inventory.management.security.JwtGenerator;
import com.nextuple.Inventory.management.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtGeneratorTest {

    private static final String USERNAME = "testUser";
    private static final String SECRET = "testSecret";
    private static final long EXPIRATION = 3600000L;

    @Mock
    private Authentication authentication;

    private JwtGenerator jwtGenerator;

    @Before
    public void setUp() {
        jwtGenerator = new JwtGenerator();
        SecurityConstants.JWT_SECRET = SECRET;
        SecurityConstants.JWT_EXPIRATION = EXPIRATION;
    }

    @Test
    public void testGenerateToken() {
        when(authentication.getName()).thenReturn(USERNAME);

        String token = jwtGenerator.generateToken(authentication);

        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        assertEquals(USERNAME, subject);
    }

    @Test
    public void testGetUsernameFromJWT() {
        String token = Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        String username = jwtGenerator.getUsernameFromJWT(token);
        assertEquals(USERNAME, username);
    }

    @Test
    public void testValidateToken_validToken() {
        String token = Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        boolean isValid = jwtGenerator.validateToken(token);
        assertTrue(isValid);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void testValidateToken_invalidToken() {
        String invalidToken = "invalidToken";

        jwtGenerator.validateToken(invalidToken);
    }
}
