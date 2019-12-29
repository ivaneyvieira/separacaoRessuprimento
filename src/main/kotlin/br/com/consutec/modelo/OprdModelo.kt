package br.com.consutec.modelo

import java.math.BigDecimal
import java.util.*
import javax.swing.table.AbstractTableModel

class OprdModelo: AbstractTableModel() {
  private var dados: MutableList<Oprd>
  private val colunas = arrayOf("Codigo Prod", "Grade", "Descrição", "Quantidade")

  override fun getColumnClass(c: Int): Class<*> {
    if(c == 0) {
      return String::class.java
    }
    if(c == 1) {
      return String::class.java
    }
    if(c == 2) {
      return String::class.java
    }
    return if(c == 3) {
      BigDecimal::class.java
    }
    else String::class.java
  }

  override fun isCellEditable(row: Int, col: Int): Boolean {
    return false
  }

  fun setDados(lista: MutableList<Oprd>) {
    dados = lista
  }

  fun addRow(op: Oprd) {
    dados.add(op)
    fireTableDataChanged()
  }

  override fun getColumnName(num: Int): String {
    return colunas[num]
  }

  override fun getRowCount(): Int {
    return dados.size
  }

  override fun getColumnCount(): Int {
    return colunas.size
  }

  override fun getValueAt(linha: Int, coluna: Int): Any? {
    when(coluna) {
      0 -> return dados[linha].prdno
      1 -> return dados[linha].grade
      2 -> return dados[linha].descricao
      3 -> return dados[linha].qtd
    }
    return null
  }

  override fun setValueAt(value: Any, linha: Int, coluna: Int) {
    when(coluna) {
      0 -> {
        dados[linha].prdno = value as String
        dados[linha].grade = value
        dados[linha].descricao = value
        dados[linha].qtd = value as BigDecimal
      }
      1 -> {
        dados[linha].grade = value as String
        dados[linha].descricao = value
        dados[linha].qtd = value as BigDecimal
      }
      2 -> {
        dados[linha].descricao = value as String
        dados[linha].qtd = value as BigDecimal
      }
      3 -> dados[linha].qtd = value as BigDecimal
    }
    fireTableCellUpdated(linha, coluna)
  }

  init {
    dados = ArrayList()
  }
}