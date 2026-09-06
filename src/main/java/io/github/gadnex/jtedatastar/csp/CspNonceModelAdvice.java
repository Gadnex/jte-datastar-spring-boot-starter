package io.github.gadnex.jtedatastar.csp;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * ControllerAdvice class to get a nonce from the HTTP request and add it to the MVC model as a
 * parameter
 */
@ControllerAdvice
public class CspNonceModelAdvice {

  /** Default constructor */
  public CspNonceModelAdvice() {}

  /**
   * Get CSP nonce model attribute
   *
   * @param request The HTTP request
   * @return the nonce model attribute
   */
  @ModelAttribute("nonce")
  public String getCspNonce(HttpServletRequest request) {
    return (String) request.getAttribute(CspRequestNonceFilter.NONCE_ATTRIBUTE);
  }
}
