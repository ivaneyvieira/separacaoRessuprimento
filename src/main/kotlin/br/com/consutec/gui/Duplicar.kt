package br.com.consutec.gui

import br.com.consutec.dao.GestorDADOS
import java.awt.Color
import java.awt.Cursor
import java.awt.EventQueue
import java.awt.Font
import java.awt.event.ActionEvent
import java.text.DecimalFormat
import java.util.logging.*
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.BASELINE
import javax.swing.GroupLayout.Alignment.LEADING
import javax.swing.GroupLayout.Alignment.TRAILING
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFormattedTextField
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.LayoutStyle.ComponentPlacement.RELATED
import javax.swing.LayoutStyle.ComponentPlacement.UNRELATED
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.text.DefaultFormatterFactory
import javax.swing.text.NumberFormatter

class Duplicar: JFrame() {
  private var btnDuplicar: JButton? = null
  private var cbdestino: JCheckBox? = null
  private var jLabel1: JLabel? = null
  private var jLabel13: JLabel? = null
  private var jLabel2: JLabel? = null
  private var jLabel3: JLabel? = null
  private var jLabel4: JLabel? = null
  private var jPanel1: JPanel? = null
  private var tfdestino: JFormattedTextField? = null
  private var tflojaDestino: JFormattedTextField? = null
  private var tflojaOrigem: JFormattedTextField? = null
  private var tfpedidoOrigem: JFormattedTextField? = null
  private fun initComponents() {
    jPanel1 = JPanel()
    tflojaOrigem = JFormattedTextField()
    jLabel2 = JLabel()
    tfpedidoOrigem = JFormattedTextField()
    jLabel3 = JLabel()
    tflojaDestino = JFormattedTextField()
    jLabel13 = JLabel()
    tfdestino = JFormattedTextField()
    cbdestino = JCheckBox()
    jLabel1 = JLabel()
    btnDuplicar = JButton()
    jLabel4 = JLabel()
    defaultCloseOperation = 2
    title = "Duplicar Pedidos"
    jPanel1!!.background = Color(255, 255, 204)
    tflojaOrigem!!.formatterFactory = DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0")))
    tflojaOrigem!!.font = Font("Tahoma", 0, 12)
    jLabel2!!.font = Font("Tahoma", 0, 12)
    jLabel2!!.text = "Número Pedido de Origem:"
    tfpedidoOrigem!!.formatterFactory = DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0")))
    tfpedidoOrigem!!.font = Font("Tahoma", 0, 12)
    jLabel3!!.font = Font("Tahoma", 0, 12)
    jLabel3!!.text = "Loja de Destino:"
    tflojaDestino!!.formatterFactory = DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0")))
    tflojaDestino!!.font = Font("Tahoma", 0, 12)
    jLabel13!!.font = Font("Tahoma", 0, 12)
    jLabel13!!.text = "Número do Pedido de Destino:"
    tfdestino!!.formatterFactory = DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0")))
    tfdestino!!.font = Font("Tahoma", 0, 12)
    cbdestino!!.background = Color(255, 255, 204)
    cbdestino!!.font = Font("Tahoma", 0, 12)
    cbdestino!!.isSelected = true
    cbdestino!!.text = "Informar número do pedido de destino"
    cbdestino!!.cursor = Cursor(0)
    cbdestino!!.addActionListener {evt -> cbdestinoActionPerformed(evt)}
    jLabel1!!.font = Font("Tahoma", 0, 12)
    jLabel1!!.text = "Loja de Origem:"
    btnDuplicar!!.font = Font("Tahoma", 0, 18)
    btnDuplicar!!.text = "Duplicar"
    btnDuplicar!!.addActionListener {evt -> btnDuplicarActionPerformed(evt)}
    jLabel4!!.font = Font("Tahoma", 1, 24)
    jLabel4!!.text = "Duplicar Pedidos"
    val jPanel1Layout = GroupLayout(jPanel1)
    jPanel1!!.layout = jPanel1Layout
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(
      jPanel1Layout.createParallelGroup(LEADING).addComponent(btnDuplicar,
                                                              -2,
                                                              132,
                                                              -2).addGroup(jPanel1Layout.createSequentialGroup().addGroup(
        jPanel1Layout.createParallelGroup(LEADING).addGroup(jPanel1Layout.createParallelGroup(TRAILING).addComponent(
          jLabel13).addComponent(jLabel3, -2, 169, -2)).addComponent(jLabel2).addComponent(jLabel1,
                                                                                           -2,
                                                                                           149,
                                                                                           -2)).addGap(18,
                                                                                                       18,
                                                                                                       18).addGroup(
        jPanel1Layout.createParallelGroup(LEADING).addComponent(cbdestino).addComponent(tfdestino,
                                                                                        -2,
                                                                                        148,
                                                                                        -2).addComponent(tflojaDestino,
                                                                                                         -2,
                                                                                                         40,
                                                                                                         -2).addComponent(
          tfpedidoOrigem,
          -2,
          151,
          -2).addComponent(tflojaOrigem, -2, 40, -2))).addComponent(jLabel4, -2, 205, -2)).addContainerGap(45, 32767)))
    jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(LEADING).addGroup(TRAILING,
                                                                                       jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(
                                                                                         jLabel4).addGap(31,
                                                                                                         31,
                                                                                                         31).addGroup(
                                                                                         jPanel1Layout.createParallelGroup(
                                                                                           BASELINE).addComponent(
                                                                                           tflojaOrigem,
                                                                                           -2,
                                                                                           -1,
                                                                                           -2).addComponent(jLabel1)).addPreferredGap(
                                                                                         UNRELATED).addGroup(
                                                                                         jPanel1Layout.createParallelGroup(
                                                                                           BASELINE).addComponent(
                                                                                           tfpedidoOrigem,
                                                                                           -2,
                                                                                           -1,
                                                                                           -2).addComponent(jLabel2)).addPreferredGap(
                                                                                         UNRELATED).addGroup(
                                                                                         jPanel1Layout.createParallelGroup(
                                                                                           BASELINE).addComponent(
                                                                                           tflojaDestino,
                                                                                           -2,
                                                                                           -1,
                                                                                           -2).addComponent(jLabel3)).addPreferredGap(
                                                                                         RELATED).addComponent(cbdestino,
                                                                                                               -2,
                                                                                                               28,
                                                                                                               -2).addPreferredGap(
                                                                                         UNRELATED).addGroup(
                                                                                         jPanel1Layout.createParallelGroup(
                                                                                           BASELINE).addComponent(
                                                                                           jLabel13).addComponent(
                                                                                           tfdestino,
                                                                                           -2,
                                                                                           -1,
                                                                                           -2)).addGap(30,
                                                                                                       30,
                                                                                                       30).addComponent(
                                                                                         btnDuplicar).addContainerGap(23,
                                                                                                                      32767)))
    val layout = GroupLayout(contentPane)
    contentPane.layout = layout
    layout.setHorizontalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
      jPanel1,
      -2,
      -1,
      -2).addContainerGap(-1, 32767)))
    layout.setVerticalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
      jPanel1,
      -2,
      -1,
      -2).addContainerGap(-1, 32767)))
    pack()
  }

  private fun cbdestinoActionPerformed(evt: ActionEvent) {
    if(cbdestino!!.isSelected) {
      tfdestino!!.isEnabled = true
    }
    else {
      tfdestino!!.isEnabled = false
    }
  }

  private fun btnDuplicarActionPerformed(evt: ActionEvent) {
    if(tflojaOrigem!!.text != "" && tfpedidoOrigem!!.text != "" && tflojaDestino!!.text != "") {
      val lojaOrigem = Integer.valueOf(tflojaOrigem!!.text.toInt())
      val lojaDestino = Integer.valueOf(tflojaDestino!!.text.toInt())
      val numPedidoOrigem = Integer.valueOf(tfpedidoOrigem!!.text.toInt())
      val dao = GestorDADOS()
      try {
        if(dao.verificaPedido(lojaOrigem, numPedidoOrigem)) {
          val numPedidoDestino: Int
          numPedidoDestino = if(tfdestino!!.text == "") {
            Integer.valueOf(0)
          }
          else {
            Integer.valueOf(tfdestino!!.text.toInt())
          }
          try {
            val resultado = dao.duplicar(lojaOrigem, lojaDestino, numPedidoOrigem, numPedidoDestino)
            JOptionPane.showMessageDialog(this, "Pedido duplicado com sucesso Novo Pedido: $resultado")
          } catch(ex: Exception) {
            JOptionPane.showMessageDialog(this, "Erro ao duplicar " + ex.message)
          }
        }
        else {
          JOptionPane.showMessageDialog(this, "Pedido informado não existe ")
        }
      } catch(e: Exception) {
        JOptionPane.showMessageDialog(this, "Não foi possível verificar o pedido " + e.message)
      }
    }
    else {
      JOptionPane.showMessageDialog(this,
                                    "Os Campos Loja de Origem, Pedido de Origem e Loja de Destino são obrigatórios")
    }
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      try {
        for(info in UIManager.getInstalledLookAndFeels()) {
          if("Nimbus" == info.name) {
            UIManager.setLookAndFeel(info.className)
            break
          }
        }
      } catch(ex: ClassNotFoundException) {
        Logger.getLogger(Duplicar::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: InstantiationException) {
        Logger.getLogger(Duplicar::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: IllegalAccessException) {
        Logger.getLogger(Duplicar::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: UnsupportedLookAndFeelException) {
        Logger.getLogger(Duplicar::class.java.name)
          .log(Level.SEVERE, null as String?, ex)
      }
      EventQueue.invokeLater {Duplicar().isVisible = true}
    }
  }

  init {
    initComponents()
  }
}