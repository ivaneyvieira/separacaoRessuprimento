package br.com.astrosoft.framework.security

import br.com.astrosoft.framework.security.SecurityUtils.isUserLoggedIn
import br.com.astrosoft.framework.view.LoginView
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.UIInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import org.springframework.stereotype.Component

@Component
class ConfigureUIServiceInitListener: VaadinServiceInitListener {
  override fun serviceInit(event: ServiceInitEvent) {
    event.source
      .addUIInitListener {uiEvent: UIInitEvent ->
        val ui = uiEvent.ui
        ui.addBeforeEnterListener {event: BeforeEnterEvent ->
          beforeEnter(event)
        }
      }
  }
  
  /**
   * Reroutes the user if (s)he is not authorized to access the view.
   *
   * @param event
   * before navigation event with event details
   */
  private fun beforeEnter(event: BeforeEnterEvent) {
    if(LoginView::class.java != event.navigationTarget
       && !isUserLoggedIn
    ) {
      event.rerouteTo(LoginView::class.java)
    }
  }
}