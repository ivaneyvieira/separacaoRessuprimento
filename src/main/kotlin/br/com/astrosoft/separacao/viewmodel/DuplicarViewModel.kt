package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.enum.ESituacaoPedido.TEMPORARIO
import br.com.astrosoft.separacao.model.saci

class DuplicarViewModel(view: IDuplicarView): ViewModel<IDuplicarView>(view) {
  override fun init() {
    view.informarNumero = true
  }
  
  fun proximoNumero(): Int? {
    val pedidoOrigem = Pedido(ordno = view.numeroOrigem ?: 0)
    return if(pedidoOrigem.tipoPedido == TEMPORARIO) {
      val loja = pedidoOrigem.storenoDestino
      saci.proximoNumero(loja)
    }
    else null
  }
  
  fun duplicar() = exec {
    val pedidoOrigem = Pedido.findTemp(view.numeroOrigem) ?: throw EViewModelError("Pedido de origem não encontrado")
    val pedidoDestino = Pedido.findTemp(view.numeroDestino) ?: Pedido(ordno = view.numeroDestino ?: 0)
    when {
      pedidoDestino.isTemporario              -> throw EViewModelError("O pedido destino não é válido")
      pedidoDestino.isNotEmpty                -> throw EViewModelError("O pedido de destino já existe")
      !pedidoDestino.compativel(pedidoOrigem) -> throw EViewModelError("O pedido de destino " +
                                                                       "não é compatível com " +
                                                                       "o pedido de Origem")
      else                                    -> {
        saci.duplicar(pedidoOrigem.ordno, pedidoDestino.ordno)
        if(view.informarNumero == false)
          view.numeroDestino = proximoNumero()
      }
    }
    view.showInformation("Pedido duplicado com sucesso. Novo pedido: ${pedidoDestino.ordno}")
  }
}

interface IDuplicarView: IView {
  var numeroOrigem: Int?
  var numeroDestino: Int?
  var informarNumero: Boolean?
}