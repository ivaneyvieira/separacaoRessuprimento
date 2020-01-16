package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.IUsuarioView
import br.com.astrosoft.separacao.viewmodel.UsuarioViewModel
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.crudui.crud.CrudOperation.ADD
import org.vaadin.crudui.crud.impl.GridCrud

@Route(layout = SeparacaoLayout::class)
@PageTitle("Usuário")
class UsuarioView: ViewLayout<UsuarioViewModel>(), IUsuarioView {
  override val viewModel = UsuarioViewModel(this)
  
  init {
    form("Editor de usuários") {
      val crud: GridCrud<UserSaci> = GridCrud<UserSaci>(UserSaci::class.java)
      crud.grid
        .setColumns(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name)
      crud.crudFormFactory
        .setUseBeanValidation(true)
      crud.crudFormFactory
        .setVisibleProperties(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name,
                              UserSaci::duplicar.name, UserSaci::separar.name, UserSaci::editar.name,
                              UserSaci::editar.name)
      crud.getCrudFormFactory()
        .setVisibleProperties(
          ADD,
          UserSaci::no.name, UserSaci::login.name, UserSaci::name.name,
          UserSaci::duplicar.name, UserSaci::separar.name, UserSaci::editar.name,
          UserSaci::editar.name)
      // layout configuration
      // layout configuration
      setSizeFull()
      add(crud)
      // logic configuration
      // logic configuration
      crud.setOperations(
        {viewModel.findAll()},
        {user: UserSaci -> viewModel.add(user)},
        {user: UserSaci? -> viewModel.update(user)},
        {user: UserSaci? -> viewModel.delete(user)})
    }
  }
}