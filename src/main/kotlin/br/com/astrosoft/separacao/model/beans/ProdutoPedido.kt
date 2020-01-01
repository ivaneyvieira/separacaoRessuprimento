package br.com.astrosoft.separacao.model.beans

data class ProdutoPedido(
  val prdno: String,
  val grade: String,
  val descricao: String,
  val fornecedor: Int,
  val centrodelucro: String,
  val localizacao: String,
  val tipo: Int,
  val qtty: Double
                        )