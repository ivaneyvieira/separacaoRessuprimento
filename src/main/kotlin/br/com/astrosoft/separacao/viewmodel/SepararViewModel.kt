package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido

class SepararViewModel(view: ISepararView): ViewModel<ISepararView>(view) {
  fun separar() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  fun imprimir() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

interface ISepararView: IView {
  val pedido: Pedido?
  val produtosSelecionados: List<ProdutoPedido>
}