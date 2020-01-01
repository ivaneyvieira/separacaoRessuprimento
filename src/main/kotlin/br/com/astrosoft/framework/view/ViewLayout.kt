package br.com.astrosoft.framework.view

import br.com.astrosoft.framework.model.RegistryUserInfo
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.view.LoginService
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.BeforeLeaveEvent
import com.vaadin.flow.router.BeforeLeaveObserver
import org.claspina.confirmdialog.ConfirmDialog

abstract class ViewLayout<V: IView, VM: ViewModel<V>>(): VerticalLayout(), IView, BeforeLeaveObserver,
                                                         BeforeEnterObserver, AfterNavigationObserver {
  abstract val viewModel: VM
  private val appName = RegistryUserInfo.appName
  private val version = "Verssão ${RegistryUserInfo.version}"
  val loginForm = LoginFormApp(appName, version)
  
  override fun showError(msg: String) {
    ConfirmDialog.createError()
      .withCaption("Erro do aplicativo")
      .withMessage(msg)
      .open()
  }
  
  override fun showWarning(msg: String) {
    ConfirmDialog.createWarning()
      .withCaption("Aviso")
      .withMessage(msg)
      .open()
  }
  
  override fun showInformation(msg: String) {
    ConfirmDialog.createInfo()
      .withCaption("Informação")
      .withMessage(msg)
      .open()
  }
  
  override fun beforeLeave(event: BeforeLeaveEvent?) {
  }
  
  override fun beforeEnter(event: BeforeEnterEvent?) {
    loginForm.isOpened = LoginService.isLogged() == false
  }
  
  override fun afterNavigation(event: AfterNavigationEvent?) {
    //   loginForm.isOpened = LoginService.isLogged() == false
  }
}

