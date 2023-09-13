package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.util.ECupsPrinter
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.framework.viewmodel.fail
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.Relatorio
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.SEPARADO

class SepararViewModel(view: ISepararView): ViewModel<ISepararView>(view) {
  fun separar() = exec {
    val pedido = view.pedido ?: fail("Pedido inválido")
    val storenoDestino = pedido.storenoDestino
    val ordno = pedido.ordno
    val proximoNumero = Pedido.proximoNumeroSeparado(storenoDestino)
    val produtosSelecionados = view.produtosSelecionados
    if(produtosSelecionados.isEmpty())
      fail("Não há nenhum produto selecionado")
    else
      produtosSelecionados.forEach {produto ->
        Pedido.atualizarQuantidade(ordno, proximoNumero, produto, SEPARADO)
      }
    print(proximoNumero)
    view.showInformation("Foi gerado o pedido número $proximoNumero")
    view.updateGrid()
  }
  
  fun proximoNumero(): Int {
    val pedido = view.pedido
    val storenoDestino = pedido?.storenoDestino ?: 0
    return Pedido.proximoNumeroSeparado(storenoDestino)
  }
  
  fun imprimir() = exec {
    val pedido = view.pedido ?: fail("Pedido inválido")
    view.showQuestion("Confirma a impressão?") {
      print(pedido.ordno)
    }
  }

  fun imprimirSelecionado() = exec {
    val pedido = view.pedido ?: fail("Pedido inválido")
    val produtosSelecionados = view.produtosSelecionados
    view.showQuestion("Confirma a impressão?") {
      printSelecionado(pedido, produtosSelecionados)
    }
  }

  private fun printSelecionado(pedido: Pedido, produtos: List<ProdutoPedido>) {
    try {
      val relatorio = produtos.map{prd ->
        Relatorio(
          ordno = pedido.ordno,
          storeno = pedido.storeno,
          prdno = prd.prdno,
          localizacao = prd.localizacao,
          name = prd.descricao,
          grade = prd.grade,
          fornecedorRef = prd.fornecedorRef,
          tipo = prd.tipo.toString(),
          qtty = prd.qtty,
          fornecedor = prd.fornecedor,
          estoque = prd.estoque,
          embalagem = prd.embalagem
                  )
      }
      RelatorioText().print("RESSUPRIMENTO", relatorio)
    } catch(e: ECupsPrinter) {
      view.showError(e.message ?: "")
    }
  }
  
  fun pedidos(): List<Pedido> {
    val user = UserSaci.userAtual
    val pedidos =Pedido.pedidos(user)
    return pedidos.filter {it.storeno == 1}
  }
}

interface ISepararView: IView {
  val pedido: Pedido?
  val produtosSelecionados: List<ProdutoPedido>
  
  fun updateGrid()
}