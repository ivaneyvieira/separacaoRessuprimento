package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.model.RegistryUserInfo
import com.github.appreciated.app.layout.behaviour.Behaviour.LEFT_RESPONSIVE
import com.github.appreciated.app.layout.builder.AppLayoutBuilder
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem
import com.github.appreciated.app.layout.component.menu.top.item.TopClickableItem
import com.github.appreciated.app.layout.entity.Section
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout
import com.github.mvysny.karibudsl.v10.navigateToView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.COPY
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.icon.VaadinIcon.ERASER
import com.vaadin.flow.component.icon.VaadinIcon.SPLIT
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import kotlin.reflect.KClass

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "Separação para ressuprimento",
     shortName = "Separação",
     iconPath = "icons/logo.png")
class SeparacaoLayout: AppLayoutRouterLayout() {
  init {
    val appMenu = headerMenu(RegistryUserInfo.commpany, "Versão ${RegistryUserInfo.version}")
      .addMenu("Duplicar", COPY, DuplicarView::class)
      .addMenu("Separar", SPLIT, SepararView::class)
      .addMenu("Editar", EDIT, EditarView::class)
      .addMenu("Remover", ERASER, RemoverView::class)
      .build()
    val appLayout = appLayout(RegistryUserInfo.appName, appMenu)
    
    init(appLayout)
  }
  
  private fun appLayout(title: String, appMenu: Component) = AppLayoutBuilder.get(LEFT_RESPONSIVE)
    .withTitle(title)
    .withAppBar(AppBarBuilder.get().add(TopClickableItem(null, VaadinIcon.CLOSE_CIRCLE.create()) {
      LoginService.logout()
      navigateToView(DefaultView::class)
    }).build())
    .withAppMenu(appMenu)
    .build()
  
  private fun headerMenu(company: String, version: String) = LeftAppMenuBuilder.get()
    .addToSection(LeftHeaderItem(company, version, null), Section.HEADER)
  
  private fun LeftAppMenuBuilder.addMenu(caption: String, icon: VaadinIcon, className: KClass<out Component>) =
    add(LeftNavigationItem(caption, icon, className.java))
}


