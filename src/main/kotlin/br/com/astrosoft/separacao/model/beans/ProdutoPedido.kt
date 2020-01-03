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
  val qtty: Double
                        ) {
  val codigo
    get() = prdno.lpad(6, "0")
}