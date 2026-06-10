package br.com.arq.utils;


import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestUtilsTest {

    @Test
    void deveRetornarIpDoHeaderXForwardedFor_quandoPresente() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("X-Forwarded-For"))
                .thenReturn("192.168.0.1");

        String ip = RequestUtils.getIp(request);

        assertEquals("192.168.0.1", ip);
    }

    @Test
    void deveRetornarPrimeiroIp_quandoMultiplosIpsNoHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("X-Forwarded-For"))
                .thenReturn("192.168.0.1, 10.0.0.1");

        String ip = RequestUtils.getIp(request);

        assertEquals("192.168.0.1", ip);
    }

    @Test
    void deveUsarRemoteAddr_quandoHeaderNaoExiste() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("X-Forwarded-For"))
                .thenReturn(null);
        when(request.getRemoteAddr())
                .thenReturn("127.0.0.1");

        String ip = RequestUtils.getIp(request);

        assertEquals("127.0.0.1", ip);
    }

    @Test
    void deveRetornarUserAgent() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("User-Agent"))
                .thenReturn("Mozilla/5.0");

        String userAgent = RequestUtils.getUserAgent(request);

        assertEquals("Mozilla/5.0", userAgent);
    }

    @Test
    void deveRetornarHost() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getServerName())
                .thenReturn("localhost");

        String host = RequestUtils.getHost(request);

        assertEquals("localhost", host);
    }

    @Test
    void deveRetornarPorta() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getServerPort())
                .thenReturn(8080);

        int port = RequestUtils.getPort(request);

        assertEquals(8080, port);
    }
}

