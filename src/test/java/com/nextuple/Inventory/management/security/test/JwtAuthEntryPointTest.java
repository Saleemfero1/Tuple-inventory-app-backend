package com.nextuple.Inventory.management.security.test;

import com.nextuple.Inventory.management.security.JwtAuthEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Test
    public void testCommence() throws IOException, ServletException {
        JwtAuthEntryPoint entryPoint = new JwtAuthEntryPoint();
        entryPoint.commence(request, response, authException);

        // Verify that the response sends the expected unauthorized error
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    // Add more test cases as needed
}
