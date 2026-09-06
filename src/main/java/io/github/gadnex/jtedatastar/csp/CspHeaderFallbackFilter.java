package io.github.gadnex.jtedatastar.csp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/** HTTP Request Filter to apply a CSP header to the response if one is not already applied */
public class CspHeaderFallbackFilter extends OncePerRequestFilter {

  @SuppressWarnings("UastIncorrectHttpHeaderInspection")
  private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

  /** Default constructor */
  public CspHeaderFallbackFilter() {}

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String nonce = (String) request.getAttribute(CspRequestNonceFilter.NONCE_ATTRIBUTE);
    if (nonce != null && !response.containsHeader(CONTENT_SECURITY_POLICY)) {
      String cspHeader =
          String.format(
              "default-src 'self'; script-src 'self' 'nonce-%s'; trusted-types datastar; require-trusted-types-for 'script';",
              nonce);
      response.setHeader(CONTENT_SECURITY_POLICY, cspHeader);
    }

    filterChain.doFilter(request, response);
  }
}
