package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.saci

class RemoverViewModel(view: IRemoverView): ViewModel<IRemoverView>(view) {
  fun remover() {
    val pedidoIncial = view.numeroPedidoInicial ?: throw EViewModelError("Pedido inicial não encontrado")
    val pedidoFinal = view.numeroPedidoFinal ?: throw EViewModelError("Pedido final não encontrado")
    if(!pedidoIncial.compativel(pedidoFinal)) {
      throw EViewModelError("O pedido de destino não é compatível com o pedido de Origem")
    }
    else
      saci.removePedido(pedidoIncial.ordno, pedidoFinal.ordno)
    view.showInformation("Pedidos excluidos com sucesso")
  }
}

interface IRemoverView: IView {
  var numeroPedidoInicial: Pedido?
  var numeroPedidoFinal: Pedido?
}