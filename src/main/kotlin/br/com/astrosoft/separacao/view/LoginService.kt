package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.model.LoginInfo
import br.com.astrosoft.framework.model.LoginInfoProvider
import br.com.astrosoft.framework.model.RegistryUserInfo
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinSession

object LoginService {
  fun login(loginInfo: LoginInfo) {
   // VaadinSession.getCurrent().session.invalidate()
   // UI.getCurrent().page.reload()
    SessionUitl.loginInfo = loginInfo
    RegistryUserInfo.loginInfoProvider = SessionLoginInfoProvider()
  }
  
  fun logout() {
    SessionUitl.loginInfo = null
    RegistryUserInfo.loginInfoProvider = null
    VaadinSession.getCurrent().session.invalidate()
    UI.getCurrent().page.reload()
  }
  
  fun isLogged(): Boolean {
    return SessionUitl.loginInfo != null
  }
}

class SessionLoginInfoProvider: LoginInfoProvider {
  override val loginInfo: LoginInfo?
    get() {
      return SessionUitl.loginInfo
    }
}

object SessionUitl {
  private const val ATTRIBUTE_NAME = "SESSION_USER"
  var loginInfo: LoginInfo?
    get() = VaadinSession.getCurrent().getAttribute(ATTRIBUTE_NAME) as? LoginInfo
    set(value) {
      VaadinSession.getCurrent()
        .setAttribute(ATTRIBUTE_NAME, value)
    }
}