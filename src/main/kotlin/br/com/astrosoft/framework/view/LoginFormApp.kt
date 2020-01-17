package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.LoginInfo
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.model.saci
import br.com.astrosoft.separacao.view.DuplicarView
import br.com.astrosoft.separacao.view.EditarView
import br.com.astrosoft.separacao.view.LoginService
import br.com.astrosoft.separacao.view.RemoverView
import br.com.astrosoft.separacao.view.SepararView
import com.github.mvysny.karibudsl.v10.navigateToView
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginI18n.Header
import com.vaadin.flow.component.login.LoginOverlay

class LoginFormApp(appName: String, version: String, navigate : (UserSaci)-> Unit): LoginOverlay() {
  init {
    setI18n(loginI18n(appName, version))
    
    addLoginListener {loginEvent ->
      val user = saci.findUser(loginEvent.username)
      when {
        user == null                      -> LoginService.logout()
        user.senha == loginEvent.password -> {
          LoginService.login(LoginInfo(user.login ?: ""))
          navigate(user)
          close()
        }
        else                              -> LoginService.logout()
      }
    }
  }
  
  private fun loginI18n(appName: String, version: String) = LoginI18n.createDefault().apply {
    this.header = Header()
    this.header.title = appName
    this.header.description = version
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