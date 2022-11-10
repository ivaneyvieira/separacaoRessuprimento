package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.util.mid
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.Produto
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.LOJA
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.SEPARADO

class PendenciaViewModel(view: IPendenciaView): ViewModel<IPendenciaView>(view) {
  fun pedidos(): List<Pedido> {
    val user = UserSaci.userAtual
    return Pedido.pedidosPendente(user)
  }
  
  fun pedidosSeparacao(): List<Pedido> = pedidos()
}

interface IPendenciaView: IView {
  val pedido: Pedido?
  val produtos: List<ProdutoPedido>
  
  fun updateGrid()
}

