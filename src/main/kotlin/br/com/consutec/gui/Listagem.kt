package br.com.consutec.gui

import br.com.consutec.dao.GestorDADOS
import br.com.consutec.modelo.Base
import br.com.consutec.modelo.Oprd
import br.com.consutec.modelo.Produto
import br.com.consutec.modelo.ProdutoTableModel
import br.com.consutec.modelo.RowNumberTable
import java.awt.Component
import java.awt.EventQueue
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import java.util.logging.*
import javax.swing.DefaultCellEditor
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.BASELINE
import javax.swing.GroupLayout.Alignment.LEADING
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.LayoutStyle.ComponentPlacement.UNRELATED
import javax.swing.RowSorter
import javax.swing.UIManager
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter

class Listagem(): JFrame() {
  var produtos: List<Produto>? = null
  var ptm: ProdutoTableModel? = null
  var filtros: Base? = null
  private var brUnMarkSel: JButton? = null
  fun setDados(prds: List<Produto?>?, base: Base?) {
    filtros = base
    val df = DecimalFormat("###,##0.0000")
    ptm = ProdutoTableModel()
    ptm!!.setDados(prds.orEmpty().filterNotNull().toMutableList())
    tbprodutos!!.model = ptm
    val alinhaCelulaDir = DefaultTableCellRenderer()
    alinhaCelulaDir.horizontalAlignment = 4
    val rowNumberTable = RowNumberTable(tbprodutos!!)
    tbprodutos!!.getColumn("Centro de Lucro")
      .cellRenderer = alinhaCelulaDir
    tbprodutos!!.columnModel.getColumn(0)
      .cellEditor = DefaultCellEditor(JCheckBox())
    tbprodutos!!.columnModel.getColumn(0)
      .preferredWidth = 60
    tbprodutos!!.columnModel.getColumn(1)
      .preferredWidth = 100
    tbprodutos!!.columnModel.getColumn(2)
      .preferredWidth = 100
    tbprodutos!!.columnModel.getColumn(3)
      .preferredWidth = 400
    tbprodutos!!.columnModel.getColumn(4)
      .preferredWidth = 100
    tbprodutos!!.columnModel.getColumn(5)
      .preferredWidth = 100
    tbprodutos!!.columnModel.getColumn(6)
      .preferredWidth = 100
    tbprodutos!!.columnModel.getColumn(7)
      .preferredWidth = 100
    tbprodutos!!.columnModel.getColumn(8)
      .preferredWidth = 150
    tbprodutos!!.columnModel.getColumn(0)
      .resizable = false
    jScrollPane1!!.setRowHeaderView(rowNumberTable as Component)
    jScrollPane1!!.setCorner("UPPER_LEFT_CORNER", rowNumberTable.tableHeader)
  }

  private var btMarkAll: JButton? = null
  private var btMarkSel: JButton? = null
  private var btnExecutar: JButton? = null
  private var jScrollPane1: JScrollPane? = null
  private var tbprodutos: JTable? = null
  fun sort() {
    val sorter = TableRowSorter(ptm)
    tbprodutos!!.rowSorter = sorter as RowSorter<ProdutoTableModel?>
  }

