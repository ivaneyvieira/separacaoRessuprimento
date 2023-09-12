package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.Pedido.Companion.removePedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import java.time.LocalDate

class DuplicarViewModel(view: IDuplicarView) : ViewModel<IDuplicarView>(view) {
  override fun init() {
    view.informarNumero = true
  }

  fun proximoNumero(): Int? {
    val pedidoOrigem = view.pedidoOrigem ?: return null
    val loja = pedidoOrigem.storeno
    val numero = pedidoOrigem.ordno
    val lojaDestino = pedidoOrigem.storenoDestino
    return run {
      Pedido.proximoNumeroDuplicado(loja, numero, lojaDestino)
    }
  }

  fun duplicar() = exec {
    val userSaci = UserSaci.userAtual
    val ordnoDestino = view.numeroDestino ?: 0
    if (ordnoDestino == 54)
      removePedido(numeroI = 54, numeroF = 54)
    val pedidoOrigem = Pedido.findPedidos(view.pedidoOrigem) ?: fail("Pedido de origem não encontrado")
    val lojaDestino = pedidoOrigem.storeno
    val pedidoDestino =
        Pedido.findPedidos(storeno = lojaDestino, ordno = ordnoDestino) ?: Pedido(
          storeno = lojaDestino,
          ordno = view.numeroDestino ?: 0,
          data = view.data,
          ordnoMae = 0,
          tipo = ""
        )
    when {
      pedidoDestino.isNotEmpty(userSaci)      -> fail("O pedido de destino já existe")
      !pedidoDestino.compativel(pedidoOrigem) -> fail("O pedido de destino não é compatível com o pedido de Origem")
      else                                    -> {
        Pedido.duplicar(pedidoOrigem, pedidoDestino)
        if (view.informarNumero == false) view.numeroDestino = proximoNumero()
      }
    }
    view.showInformation("Pedido duplicado com sucesso. Novo pedido: ${pedidoDestino.ordno}")
  }

  fun pedidos(): List<Pedido> {
    val userSaci = UserSaci.userAtual
    return if (userSaci?.admin == true) Pedido.pedidosTodos()
    else Pedido.pedidos()
  }
}

interface IDuplicarView : IView {
  var pedidoOrigem: Pedido?
  var numeroDestino: Int?
  val data: LocalDate?
  var informarNumero: Boolean?
}