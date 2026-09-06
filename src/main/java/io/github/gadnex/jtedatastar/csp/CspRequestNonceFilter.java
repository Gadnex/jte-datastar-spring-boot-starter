package io.github.gadnex.jtedatastar.csp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.web.filter.OncePerRequestFilter;

/** Request filter to add a CSP nonce to the request */
public class CspRequestNonceFilter extends OncePerRequestFilter {

  /** CSP nonce request attribute name */
  public static final String NONCE_ATTRIBUTE = "cspNonce";

  private static final SecureRandom RANDOM = new SecureRandom();

  /** Default constructor */
  public CspRequestNonceFilter() {}

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getAttribute(NONCE_ATTRIBUTE) == null) {
      byte[] nonceBytes = new byte[16];
      RANDOM.nextBytes(nonceBytes);
      String nonce = Base64.getEncoder().encodeToString(nonceBytes);
      request.setAttribute(NONCE_ATTRIBUTE, nonce);
    }

    filterChain.doFilter(request, response);
  }
}
