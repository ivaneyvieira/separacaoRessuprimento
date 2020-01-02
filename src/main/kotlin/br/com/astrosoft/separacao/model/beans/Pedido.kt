package br.com.astrosoft.separacao.model.beans

import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.util.mid
import br.com.astrosoft.separacao.model.enum.ESituacaoPedido
import br.com.astrosoft.separacao.model.enum.ESituacaoPedido.BACKUP
import br.com.astrosoft.separacao.model.enum.ESituacaoPedido.ERRO
import br.com.astrosoft.separacao.model.enum.ESituacaoPedido.TEMPORARIO
import br.com.astrosoft.separacao.model.saci

data class Pedido(val storeno: Int = 1, val ordno: Int) {
  fun compativel(pedido: Pedido) = storenoDestino == pedido.storenoDestino
  
  val isNotEmpty
    get() = produtos.isNotEmpty()
  val isTemporario: Boolean
    get() = tipoPedido != TEMPORARIO
  val produtos
    get() = saci.listaProduto(ordno)
  val chave: String
    get() = ordno.toString().lpad(10, "0")
  val prefixo
    get() = chave.mid(0, 1).toIntOrNull() ?: -1
  val tipoPedido: ESituacaoPedido
    get() = if(chave.length == 10) {
      when {
        prefixo == 1 && numeroBackup != 0 -> BACKUP
        prefixo == 0 && numeroBackup == 0 -> TEMPORARIO
        else                              -> ERRO
      }
    }
    else ERRO
  val storenoDestino
    get() = chave.mid(6, 1).toIntOrNull() ?: 0
  val ordnoOrigem
    get() = chave.mid(6, 4).toIntOrNull() ?: 0
  val numeroBackup
    get() = chave.mid(1, 5).toIntOrNull() ?: 0
  
  companion object {
    fun findTemp(numeroOrigem: Int?): Pedido? {
      numeroOrigem ?: return null
      return pedidosTemporarios.firstOrNull {it.ordno == numeroOrigem}
    }
    
    val pedidos
      get() = saci.listaPedido().filter {
        it.tipoPedido == TEMPORARIO || it.tipoPedido == BACKUP
      }.filter {it.storenoDestino in 2..5}
        .sortedWith(compareBy(Pedido::ordnoOrigem, Pedido::ordno))
    val pedidosTemporarios
      get() = pedidos.filter {it.tipoPedido == TEMPORARIO}
    val pedidosBackup
      get() = pedidos.filter {it.tipoPedido == BACKUP}
  }
}

