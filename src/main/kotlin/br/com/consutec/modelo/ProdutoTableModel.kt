package br.com.consutec.modelo

import java.math.BigDecimal
import java.util.*
import javax.swing.JOptionPane
import javax.swing.table.AbstractTableModel

class ProdutoTableModel: AbstractTableModel() {
  private var dados: MutableList<Produto>
  private val colunas =
    arrayOf("Seleção",
            "Codigo Prod",
            "Grade",
            "Descrição",
            "Fornecedor",
            "Centro de Lucro",
            "Tipo",
            "Localização",
            "Quantidade")

  override fun getColumnClass(c: Int): Class<*> {
    if(c == 0) {
      return Boolean::class.java
    }
    if(c == 1) {
      return Long::class.java
    }
    if(c == 2) {
      return String::class.java
    }
    if(c == 3) {
      return String::class.java
    }
    if(c == 4) {
      return Long::class.java
    }
    if(c == 5) {
      return String::class.java
    }
    if(c == 6) {
      return Long::class.java
    }
    if(c == 7) {
      return String::class.java
    }
    return if(c == 8) {
      BigDecimal::class.java
    }
    else Any::class.java
  }

  override fun isCellEditable(row: Int, col: Int): Boolean {
    return if(col == 8 || col == 0) {
      true
    }
    else false
  }

  fun setDados(lista: MutableList<Produto>) {
    dados = lista
  }

  fun addRow(prd: Produto) {
    dados.add(prd)
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
      0 -> return dados[linha].selecao
      1 -> return dados[linha].prdno
      2 -> return dados[linha].grade
      3 -> return dados[linha].descricao
      4 -> return dados[linha].fornecedor
      5 -> return dados[linha].centrodelucro
      6 -> return dados[linha].tipo
      7 -> return dados[linha].localização
      8 -> return dados[linha].qtd
    }
    return null
  }

  override fun setValueAt(value: Any, linha: Int, coluna: Int) {
    if(coluna == 0) {
      dados[linha].selecao = value as Boolean
    }
    else {
      val atual: BigDecimal
      val novo: BigDecimal
      when(coluna) {
        1 -> {
          dados[linha].prdno = value as String
          dados[linha].grade = value
          dados[linha].descricao = value
          dados[linha].fornecedor = value as Long
          dados[linha].centrodelucro = value as String
          dados[linha].tipo = value as Long
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual < novo) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        2 -> {
          dados[linha].grade = value as String
          dados[linha].descricao = value
          dados[linha].fornecedor = value as Long
          dados[linha].centrodelucro = value as String
          dados[linha].tipo = value as Long
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual < novo) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        3 -> {
          dados[linha].descricao = value as String
          dados[linha].fornecedor = value as Long
          dados[linha].centrodelucro = value as String
          dados[linha].tipo = value as Long
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual.compareTo(novo) < 0) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        4 -> {
          dados[linha].fornecedor = value as Long
          dados[linha].centrodelucro = value as String
          dados[linha].tipo = value as Long
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual.compareTo(novo) < 0) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        5 -> {
          dados[linha].centrodelucro = value as String
          dados[linha].tipo = value as Long
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual.compareTo(novo) < 0) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        6 -> {
          dados[linha].tipo = value as Long
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual.compareTo(novo) < 0) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        7 -> {
          dados[linha].localização = value as String
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual.compareTo(novo) < 0) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
        8 -> {
          atual = getValueAt(linha, 8) as BigDecimal
          novo = value as BigDecimal
          if(atual < novo) {
            JOptionPane.showMessageDialog(null, "A quantidade informada é superior à quantidade do pedido")
            return
          }
          dados[linha].qtd = novo
        }
      }
    }
    fireTableCellUpdated(linha, coluna)
  }

  init {
    dados = ArrayList()
  }
}