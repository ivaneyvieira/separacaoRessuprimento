package br.com.astrosoft.separacao.model.beans

import br.com.astrosoft.framework.util.lpad

data class ProdutoPedido(
  val prdno: String,
  val grade: String,
  val descricao: String,
  val fornecedor: Int,
  val centrodelucro: String,
  val localizacao: String,
  val tipo: Int,
  val qtty: Double,
  val qttyOriginal: Double,
  val saldo: Double
                        ) {
  val quantidadeValida: Boolean
    get() = qttyEdit in qttyMin..qttyMax
  val codigo
    get() = prdno.lpad(6, "0")
  val prdnoSaci
    get() = prdno.lpad(16, " ")
  var qttyEdit: Int = 0
  val qttyMax
    get() = (qttyOriginal * 2.00).toInt()
  val qttyMin
    get() = 1
//  val diferenca
//    get() = qttyOriginal.toInt() - qttyEdit
}