  private fun initComponents() {
    jScrollPane1 = JScrollPane()
    tbprodutos = JTable()
    btnExecutar = JButton()
    btMarkAll = JButton()
    btMarkSel = JButton()
    brUnMarkSel = JButton()
    defaultCloseOperation = 2
    title = "Listagem de Produtos do Pedido"
    tbprodutos!!.autoCreateRowSorter = true
    tbprodutos!!.model = DefaultTableModel(arrayOfNulls(0), arrayOfNulls<String>(0) as Array<Any?>?)
    tbprodutos!!.autoResizeMode = 0
    jScrollPane1!!.setViewportView(tbprodutos)
    btnExecutar!!.font = Font("Tahoma", 0, 18)
    btnExecutar!!.text = "Executar"
    btnExecutar!!.addActionListener(ActionListener {evt -> btnExecutarActionPerformed(evt)})
    btMarkAll!!.text = "Marcar Todos"
    btMarkAll!!.addActionListener(object: ActionListener {
      override fun actionPerformed(evt: ActionEvent) {
        btMarkAllActionPerformed(evt)
      }
    })
    btMarkSel!!.text = "Marcar Selecionados"
    btMarkSel!!.addActionListener(object: ActionListener {
      override fun actionPerformed(evt: ActionEvent) {
        btMarkSelActionPerformed(evt)
      }
    })
    brUnMarkSel!!.text = "Desmarcar Selecionados"
    brUnMarkSel!!.addActionListener(object: ActionListener {
      override fun actionPerformed(evt: ActionEvent) {
        brUnMarkSelActionPerformed(evt)
      }
    })
    val layout = GroupLayout(contentPane)
    contentPane.layout = layout
    layout.setHorizontalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(
      layout.createParallelGroup(LEADING).addComponent(jScrollPane1,
                                                       -1,
                                                       1272,
                                                       32767).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(
        LEADING).addComponent(btnExecutar).addGroup(layout.createSequentialGroup().addComponent(btMarkAll,
                                                                                                -2,
                                                                                                147,
                                                                                                -2).addPreferredGap(
        UNRELATED).addComponent(btMarkSel, -2, 148, -2).addPreferredGap(UNRELATED).addComponent(brUnMarkSel))).addGap(0,
                                                                                                                      0,
                                                                                                                      32767))).addContainerGap()))
    layout.setVerticalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
      jScrollPane1,
      -2,
      592,
      -2).addGap(18, 18, 18).addGroup(layout.createParallelGroup(BASELINE).addComponent(btMarkAll).addComponent(
      btMarkSel).addComponent(brUnMarkSel)).addGap(18, 18, 18).addComponent(btnExecutar, -2, 28, -2).addContainerGap(24,
                                                                                                                     32767)))
    pack()
  }

  private fun btnExecutarActionPerformed(evt: ActionEvent) {
    try {
      var pedido = Integer.valueOf(0)
      val lista: MutableList<Oprd> = ArrayList()
      for(i in 0 until tbprodutos!!.model.rowCount) {
        val marcado = tbprodutos!!.model.getValueAt(i, 0) as Boolean
        if(marcado) {
          val oprd = Oprd()
          oprd.prdno = tbprodutos!!.model.getValueAt(i, 1) as String?
          oprd.grade = tbprodutos!!.model.getValueAt(i, 2) as String?
          oprd.qtd = tbprodutos!!.model.getValueAt(i, 8) as BigDecimal?
          oprd.descricao = tbprodutos!!.model.getValueAt(i, 3) as String?
          lista.add(oprd)
        }
      }
      if(lista.size > 0) {
        val gestorDADOS = GestorDADOS()
        pedido = gestorDADOS.executarNovo(lista, (filtros)!!)
        JOptionPane.showMessageDialog(this, "Pedido separado com sucesso Novo Pedido: $pedido")
        dispose()
      }
      else {
        JOptionPane.showMessageDialog(this, "Nenhum produto selecionado")
      }
    } catch(e: Exception) {
      JOptionPane.showMessageDialog(this, "Não foi possível Separar o pedido! Erro:$e")
    }
  }

  private fun btMarkSelActionPerformed(evt: ActionEvent) {
    val linhas = tbprodutos!!.selectedRows
    if(linhas.size == 0) {
      JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada ")
    }
    else {
      for(linha: Int in linhas) {
        val idxModel = tbprodutos!!.rowSorter.convertRowIndexToModel(linha)
        tbprodutos!!.model.setValueAt(java.lang.Boolean.valueOf(true), idxModel, 0)
      }
    }
  }

  private fun btMarkAllActionPerformed(evt: ActionEvent) {
    if((btMarkAll!!.text == "Marcar Todos")) {
      for(i in 0 until tbprodutos!!.model.rowCount) {
        tbprodutos!!.model.setValueAt(java.lang.Boolean.valueOf(true), i, 0)
      }
      btMarkAll!!.text = "Desmarcar Todos"
    }
    else {
      for(i in 0 until tbprodutos!!.model.rowCount) {
        tbprodutos!!.model.setValueAt(java.lang.Boolean.valueOf(false), i, 0)
      }
      btMarkAll!!.text = "Marcar Todos"
    }
  }

  private fun brUnMarkSelActionPerformed(evt: ActionEvent) {
    val linhas = tbprodutos!!.selectedRows
    if(linhas.size == 0) {
      JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada ")
    }
    else {
      for(linha: Int in linhas) {
        val idxModel = tbprodutos!!.rowSorter.convertRowIndexToModel(linha)
        tbprodutos!!.model.setValueAt(java.lang.Boolean.valueOf(false), idxModel, 0)
      }
    }
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      try {
        for(info: LookAndFeelInfo in UIManager.getInstalledLookAndFeels()) {
          if(("Nimbus" == info.name)) {
            UIManager.setLookAndFeel(info.className)
            break
          }
        }
      } catch(ex: ClassNotFoundException) {
        Logger.getLogger(Listagem::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: InstantiationException) {
        Logger.getLogger(Listagem::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: IllegalAccessException) {
        Logger.getLogger(Listagem::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: UnsupportedLookAndFeelException) {
        Logger.getLogger(Listagem::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      }
      EventQueue.invokeLater(object: Runnable {
        override fun run() {
          (Listagem()).isVisible = true
        }
      })
    }
  }

  init {
    initComponents()
    extendedState = 6
  }
}