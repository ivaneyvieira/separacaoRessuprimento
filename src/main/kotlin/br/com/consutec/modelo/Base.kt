package br.com.consutec.modelo

import java.math.BigDecimal

class Base {
  var lojaOrigem: Int? = null
  var pedidoOrigem: Int? = null
  var lojaDestino: Int? = null
  var pedidoDestino: Int? = null
  var descIni: String? = null
  var descFim: String? = null
  var fornecedores: String? = null
  var tipos: String? = null
  var lojaArea: Int? = null
  var areas: String? = null
  var centrodeLucro: String? = null
  var qtd1: BigDecimal? = null
  var qtd2: BigDecimal? = null
  var custo1: BigDecimal? = null
  var custo2: BigDecimal? = null
  var sinalQtd: String? = null
  var sinalCusto: String? = null
  var localizacao: String? = null
  var informaPedidoDestino: Boolean? = null
  var usaFiltrosProduto: Boolean? = null
}