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
    user.ativo = true
    saci.updateUser(user)
    return user
  }
  
  fun update(user: UserSaci?): UserSaci? {
    user ?: throw EViewModelError("Usuário não selecionado")
    saci.updateUser(user)
    return user
  }
  
  fun delete(user: UserSaci?) {
    user ?: throw EViewModelError("Usuário não selecionado")
    user.ativo = false
    saci.updateUser(user)
  }
}

interface IUsuarioView: IView {
}