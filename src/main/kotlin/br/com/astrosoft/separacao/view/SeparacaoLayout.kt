package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.model.RegistryUserInfo
import br.com.astrosoft.separacao.model.beans.UserSaci
import com.github.appreciated.app.layout.behaviour.AppLayout
import com.github.appreciated.app.layout.behaviour.Behaviour
import com.github.appreciated.app.layout.builder.AppLayoutBuilder
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem
import com.github.appreciated.app.layout.component.menu.top.item.TopClickableItem
import com.github.appreciated.app.layout.entity.Section
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout
import com.github.appreciated.app.layout.router.AppLayoutRouterLayoutBase
import com.github.mvysny.karibudsl.v10.navigateToView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.icon.VaadinIcon.COPY
import com.vaadin.flow.component.icon.VaadinIcon.EDIT
import com.vaadin.flow.component.icon.VaadinIcon.ERASER
import com.vaadin.flow.component.icon.VaadinIcon.SPLIT
import com.vaadin.flow.component.icon.VaadinIcon.USER
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.router.PreserveOnRefresh
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import kotlin.reflect.KClass

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@PWA(name = "Separação para ressuprimento",
     shortName = "Separação",
     iconPath = "icons/logo.png")
@PreserveOnRefresh
class SeparacaoLayout: AppLayoutRouterLayout() {
  init {
    /*  val usuario = saci.findUser(SessionUitl.loginInfo?.usuario)
      if(usuario == null)
        navigateToView(LoginView::class)
      else*/
    init(atualizaMenu())
  }
  
  fun atualizaMenu(usuario: UserSaci? = null): AppLayout? {
    val appMenu = headerMenu(RegistryUserInfo.commpany, "Versão ${RegistryUserInfo.version}")
      .addMenu("Duplicar", COPY, DuplicarView::class, usuario?.duplicar)
      .addMenu("Separar", SPLIT, SepararView::class, usuario?.separar)
      .addMenu("Editar", EDIT, EditarView::class, usuario?.editar)
      .addMenu("Remover", ERASER, RemoverView::class, usuario?.remover)
      .addMenu("Usuários", USER, UsuarioView::class, usuario?.admin)
      .build()
    return appLayout(RegistryUserInfo.appName, appMenu)
  }
  
  private fun appLayout(title: String, appMenu: Component) = AppLayoutBuilder.get(Behaviour.LEFT_RESPONSIVE)
    .withTitle(title)
    .withAppBar(AppBarBuilder.get().add(TopClickableItem(null, VaadinIcon.CLOSE_CIRCLE.create()) {
      LoginService.logout()
      navigateToView(DefaultView::class)
    }).build())
    .withAppMenu(appMenu)
    .build()
  
  companion object {
    private lateinit var menuUsuario: LeftAppMenuBuilder
    private lateinit var menuRemover: LeftAppMenuBuilder
    private lateinit var menuEditar: LeftAppMenuBuilder
    private lateinit var menuSeparar: LeftAppMenuBuilder
    private lateinit var menuDuplicar: LeftAppMenuBuilder
    val currentLayout get() = AppLayoutRouterLayoutBase.getCurrent()
    
    fun updateLayout(user: UserSaci) {
      currentLayout.localeMenu("Duplicar")
    }
    
    fun Component.localeMenu(caption: String): Component? {
      println(this)
      this.children.forEach {
        it.localeMenu(caption)
      }
      return null
    }
    
    fun appMenu(usuario: UserSaci) {
      val appMenu = headerMenu(RegistryUserInfo.commpany, "Versão ${RegistryUserInfo.version}")
      menuDuplicar = appMenu.addMenu("Duplicar", COPY, DuplicarView::class, usuario.duplicar ?: false)
      menuSeparar = appMenu.addMenu("Separar", SPLIT, SepararView::class, usuario.separar ?: false)
      menuEditar = appMenu.addMenu("Editar", EDIT, EditarView::class, usuario.editar ?: false)
      menuRemover = appMenu.addMenu("Remover", ERASER, RemoverView::class, usuario.remover ?: false)
      menuUsuario = appMenu.addMenu("Usuários", USER, UsuarioView::class, usuario.admin ?: false)
    }
    
    private fun headerMenu(company: String, version: String) = LeftAppMenuBuilder.get()
      .addToSection(LeftHeaderItem(company, version, null), Section.HEADER).add(Hr())
    
    private fun LeftAppMenuBuilder.addMenu(caption: String, icon: VaadinIcon, className: KClass<out Component>,
                                           isVisible: Boolean? = true): LeftAppMenuBuilder {
      val menu = add(LeftNavigationItem(caption, icon, className.java))
      return menu
    }
  }
}