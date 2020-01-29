package br.com.astrosoft.separacao.model.beans

import br.com.astrosoft.framework.util.mid
import br.com.astrosoft.separacao.model.enum.ETipoOrigem
import br.com.astrosoft.separacao.model.enum.ETipoOrigem.DUPLICADO
import br.com.astrosoft.separacao.model.saci

data class Pedido(val storeno: Int = 1, val ordno: Int, val ordnoMae: Int, val tipo: String) {
  fun compativel(pedido: Pedido) = storenoDestino == pedido.storenoDestino
  
  fun isNotEmpty(user: UserSaci?) = produtos(user).isNotEmpty()
  
  fun produtos(user: UserSaci?) = saci
    .listaProduto(ordno)
    .filtraLocalizacoes()
    .filter {user?.isLocalizacaoCompativel(it.localizacao) ?: false}
    .sortedWith(compareBy({it.localizacao}, {it.descricao}, {it.grade}))
  
  val storenoDestino
    get() = ordno.toString().mid(0, 1).toIntOrNull() ?: 0
  val tipoOrigem: ETipoOrigem
    get() = ETipoOrigem.value(tipo) ?: DUPLICADO
  
  fun abreviacoes(user: UserSaci?) = produtos(user)
    .filtraLocalizacoes()
    .groupBy {it.localizacao.mid(0, 4)}
    .entries
    .sortedBy {-it.value.size}
    .map {it.key}
  
  companion object {
    fun findPedidos(numeroOrigem: Int?): Pedido? {
      numeroOrigem ?: return null
      val user = UserSaci.userAtual
      return pedidos(user).firstOrNull {it.ordno == numeroOrigem}
    }
    
    fun pedidos(user: UserSaci?) = saci.listaPedido().filter {it.storenoDestino in 2..5}
      .filter {it.isNotEmpty(user)}
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
