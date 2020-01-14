package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.SEPARADO
import br.com.astrosoft.separacao.model.saci

class EditarViewModel(view: IEditarView): ViewModel<IEditarView>(view) {
  fun processar() = exec {
    view.produtos.forEach {produto ->
      saci.retornaSaldo()
    }
  }
  
  val pedidosSeparacao: List<Pedido>
    get() = Pedido.pedidosTemporarios.filter {
      it.tipoOrigem == SEPARADO
    }
}

interface IEditarView: IView {
  val pedido: Pedido?
  val produtos: List<ProdutoPedido>
  
  fun updateGrid()
}