package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.util.ECupsPrinter
import br.com.astrosoft.framework.util.Ssh
import br.com.astrosoft.framework.util.execCommand
import br.com.astrosoft.framework.viewmodel.EViewModelError
import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.SEPARADO
import br.com.astrosoft.separacao.model.saci

class SepararViewModel(view: ISepararView): ViewModel<ISepararView>(view) {
  fun separar() = exec {
    val pedido = view.pedido ?: throw EViewModelError("Pedido inválido")
    val storenoDestino = pedido.storenoDestino
    val ordno = pedido.ordno
    val proximoNumero = proximoNumero() ?: throw EViewModelError("Próximo número não encontrado")
    val produtosSelecionados = view.produtosSelecionados
    if(produtosSelecionados.isEmpty())
      throw EViewModelError("Não há nenhum produto selecionado")
    else
      produtosSelecionados.forEach {produto ->
        saci.atualizarQuantidade(ordno, proximoNumero, produto.prdno, produto.grade,
                                 produto.localizacao, produto.qttyEdit, SEPARADO)
      }
    print(proximoNumero)
    view.showInformation("Foi gerado o pedido número $proximoNumero")
    view.updateGrid()
  }
  
  fun imprimir() = exec {
    val pedido = view.pedido ?: throw EViewModelError("Pedido inválido")
    print(pedido.ordno)
  }
  
  private fun print(ordno: Int) {
    //if(!QuerySaci.test)
    try {
      RelatorioText().print("RESSUPRIMENTO", saci.listaRelatorio(ordno))
    } catch(e: ECupsPrinter) {
      view.showError(e.message ?: "Erro de impressão")
      Ssh("172.20.47.1", "ivaney", "ivaney").shell {
        execCommand("/u/saci/shells/printRessuprimento.sh $ordno")
      }
    }
  }
  
  fun proximoNumero(): Int? {
    val storenoDestino = view.pedido?.storenoDestino ?: return null
    return saci.proximoNumeroSeparado(storenoDestino)
  }
}

interface ISepararView: IView {
  val pedido: Pedido?
  val produtosSelecionados: List<ProdutoPedido>
  
  fun updateGrid()
}