package br.com.astrosoft.separacao.model.beans

import br.com.astrosoft.framework.util.mid
import br.com.astrosoft.separacao.model.enum.ETipoOrigem
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.DUPLICADO
import br.com.astrosoft.separacao.model.saci

data class Pedido(val storeno: Int = 1, val ordno: Int, val ordnoMae: Int, val tipo: String) {
  fun compativel(pedido: Pedido) = storenoDestino == pedido.storenoDestino
  
  val isNotEmpty
    get() = produtos.isNotEmpty()
  val produtos
    get() = saci.listaProduto(ordno).filtraLocalizacoes()
      .sortedWith(compareBy({it.localizacao}, {it.descricao}, {it.grade}))
  val storenoDestino
    get() = ordno.toString().mid(0, 1).toIntOrNull() ?: 0
  val tipoOrigem: ETipoOrigem
    get() = ETipoOrigem.value(tipo) ?: DUPLICADO
  val abreviacoes
    get() = produtos
      .filtraLocalizacoes()
      .groupBy {it.localizacao.mid(0, 4)}
      .entries
      .sortedBy { -it.value.size}
      .map {it.key}
  
  companion object {
    fun findPedidos(numeroOrigem: Int?): Pedido? {
      numeroOrigem ?: return null
      return pedidos.firstOrNull {it.ordno == numeroOrigem}
    }
  
    val pedidos
      get() = saci.listaPedido().filter {it.storenoDestino in 2..5}
        .sortedWith(compareBy(Pedido::ordno, Pedido::ordno))
  }
  
  private fun List<ProdutoPedido>.filtraLocalizacoes(): List<ProdutoPedido> {
    return this.groupBy {ProdutoKey(it.prdno, it.grade)}
      .flatMap {entry ->
        val list = entry.value.filter {
          (!it.localizacao.startsWith("EXP4")) && (!it.localizacao.startsWith("CD00"))
        }
        if(list.isEmpty()) entry.value else list
      }
  }
}

data class ProdutoKey(val prdno: String, val grade: String)
