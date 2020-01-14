package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.SEPARADO
import br.com.astrosoft.separacao.model.saci

class EditarViewModel(view: IEditarView): ViewModel<IEditarView>(view) {
  fun processar() = exec {
    val pedido = view.pedido ?: throw EViewModelError("Nenum pedido selecionado")
    view.produtosSelecionados.forEach {produto ->
      saci.retornaSaldo(ordnoMae = pedido.ordnoMae,
                        ordno = pedido.ordno,
                        codigo = produto.codigo,
                        grade = produto.grade,
                        diferenca = produto.diferenca,
                        localizacao = produto.localizacao)
    }
    view.produtosNaoSelecionado.forEach {produto ->
      saci.retornaSaldo(ordnoMae = pedido.ordnoMae,
                        ordno = pedido.ordno,
                        codigo = produto.codigo,
                        grade = produto.grade,
                        diferenca = produto.saldo.toInt(),
                        localizacao = produto.localizacao)
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
  val produtosSelecionados: List<ProdutoPedido>
  
  fun updateGrid()
  
  val produtosNaoSelecionado
    get() = produtos - produtosSelecionados
}