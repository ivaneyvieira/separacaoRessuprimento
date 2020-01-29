package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.model.saci

class UsuarioViewModel(view: IUsuarioView): ViewModel<IUsuarioView>(view) {
  fun findAll(): List<UserSaci>? {
    return saci.findAllUser()
      .filter {it.ativo}
  }
  
  fun add(user: UserSaci): UserSaci? {
    exec {
      user.ativo = true
      validaUser(user)
      saci.updateUser(user)
    }
    return user
  }
  
  fun validaUser(user: UserSaci?): UserSaci {
    saci.findUser(user?.login) ?: throw EViewModelError("Usuário não encontrado no saci")
    return user ?: throw EViewModelError("Usuário não selecionado")
  }
  
  fun update(user: UserSaci?): UserSaci? {
    exec {
      saci.updateUser(validaUser(user))
    }
    return user
  }
  
  fun delete(user: UserSaci?) {
    exec {
      val userValid = validaUser(user)
      userValid.ativo = false
      saci.updateUser(userValid)
    }
  }
  
  fun abreviacoes(): List<String> {
    return saci.findAbreviacoes()
  }
}

interface IUsuarioView: IView {
}