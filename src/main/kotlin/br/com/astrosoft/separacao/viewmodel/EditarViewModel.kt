package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.util.mid
import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.Produto
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.LOJA
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.SEPARADO
import br.com.astrosoft.separacao.model.saci

class EditarViewModel(view: IEditarView): ViewModel<IEditarView>(view) {
  fun processar() = exec {
    val pedido = view.pedido ?: throw EViewModelError("Nenum pedido selecionado")
    val proximoNumero = saci.proximoNumero(pedido.storenoDestino)
    view.produtosSelecionados.forEach {produto ->
      if(produto.estoqueLoja == true)
        saci.atualizarQuantidade(ordno = pedido.ordno,
                                 ordnoNovo = proximoNumero,
                                 codigo = produto.prdnoSaci,
                                 grade = produto.grade,
                                 localizacao = produto.localizacao,
                                 qtty = produto.qttyEdit,
                                 tipo = LOJA)
      else
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
                        diferenca = produto.qtty.toInt(),
                        localizacao = produto.localizacao)
    }
    view.updateGrid()
  }
  
  fun novoProduto() = exec {
    view.pedido?.let {pedido ->
      view.novoProduto(pedido)
    }
  }
  
  fun findProduto(prdno: String?): List<Produto> {
    prdno ?: return emptyList()
    return saci.findProduto(prdno)
  }
  
  fun salvaProduto(produto: ProdutoDlg) = exec {
    produto.validadialog()
    val pedido = view.pedido ?: throw EViewModelError("Pedido não selecionado")
    val prdno = produto.codigo.lpad(16, " ")
    val grade = produto.grade
    val localizacao = produto.localizacao
    val qtty = produto.qtty ?: throw EViewModelError("Quantidade não informada")
    if(pedido.produtos.any {it.prdno == produto.codigo && it.grade == produto.grade})
      throw EViewModelError("O produto já está adicionado")
    saci.adicionarProduto(pedido, prdno, grade, qtty, localizacao)
    view.updateGrid()
  }
  
  val pedidosSeparacao: List<Pedido>
    get() = Pedido.pedidosTemporarios.filter {
      it.tipoOrigem == SEPARADO || it.tipoOrigem == LOJA
    }
}

interface IEditarView: IView {
  val pedido: Pedido?
  val produtos: List<ProdutoPedido>
  val produtosSelecionados: List<ProdutoPedido>
  
  fun updateGrid()
  
  val produtosNaoSelecionado
    get() = produtos - produtosSelecionados
  
  fun novoProduto(pedido: Pedido)
}

class ProdutoDlg(val pedido: Pedido) {
  var codigo: String = ""
  var grade: String = ""
  var localizacao: String = ""
  var qtty: Int? = 0
  var produtos: List<Produto> = emptyList()
  
  fun validadialog() {
    if(produtos.isEmpty())
      throw EViewModelError("Produto inválido")
    val quantidade = qtty ?: 0
    if(quantidade <= 0)
      throw EViewModelError("Quantidade inválida")
    val abreviacao = localizacao.mid(0, 4)
    if(!pedido.abreviacoes.contains(abreviacao))
      throw EViewModelError("A localização $abreviacao não é compativel")
  }
}