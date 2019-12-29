package br.com.consutec.gui

import java.awt.EventQueue
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.logging.*
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.LEADING
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.UIManager
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UnsupportedLookAndFeelException

class Main constructor(): JFrame() {
  private var btnDup: JButton? = null
  private var btnRem: JButton? = null
  private var btnSep: JButton? = null
  private var lbGestao: JLabel? = null
  private fun initComponents() {
    btnSep = JButton()
    btnRem = JButton()
    btnDup = JButton()
    lbGestao = JLabel()
    setDefaultCloseOperation(3)
    setTitle("Gesão de pedidos")
    btnSep!!.setFont(Font("Tahoma", 0, 14))
    btnSep!!.setText("Separar")
    btnSep!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        btnSepActionPerformed(evt)
      }
    })
    btnRem!!.setFont(Font("Tahoma", 0, 14))
    btnRem!!.setText("Remover")
    btnRem!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        btnRemActionPerformed(evt)
      }
    })
    btnDup!!.setFont(Font("Tahoma", 0, 14))
    btnDup!!.setText("Duplicar")
    btnDup!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        btnDupActionPerformed(evt)
      }
    })
    lbGestao!!.setFont(Font("Tahoma", 0, 18))
    lbGestao!!.setText("Gestão de Pedidos")
    val layout: GroupLayout = GroupLayout(getContentPane())
    getContentPane().setLayout(layout)
    layout.setHorizontalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addGap(24,
                                                                                                                 24,
                                                                                                                 24).addGroup(
      layout.createParallelGroup(LEADING).addComponent(btnDup, -2, 166, -2).addGroup(layout.createParallelGroup(LEADING,
                                                                                                                false).addComponent(
        btnSep,
        -1,
        -1,
        32767).addComponent(btnRem, -1, -1, 32767).addComponent(lbGestao, -1, 166, 32767))).addContainerGap(30, 32767)))
    layout.setVerticalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
      lbGestao).addGap(18, 18, 18).addComponent(btnDup, -2, 30, -2).addGap(18, 18, 18).addComponent(btnSep,
                                                                                                    -2,
                                                                                                    30,
                                                                                                    -2).addGap(18,
                                                                                                               18,
                                                                                                               18).addComponent(
      btnRem,
      -2,
      32,
      -2).addContainerGap(37, 32767)))
    pack()
  }

  private fun btnDupActionPerformed(evt: ActionEvent) {
    val dup: Duplicar = Duplicar()
    dup.setVisible(true)
  }

  private fun btnSepActionPerformed(evt: ActionEvent) {
    val sep: Separador = Separador()
    sep.setVisible(true)
  }

  private fun btnRemActionPerformed(evt: ActionEvent) {
    val rem: Remocao = Remocao()
    rem.setVisible(true)
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
        Logger.getLogger(Main::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: InstantiationException) {
        Logger.getLogger(Main::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: IllegalAccessException) {
        Logger.getLogger(Main::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: UnsupportedLookAndFeelException) {
        Logger.getLogger(Main::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      }
      EventQueue.invokeLater(object: Runnable {
        public override fun run() {
          (Main()).setVisible(true)
        }
      })
    }
  }

  init {
    initComponents()
  }
}