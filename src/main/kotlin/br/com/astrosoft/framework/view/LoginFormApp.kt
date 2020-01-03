package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.LoginInfo
import br.com.astrosoft.framework.model.RegistryUserInfo
import br.com.astrosoft.separacao.model.saci
import br.com.astrosoft.separacao.view.LoginService
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginI18n.Header
import com.vaadin.flow.component.login.LoginOverlay

class LoginFormApp(): LoginOverlay() {
  init {
    setI18n(loginI18n())
    
    addLoginListener {loginEvent ->
      val user = saci.findUser(loginEvent.username)
      when {
        user == null                      -> LoginService.logout()
        user.senha == loginEvent.password -> {
          LoginService.login(LoginInfo(user.name))
          close()
        }
        else                              -> LoginService.logout()
      }
    }
  }
  
  private fun loginI18n() = LoginI18n.createDefault().apply {
    this.header = Header()
    this.header.title = RegistryUserInfo.appName
    this.header.description = RegistryUserInfo.version
    this.form.username = "Usuário"
    this.form.title = "Acesse a sua conta"
    this.form.submit = "Entrar"
    this.form.password = "Senha"
    this.form.forgotPassword = ""
    this.errorMessage.title = "Usuário/senha inválidos"
    this.errorMessage.message = "Confira seu usuário e senha e tente novamente."
    this.additionalInformation = ""
  }
}