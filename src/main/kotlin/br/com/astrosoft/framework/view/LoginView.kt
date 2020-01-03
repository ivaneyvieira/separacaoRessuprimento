package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.security.CustomRequestCache
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
@Component
class LoginView: VerticalLayout {
  val login = LoginFormApp()
  
  @Autowired
  constructor(authenticationManager: AuthenticationManager,
              requestCache: CustomRequestCache) {
    login.action = "login"
    login.isOpened = true
    add(login)
    
    login.addLoginListener {e: LoginEvent ->
      try { // try to authenticate with given credentials, should always return !null or throw an {@link AuthenticationException}
        val authentication =
          authenticationManager
            .authenticate(UsernamePasswordAuthenticationToken(
              e.username,
              e.password))
        // if authentication was successful we will update the security context and redirect to the page requested first
        if(authentication != null) {
          login.close()
          SecurityContextHolder.getContext()
            .authentication = authentication
          UI.getCurrent()
            .navigate(requestCache.resolveRedirectUrl())
        }
      } catch(ex: AuthenticationException) { // show default error message
        // Note: You should not expose any detailed information here like "username is known but password is wrong"
        // as it weakens security.
        login.isError = true
      }
    }
  }
  
  companion object {
    const val ROUTE = "login"
  }
}