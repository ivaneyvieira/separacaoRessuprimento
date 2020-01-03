package br.com.astrosoft.framework.security

import br.com.astrosoft.framework.security.SecurityUtils.isFrameworkInternalRequest
import br.com.astrosoft.framework.view.LoginView
import com.vaadin.flow.server.VaadinServletRequest
import com.vaadin.flow.server.VaadinServletResponse
import org.springframework.security.web.savedrequest.DefaultSavedRequest
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * HttpSessionRequestCache that avoids saving internal framework requests.
 */
class CustomRequestCache: HttpSessionRequestCache() {
  /**
   * {@inheritDoc}
   *
   * If the method is considered an internal request from the framework, we skip
   * saving it.
   *
   * @see SecurityUtils.isFrameworkInternalRequest
   */
  override fun saveRequest(request: HttpServletRequest, response: HttpServletResponse) {
    if(!isFrameworkInternalRequest(request)) {
      super.saveRequest(request, response)
    }
  }
  
  fun resolveRedirectUrl(): String? {
    val savedRequest =
      getRequest(VaadinServletRequest.getCurrent().httpServletRequest,
                 VaadinServletResponse.getCurrent().httpServletResponse)
    if(savedRequest is DefaultSavedRequest) {
      val requestURI = savedRequest.requestURI //
      // check for valid URI and prevent redirecting to the login view
      if(requestURI != null && !requestURI.isEmpty() && !requestURI.contains(LoginView.ROUTE)) { //
        return if(requestURI.startsWith("/")) requestURI.substring(1) else requestURI //
      }
    }
    // if everything fails, redirect to the main view
    return ""
  }
}