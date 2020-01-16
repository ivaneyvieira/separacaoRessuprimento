package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.IUsuarioView
import br.com.astrosoft.separacao.viewmodel.UsuarioViewModel
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.vaadin.crudui.crud.CrudOperation.ADD
import org.vaadin.crudui.crud.CrudOperation.DELETE
import org.vaadin.crudui.crud.CrudOperation.READ
import org.vaadin.crudui.crud.CrudOperation.UPDATE
import org.vaadin.crudui.crud.impl.GridCrud

@Route(layout = SeparacaoLayout::class)
@PageTitle("Usuário")
class UsuarioView: ViewLayout<UsuarioViewModel>(), IUsuarioView {
  override val viewModel = UsuarioViewModel(this)
  
  init {
    form("Editor de usuários")
    setSizeFull()
    val crud: GridCrud<UserSaci> = gridCrud()
    // layout configuration
    setSizeFull()
    this.add(crud)
    // logic configuration
    setOperationd(crud)
  }
  
  private fun gridCrud(): GridCrud<UserSaci> {
    val captionFields = listOf("Numero", "Login", "Nome", "Duplicar", "Separar", "Editar", "Remover")
    val crud: GridCrud<UserSaci> = GridCrud<UserSaci>(UserSaci::class.java)
    crud.grid
      .setColumns(UserSaci::no.name, UserSaci::login.name, UserSaci::name.name)
    crud.grid.addColumnBool("Duplicar") {duplicar}
    crud.grid.addColumnBool("Separar") {separar}
    crud.grid.addColumnBool("Editar") {editar}
    crud.grid.addColumnBool("Remover") {remover}
    
    crud.crudFormFactory
      .setUseBeanValidation(true)
    val allFields = listOf(UserSaci::no.name,
                           UserSaci::login.name,
                           UserSaci::name.name,
                           UserSaci::duplicar.name,
                           UserSaci::separar.name,
                           UserSaci::editar.name,
                           UserSaci::remover.name)
    crud.crudFormFactory.setVisibleProperties(*allFields.toTypedArray())
    crud.crudFormFactory.setDisabledProperties(UPDATE, UserSaci::no.name, UserSaci::login.name, UserSaci::name.name)
    crud.crudFormFactory.setFieldCaptions(UPDATE, * captionFields.toTypedArray())
    crud.crudFormFactory.setDisabledProperties(READ, *allFields.toTypedArray())
    crud.crudFormFactory.setFieldCaptions(READ, * captionFields.toTypedArray())
    crud.crudFormFactory.setDisabledProperties(DELETE, *allFields.toTypedArray())
    crud.crudFormFactory.setFieldCaptions(DELETE, * captionFields.toTypedArray())
    crud.crudFormFactory.setDisabledProperties(ADD, UserSaci::no.name,
                                               UserSaci::name.name)
    crud.crudFormFactory.setVisibleProperties(ADD,
                                              UserSaci::login.name,
                                              UserSaci::duplicar.name,
                                              UserSaci::separar.name,
                                              UserSaci::editar.name,
                                              UserSaci::editar.name)
    crud.crudFormFactory.setFieldCaptions(ADD, "Login", "Duplicar", "Separar", "Editar", "Remover")
    crud.setSizeFull()
    return crud
  }
  
  private fun UsuarioView.setOperationd(crud: GridCrud<UserSaci>) {
    crud.setOperations(
      {viewModel.findAll()},
      {user: UserSaci -> viewModel.add(user)},
      {user: UserSaci? -> viewModel.update(user)},
      {user: UserSaci? -> viewModel.delete(user)})
  }
  
  private fun Grid<UserSaci>.addColumnBool(caption: String, value: UserSaci.() -> Boolean) {
    val column = this.addComponentColumn {bean ->
      val icon = if(bean.value()) VaadinIcon.CHECK_CIRCLE_O.create()
      else VaadinIcon.CIRCLE_THIN.create()
      icon
    }
    column.setHeader(caption)
    column.textAlign = ColumnTextAlign.CENTER
  }
}

