package br.com.astrosoft.separacao.model.beans

data class Relatorio(
  val ordno: Int,
  val storeno: Int,
  val prdno: String,
  val localizacao: String,
  val localizacaoSaci: String,
  val name: String,
  val grade: String,
  val fornecedorRef: String,
  val tipo: String,
  val qtty: Double,
  val fornecedor: Int,
  val estoque: Double,
  val embalagem: Double
                    )