package br.com.astrosoft.framework.viewmodel

import br.com.astrosoft.framework.view.log

open class ViewModel<V: IView>(val view: V) {
  fun exec(block: () -> Unit) {
    try {
      block()
    } catch(e: EViewModelError) {
      view.showError(e.message ?: "Erro generico")
      log?.error(e.toString())
      throw e
    } catch(e: EViewModelWarning) {
      view.showWarning(e.message ?: "Aviso generico")
      log?.error(e.toString())
      throw e
    }
  }
  
  open fun init() {
  }
}

interface IView {
  fun showError(msg: String)
  fun showWarning(msg: String)
  fun showInformation(msg: String)
}