package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido

class EditarViewModel(view: IEditarView): ViewModel<IEditarView>(view) {
}

interface IEditarView : IView {
  val pedido: Pedido?
 
  fun updateGrid()
}