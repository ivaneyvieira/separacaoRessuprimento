package br.com.consutec.gui

import br.com.consutec.dao.GestorDADOS
import java.awt.Color
import java.awt.EventQueue
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.text.DecimalFormat
import java.util.logging.*
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.BASELINE
import javax.swing.GroupLayout.Alignment.LEADING
import javax.swing.GroupLayout.Alignment.TRAILING
import javax.swing.JButton
import javax.swing.JFormattedTextField
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.LayoutStyle.ComponentPlacement.RELATED
import javax.swing.UIManager
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.text.DefaultFormatterFactory
import javax.swing.text.NumberFormatter

class Remocao constructor(): JFrame() {
  private var btnexecutarRemocao: JButton? = null
  private var jLabel16: JLabel? = null
  private var jLabel17: JLabel? = null
  private var jLabel18: JLabel? = null
  private var jPanel1: JPanel? = null
  private var tflojaRemocao: JFormattedTextField? = null
  private var tfpedidoRemocao: JFormattedTextField? = null
  private fun initComponents() {
    jPanel1 = JPanel()
    tflojaRemocao = JFormattedTextField()
    jLabel18 = JLabel()
    btnexecutarRemocao = JButton()
    tfpedidoRemocao = JFormattedTextField()
    jLabel16 = JLabel()
    jLabel17 = JLabel()
    setDefaultCloseOperation(2)
    setTitle("Remoção de Pedidos")
    jPanel1!!.setBackground(Color(204, 0, 51))
    tflojaRemocao!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tflojaRemocao!!.setFont(Font("Tahoma", 0, 18))
    jLabel18!!.setFont(Font("Tahoma", 0, 18))
    jLabel18!!.setForeground(Color(255, 255, 255))
    jLabel18!!.setText("Número do Pedido:")
    btnexecutarRemocao!!.setFont(Font("Tahoma", 0, 18))
    btnexecutarRemocao!!.setText("Executar")
    btnexecutarRemocao!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        btnexecutarRemocaoActionPerformed(evt)
      }
    })
    tfpedidoRemocao!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tfpedidoRemocao!!.setFont(Font("Tahoma", 0, 18))
    jLabel16!!.setFont(Font("Tahoma", 1, 24))
    jLabel16!!.setForeground(Color(255, 255, 255))
    jLabel16!!.setText("Remoção de Pedidos")
    jLabel17!!.setFont(Font("Tahoma", 0, 18))
    jLabel17!!.setForeground(Color(255, 255, 255))
    jLabel17!!.setText("Loja:")
    val jPanel1Layout: GroupLayout = GroupLayout(jPanel1)
    jPanel1!!.setLayout(jPanel1Layout)
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(LEADING).addGroup(TRAILING,
                                                                                         jPanel1Layout.createSequentialGroup().addContainerGap(
                                                                                           15,
                                                                                           32767).addGroup(jPanel1Layout.createParallelGroup(
                                                                                           LEADING).addComponent(
                                                                                           jLabel18,
                                                                                           -2,
                                                                                           173,
                                                                                           -2).addComponent(jLabel17,
                                                                                                            -2,
                                                                                                            41,
                                                                                                            -2)).addGap(
                                                                                           18,
                                                                                           18,
                                                                                           18).addGroup(jPanel1Layout.createParallelGroup(
                                                                                           LEADING).addComponent(
                                                                                           tflojaRemocao,
                                                                                           -2,
                                                                                           46,
                                                                                           -2).addComponent(
                                                                                           tfpedidoRemocao,
                                                                                           -2,
                                                                                           162,
                                                                                           -2)).addGap(77,
                                                                                                       77,
                                                                                                       77)).addGroup(
      jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(LEADING).addComponent(
        btnexecutarRemocao).addComponent(jLabel16, -2, 307, -2)).addContainerGap(-1, 32767)))
    jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(LEADING).addGroup(TRAILING,
                                                                                       jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(
                                                                                         jLabel16).addPreferredGap(
                                                                                         RELATED,
                                                                                         35,
                                                                                         32767).addGroup(jPanel1Layout.createParallelGroup(
                                                                                         BASELINE).addComponent(
                                                                                         tflojaRemocao,
                                                                                         -2,
                                                                                         -1,
                                                                                         -2).addComponent(jLabel17)).addGap(
                                                                                         18,
                                                                                         18,
                                                                                         18).addGroup(jPanel1Layout.createParallelGroup(
                                                                                         BASELINE).addComponent(
                                                                                         tfpedidoRemocao,
                                                                                         -2,
                                                                                         -1,
                                                                                         -2).addComponent(jLabel18)).addGap(
                                                                                         48,
                                                                                         48,
                                                                                         48).addComponent(
                                                                                         btnexecutarRemocao).addContainerGap()))
    val layout: GroupLayout = GroupLayout(getContentPane())
    getContentPane().setLayout(layout)
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

  private fun btnexecutarRemocaoActionPerformed(evt: ActionEvent) {
    try {
      val loja: Int = Integer.valueOf(tflojaRemocao!!.getText().toInt())
      val pedido: Int = Integer.valueOf(tfpedidoRemocao!!.getText().toInt())
      if((loja.toInt() == 1) || (loja.toInt() == 7) || (loja.toInt() == 10)) {
        if(loja.toInt() == 1) {
          if(pedido.toInt() >= 2000) {
            val gestorDADOS: GestorDADOS = GestorDADOS()
            gestorDADOS.removerPedido(loja, pedido)
            JOptionPane.showMessageDialog(this, "Pedido removido com sucesso! ")
            tflojaRemocao!!.setText("")
            tfpedidoRemocao!!.setText("")
            dispose()
          }
          else {
            JOptionPane.showMessageDialog(this, "Para loja 1 informe um pedido com número maior ou igual a 2000 ")
          }
        }
        else {
          val gestorDADOS: GestorDADOS = GestorDADOS()
          gestorDADOS.removerPedido(loja, pedido)
          JOptionPane.showMessageDialog(this, "Pedido removido com sucesso! ")
          tflojaRemocao!!.setText("")
          tfpedidoRemocao!!.setText("")
          dispose()
        }
      }
      else {
        JOptionPane.showMessageDialog(this, "Informe uma loja válida!")
      }
    } catch(e: Exception) {
      e.printStackTrace()
      JOptionPane.showMessageDialog(this, "Não foi possível remover o pedido informado! Erro: " + e)
    }
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      try {
        for(info: LookAndFeelInfo in UIManager.getInstalledLookAndFeels()) {
          if(("Nimbus" == info.getName())) {
            UIManager.setLookAndFeel(info.getClassName())
            break
          }
        }
      } catch(ex: ClassNotFoundException) {
        Logger.getLogger(Remocao::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: InstantiationException) {
        Logger.getLogger(Remocao::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: IllegalAccessException) {
        Logger.getLogger(Remocao::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: UnsupportedLookAndFeelException) {
        Logger.getLogger(Remocao::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      }
      EventQueue.invokeLater(object: Runnable {
        public override fun run() {
          (Remocao()).setVisible(true)
        }
      })
    }
  }

  init {
    initComponents()
  }
}