package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.saci

class SepararViewModel(view: ISepararView): ViewModel<ISepararView>(view) {
  fun separar() = exec {
    val pedido = view.pedido ?: throw EViewModelError("Pedido inválido")
    val storenoDestino = pedido.storenoDestino
    val ordno = pedido.ordno
    val proximoNumero = saci.proximoNumero(storenoDestino)
    val produtosSelecionados = view.produtosSelecionados
    if(produtosSelecionados.isEmpty())
      throw EViewModelError("Não há nenhum produto selecionado")
    else
      produtosSelecionados.forEach {produto ->
        saci.atualizarQuantidade(ordno, proximoNumero, produto.prdnoSaci, produto.grade, produto.qttyEdit.toDouble())
      }
    view.showInformation("Foi gerado o pedido número $proximoNumero")
    view.updateGrid()
  }
  
  fun imprimir() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  fun proximoNumero(): Int? {
    val storenoDestino = view.pedido?.storenoDestino ?: return null
    return saci.proximoNumero(storenoDestino)
  }
}

interface ISepararView: IView {
  val pedido: Pedido?
  val produtosSelecionados: List<ProdutoPedido>
  
  fun updateGrid()
}