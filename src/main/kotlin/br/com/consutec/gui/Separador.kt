package br.com.consutec.gui

import br.com.consutec.dao.GestorDADOS
import br.com.consutec.modelo.Base
import br.com.consutec.modelo.Produto
import java.awt.Color
import java.awt.Cursor
import java.awt.EventQueue
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.logging.*
import javax.swing.DefaultComboBoxModel
import javax.swing.GroupLayout
import javax.swing.GroupLayout.Alignment.BASELINE
import javax.swing.GroupLayout.Alignment.LEADING
import javax.swing.GroupLayout.Alignment.TRAILING
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JComboBox
import javax.swing.JFormattedTextField
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.LayoutStyle.ComponentPlacement.RELATED
import javax.swing.LayoutStyle.ComponentPlacement.UNRELATED
import javax.swing.UIManager
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.text.DefaultFormatterFactory
import javax.swing.text.NumberFormatter

class Separador constructor(): JFrame() {
  private val base: Base
  private var btbuscar: JButton? = null
  private var cbdestino: JCheckBox? = null
  private var cbfiltros: JCheckBox? = null
  private var cbsinalQtd: JComboBox<String>? = null
  private var jLabel1: JLabel? = null
  private var jLabel10: JLabel? = null
  private var jLabel11: JLabel? = null
  private var jLabel12: JLabel? = null
  private var jLabel13: JLabel? = null
  private var jLabel14: JLabel? = null
  private var jLabel19: JLabel? = null
  private var jLabel2: JLabel? = null
  private var jLabel20: JLabel? = null
  private var jLabel21: JLabel? = null
  private var jLabel3: JLabel? = null
  private var jLabel4: JLabel? = null
  private var jLabel5: JLabel? = null
  private var jLabel6: JLabel? = null
  private var jLabel7: JLabel? = null
  private var jLabel8: JLabel? = null
  private var jLabel9: JLabel? = null
  private var painelDuplicacao: JPanel? = null
  private var painelFiltros: JPanel? = null
  private var tfLocCD: JTextField? = null
  private var tfcentrol: JTextField? = null
  private var tfdescFim: JTextField? = null
  private var tfdescIni: JTextField? = null
  private var tfdestino: JFormattedTextField? = null
  private var tffornecedores: JTextField? = null
  private var tflocalizacao: JTextField? = null
  private var tflojaDestino: JFormattedTextField? = null
  private var tflojaOrigem: JFormattedTextField? = null
  private var tflojaloc: JFormattedTextField? = null
  private var tfpedidoOrigem: JFormattedTextField? = null
  private var tfqtd1: JTextField? = null
  private var tfqtd2: JTextField? = null
  private var tftipos: JTextField? = null
  private fun initComponents() {
    painelDuplicacao = JPanel()
    jLabel1 = JLabel()
    tflojaOrigem = JFormattedTextField()
    jLabel2 = JLabel()
    tfpedidoOrigem = JFormattedTextField()
    jLabel3 = JLabel()
    tflojaDestino = JFormattedTextField()
    jLabel13 = JLabel()
    tfdestino = JFormattedTextField()
    cbdestino = JCheckBox()
    jLabel14 = JLabel()
    btbuscar = JButton()
    painelFiltros = JPanel()
    jLabel4 = JLabel()
    cbfiltros = JCheckBox()
    jLabel5 = JLabel()
    jLabel9 = JLabel()
    jLabel10 = JLabel()
    tfdescIni = JTextField()
    tfdescFim = JTextField()
    jLabel6 = JLabel()
    jLabel7 = JLabel()
    tffornecedores = JTextField()
    tftipos = JTextField()
    jLabel8 = JLabel()
    jLabel11 = JLabel()
    jLabel12 = JLabel()
    tflojaloc = JFormattedTextField()
    tflocalizacao = JTextField()
    jLabel19 = JLabel()
    tfcentrol = JTextField()
    jLabel20 = JLabel()
    cbsinalQtd = JComboBox()
    jLabel21 = JLabel()
    tfLocCD = JTextField()
    tfqtd1 = JTextField()
    tfqtd2 = JTextField()
    setDefaultCloseOperation(2)
    setTitle("Separador de Pedidos")
    painelDuplicacao!!.setBackground(Color(204, 204, 255))
    jLabel1!!.setFont(Font("Tahoma", 0, 12))
    jLabel1!!.setText("Loja de Origem:")
    tflojaOrigem!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tflojaOrigem!!.setFont(Font("Tahoma", 0, 12))
    jLabel2!!.setFont(Font("Tahoma", 0, 12))
    jLabel2!!.setText("Número Pedido de Origem:")
    tfpedidoOrigem!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tfpedidoOrigem!!.setFont(Font("Tahoma", 0, 12))
    jLabel3!!.setFont(Font("Tahoma", 0, 12))
    jLabel3!!.setText("Loja de Destino:")
    tflojaDestino!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tflojaDestino!!.setFont(Font("Tahoma", 0, 12))
    jLabel13!!.setFont(Font("Tahoma", 0, 12))
    jLabel13!!.setText("Número do Pedido de Destino:")
    tfdestino!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tfdestino!!.setFont(Font("Tahoma", 0, 12))
    cbdestino!!.setBackground(Color(204, 204, 255))
    cbdestino!!.setFont(Font("Tahoma", 0, 12))
    cbdestino!!.setSelected(true)
    cbdestino!!.setText("Informar número do pedido de destino")
    cbdestino!!.setCursor(Cursor(0))
    cbdestino!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        cbdestinoActionPerformed(evt)
      }
    })
    jLabel14!!.setFont(Font("Tahoma", 1, 24))
    jLabel14!!.setText("Separador de Pedidos")
    val painelDuplicacaoLayout: GroupLayout = GroupLayout(painelDuplicacao)
    painelDuplicacao!!.setLayout(painelDuplicacaoLayout)
    painelDuplicacaoLayout.setHorizontalGroup(painelDuplicacaoLayout.createParallelGroup(LEADING).addGroup(
      painelDuplicacaoLayout.createSequentialGroup().addGroup(painelDuplicacaoLayout.createParallelGroup(LEADING).addGroup(
        painelDuplicacaoLayout.createSequentialGroup().addContainerGap().addGroup(painelDuplicacaoLayout.createParallelGroup(
          LEADING).addGroup(painelDuplicacaoLayout.createSequentialGroup().addComponent(jLabel13).addGap(18,
                                                                                                         18,
                                                                                                         18).addComponent(
          tfdestino,
          -2,
          148,
          -2)).addGroup(painelDuplicacaoLayout.createSequentialGroup().addGroup(painelDuplicacaoLayout.createParallelGroup(
          TRAILING,
          false).addComponent(jLabel1, -1, -1, 32767).addComponent(jLabel2, LEADING, -1, 169, 32767).addComponent(
          jLabel3,
          LEADING,
          -1,
          -1,
          32767)).addPreferredGap(UNRELATED).addGroup(painelDuplicacaoLayout.createParallelGroup(LEADING).addComponent(
          tflojaOrigem,
          -2,
          40,
          -2).addComponent(tfpedidoOrigem, -2, 151, -2).addComponent(tflojaDestino, -2, 40, -2))))).addGroup(
        painelDuplicacaoLayout.createSequentialGroup().addGap(192, 192, 192).addComponent(cbdestino,
                                                                                          -2,
                                                                                          352,
                                                                                          -2))).addGap(0,
                                                                                                       0,
                                                                                                       32767)).addGroup(
      painelDuplicacaoLayout.createSequentialGroup().addContainerGap().addComponent(jLabel14).addContainerGap(-1,
                                                                                                              32767)))
    painelDuplicacaoLayout.setVerticalGroup(painelDuplicacaoLayout.createParallelGroup(LEADING).addGroup(
      painelDuplicacaoLayout.createSequentialGroup().addContainerGap().addComponent(jLabel14).addPreferredGap(RELATED,
                                                                                                              20,
                                                                                                              32767).addGroup(
        painelDuplicacaoLayout.createParallelGroup(BASELINE).addComponent(jLabel1).addComponent(tflojaOrigem,
                                                                                                -2,
                                                                                                -1,
                                                                                                -2)).addPreferredGap(
        RELATED).addGroup(painelDuplicacaoLayout.createParallelGroup(BASELINE).addComponent(tfpedidoOrigem,
                                                                                            -2,
                                                                                            -1,
                                                                                            -2).addComponent(jLabel2)).addPreferredGap(
        RELATED).addGroup(painelDuplicacaoLayout.createParallelGroup(BASELINE).addComponent(jLabel3).addComponent(
        tflojaDestino,
        -2,
        -1,
        -2)).addGap(18, 18, 18).addComponent(cbdestino, -2, 28, -2).addPreferredGap(UNRELATED).addGroup(
        painelDuplicacaoLayout.createParallelGroup(BASELINE).addComponent(jLabel13).addComponent(tfdestino,
                                                                                                 -2,
                                                                                                 -1,
                                                                                                 -2)).addGap(16,
                                                                                                             16,
                                                                                                             16)))
    btbuscar!!.setFont(Font("Tahoma", 0, 18))
    btbuscar!!.setText("Buscar")
    btbuscar!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        btbuscarActionPerformed(evt)
      }
    })
    painelFiltros!!.setBackground(Color(204, 255, 204))
    jLabel4!!.setFont(Font("Tahoma", 0, 12))
    jLabel4!!.setText("Filtro Produtos:")
    cbfiltros!!.setFont(Font("Tahoma", 0, 12))
    cbfiltros!!.setSelected(true)
    cbfiltros!!.setText("Utiliza filtros")
    cbfiltros!!.addActionListener(object: ActionListener {
      public override fun actionPerformed(evt: ActionEvent) {
        cbfiltrosActionPerformed(evt)
      }
    })
    jLabel5!!.setFont(Font("Tahoma", 0, 12))
    jLabel5!!.setText("Descrição: ")
    jLabel9!!.setFont(Font("Tahoma", 0, 12))
    jLabel9!!.setText("Inicio:")
    jLabel10!!.setFont(Font("Tahoma", 0, 12))
    jLabel10!!.setText("Fim:")
    tfdescIni!!.setFont(Font("Tahoma", 0, 12))
    tfdescIni!!.addFocusListener(object: FocusAdapter() {
      public override fun focusLost(evt: FocusEvent) {
        tfdescIniFocusLost(evt)
      }
    })
    tfdescIni!!.addKeyListener(object: KeyAdapter() {
      public override fun keyReleased(evt: KeyEvent) {
        tfdescIniKeyReleased(evt)
      }
    })
    tfdescFim!!.setFont(Font("Tahoma", 0, 12))
    tfdescFim!!.addFocusListener(object: FocusAdapter() {
      public override fun focusLost(evt: FocusEvent) {
        tfdescFimFocusLost(evt)
      }
    })
    tfdescFim!!.addKeyListener(object: KeyAdapter() {
      public override fun keyReleased(evt: KeyEvent) {
        tfdescFimKeyReleased(evt)
      }
    })
    jLabel6!!.setFont(Font("Tahoma", 0, 12))
    jLabel6!!.setText("Fornecedores:")
    jLabel7!!.setFont(Font("Tahoma", 0, 12))
    jLabel7!!.setText("Tipos:")
    tffornecedores!!.setFont(Font("Tahoma", 0, 12))
    tftipos!!.setFont(Font("Tahoma", 0, 12))
    jLabel8!!.setFont(Font("Tahoma", 0, 12))
    jLabel8!!.setText("Areas:")
    jLabel11!!.setFont(Font("Tahoma", 0, 12))
    jLabel11!!.setText("Loja Localização")
    jLabel12!!.setFont(Font("Tahoma", 0, 12))
    jLabel12!!.setText("Descrição da Localização")
    tflojaloc!!.setFormatterFactory(DefaultFormatterFactory(NumberFormatter(DecimalFormat("#0"))))
    tflojaloc!!.setFont(Font("Tahoma", 0, 12))
    tflocalizacao!!.setFont(Font("Tahoma", 0, 12))
    tflocalizacao!!.addFocusListener(object: FocusAdapter() {
      public override fun focusLost(evt: FocusEvent) {
        tflocalizacaoFocusLost(evt)
      }
    })
    jLabel19!!.setFont(Font("Tahoma", 0, 12))
    jLabel19!!.setText("Cent. de Lucro:")
    tfcentrol!!.setFont(Font("Tahoma", 0, 12))
    tfcentrol!!.addFocusListener(object: FocusAdapter() {
      public override fun focusLost(evt: FocusEvent) {
        tfcentrolFocusLost(evt)
      }
    })
    tfcentrol!!.addKeyListener(object: KeyAdapter() {
      public override fun keyReleased(evt: KeyEvent) {
        tfcentrolKeyReleased(evt)
      }
    })
    jLabel20!!.setFont(Font("Tahoma", 0, 12))
    jLabel20!!.setText("Quantidade:")
    cbsinalQtd!!.setFont(Font("Tahoma", 0, 12))
    cbsinalQtd!!.setModel(DefaultComboBoxModel(arrayOf("Todos",
                                                       "Maior que   \">\"",
                                                       "Menor que  \"<\"",
                                                       "Igual a        \"=\"",
                                                       "Entre",
                                                       " ")))
    jLabel21!!.setFont(Font("Tahoma", 0, 12))
    jLabel21!!.setText("Localização")
    tfLocCD!!.setFont(Font("Tahoma", 0, 12))
    tfLocCD!!.addKeyListener(object: KeyAdapter() {
      public override fun keyTyped(evt: KeyEvent) {
        tfLocCDKeyTyped(evt)
      }
    })
    val painelFiltrosLayout: GroupLayout = GroupLayout(painelFiltros)
    painelFiltros!!.setLayout(painelFiltrosLayout)
    painelFiltrosLayout.setHorizontalGroup(painelFiltrosLayout.createParallelGroup(LEADING).addGroup(painelFiltrosLayout.createSequentialGroup().addContainerGap().addGroup(
      painelFiltrosLayout.createParallelGroup(LEADING).addGroup(painelFiltrosLayout.createSequentialGroup().addGroup(
        painelFiltrosLayout.createParallelGroup(LEADING).addGroup(painelFiltrosLayout.createParallelGroup(TRAILING,
                                                                                                          false).addComponent(
          jLabel6,
          LEADING,
          -1,
          115,
          32767).addComponent(jLabel5, LEADING, -1, -1, 32767)).addComponent(jLabel8).addComponent(jLabel7,
                                                                                                   -2,
                                                                                                   60,
                                                                                                   -2).addComponent(
          jLabel4,
          -2,
          134,
          -2)).addGap(14, 14, 14).addGroup(painelFiltrosLayout.createParallelGroup(LEADING, false).addGroup(
        painelFiltrosLayout.createSequentialGroup().addComponent(tfdescIni, -2, 68, -2).addGap(37, 37, 37).addComponent(
          tfdescFim,
          -2,
          71,
          -2)).addGroup(painelFiltrosLayout.createSequentialGroup().addGap(8, 8, 8).addComponent(jLabel9).addGap(64,
                                                                                                                 64,
                                                                                                                 64).addComponent(
        jLabel10)).addComponent(cbfiltros).addComponent(tffornecedores, -2, 889, -2).addComponent(tftipos,
                                                                                                  -2,
                                                                                                  889,
                                                                                                  -2).addGroup(
        painelFiltrosLayout.createSequentialGroup().addGroup(painelFiltrosLayout.createParallelGroup(LEADING).addComponent(
          jLabel11).addComponent(tflojaloc, -2, 51, -2)).addGap(27,
                                                                27,
                                                                27).addGroup(painelFiltrosLayout.createParallelGroup(
          LEADING).addComponent(tflocalizacao,
                                -2,
                                318,
                                -2).addComponent(jLabel12))))).addGroup(painelFiltrosLayout.createSequentialGroup().addComponent(
        jLabel19,
        -2,
        142,
        -2).addPreferredGap(RELATED).addComponent(tfcentrol,
                                                  -2,
                                                  162,
                                                  -2)).addGroup(painelFiltrosLayout.createSequentialGroup().addComponent(
        jLabel21,
        -2,
        142,
        -2).addPreferredGap(RELATED).addComponent(tfLocCD,
                                                  -2,
                                                  63,
                                                  -2)).addGroup(painelFiltrosLayout.createSequentialGroup().addComponent(
        jLabel20,
        -2,
        142,
        -2).addPreferredGap(RELATED).addComponent(cbsinalQtd, -2, 253, -2).addGap(18, 18, 18).addComponent(tfqtd1,
                                                                                                           -2,
                                                                                                           86,
                                                                                                           -2).addGap(18,
                                                                                                                      18,
                                                                                                                      18).addComponent(
        tfqtd2,
        -2,
        82,
        -2))).addGap(0, 79, 32767)))
    painelFiltrosLayout.setVerticalGroup(painelFiltrosLayout.createParallelGroup(LEADING).addGroup(painelFiltrosLayout.createSequentialGroup().addContainerGap().addGroup(
      painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(jLabel4).addComponent(cbfiltros)).addPreferredGap(
      RELATED).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(jLabel9).addComponent(jLabel10)).addPreferredGap(
      UNRELATED).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(tfdescIni,
                                                                                         -2,
                                                                                         -1,
                                                                                         -2).addComponent(jLabel5).addComponent(
      tfdescFim,
      -2,
      -1,
      -2)).addGap(18,
                  18,
                  18).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(jLabel6).addComponent(
      tffornecedores,
      -2,
      -1,
      -2)).addGap(21, 21, 21).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(tftipos,
                                                                                                      -2,
                                                                                                      -1,
                                                                                                      -2).addComponent(
      jLabel7)).addGap(18,
                       18,
                       18).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(jLabel11).addComponent(
      jLabel12)).addGap(18, 18, 18).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(tflojaloc,
                                                                                                            -2,
                                                                                                            -1,
                                                                                                            -2).addComponent(
      tflocalizacao,
      -2,
      -1,
      -2).addComponent(jLabel8)).addPreferredGap(UNRELATED).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(
      jLabel19,
      -2,
      31,
      -2).addComponent(tfcentrol,
                       -2,
                       -1,
                       -2)).addPreferredGap(RELATED).addGroup(painelFiltrosLayout.createParallelGroup(BASELINE).addComponent(
      jLabel21,
      -2,
      31,
      -2).addComponent(tfLocCD, -2, -1, -2)).addGap(11, 11, 11).addGroup(painelFiltrosLayout.createParallelGroup(
      BASELINE).addComponent(jLabel20, -2, 31, -2).addComponent(cbsinalQtd, -2, -1, -2).addComponent(tfqtd1,
                                                                                                     -2,
                                                                                                     -1,
                                                                                                     -2).addComponent(
      tfqtd2,
      -2,
      -1,
      -2)).addContainerGap(29, 32767)))
    val layout: GroupLayout = GroupLayout(getContentPane())
    getContentPane().setLayout(layout)
    layout.setHorizontalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(
      layout.createParallelGroup(LEADING).addComponent(painelFiltros,
                                                       -1,
                                                       -1,
                                                       32767).addGroup(layout.createSequentialGroup().addComponent(
        btbuscar).addGap(0, 0, 32767)).addComponent(painelDuplicacao, -1, -1, 32767)).addContainerGap()))
    layout.setVerticalGroup(layout.createParallelGroup(LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(
      painelDuplicacao,
      -2,
      -1,
      -2).addPreferredGap(RELATED).addComponent(painelFiltros, -2, -1, -2).addPreferredGap(RELATED).addComponent(
      btbuscar).addContainerGap(-1, 32767)))
    pack()
  }

  private fun tfdescIniKeyReleased(evt: KeyEvent) {
    if(tfdescIni!!.getText().length >= 3) {
      tfdescFim!!.requestFocus()
    }
  }

  private fun tfdescFimKeyReleased(evt: KeyEvent) {
    if(tfdescFim!!.getText().length >= 3) {
      tffornecedores!!.requestFocus()
    }
  }

  private fun btbuscarActionPerformed(evt: ActionEvent) {
    try {
      if((!(tflojaOrigem!!.getText() == "") && !(tfpedidoOrigem!!.getText() == "") && !(tflojaDestino!!.getText() == ""))) {
        if(!(tflocalizacao!!.getText() == "") && (tflojaloc!!.getText() == "")) {
          JOptionPane.showMessageDialog(this,
                                        "Ao utilizar o filtro por Area, o campo loja da area se torna obrigatório")
        }
        else if((tflocalizacao!!.getText() == "") && !(tflojaloc!!.getText() == "")) {
          JOptionPane.showMessageDialog(this,
                                        "Ao utilizar o filtro por Area, o campo descrição da area se torna obrigatório")
        }
        else {
          val gestorDADOS: GestorDADOS = GestorDADOS()
          this.base.lojaOrigem = Integer.valueOf(tflojaOrigem!!.getText().toInt())
          this.base.pedidoOrigem = Integer.valueOf(tfpedidoOrigem!!.getText().toInt())
          this.base.lojaDestino = Integer.valueOf(tflojaDestino!!.getText().toInt())
          if((tfdestino!!.getText() == "")) {
            this.base.pedidoDestino = Integer.valueOf(0)
          }
          else {
            this.base.pedidoDestino = Integer.valueOf(tfdestino!!.getText().toInt())
          }
          this.base.descIni = tfdescIni!!.getText()
          this.base.descFim = tfdescFim!!.getText()
          this.base.fornecedores = tffornecedores!!.getText()
          this.base.tipos = tftipos!!.getText()
          if((tflojaloc!!.getText() == "")) {
            this.base.lojaArea = Integer.valueOf(0)
          }
          else {
            this.base.lojaArea = Integer.valueOf(tflojaloc!!.getText().toInt())
          }
          this.base.areas = tflocalizacao!!.getText()
          this.base.centrodeLucro = tfcentrol!!.getText()
          if(cbsinalQtd!!.getSelectedIndex() == 0) {
            this.base.sinalQtd = "todos"
          }
          else if(cbsinalQtd!!.getSelectedIndex() == 1) {
            this.base.sinalQtd = ">"
          }
          else if(cbsinalQtd!!.getSelectedIndex() == 2) {
            this.base.sinalQtd = "<"
          }
          else if(cbsinalQtd!!.getSelectedIndex() == 3) {
            this.base.sinalQtd = "="
          }
          else {
            this.base.sinalQtd = "entre"
          }
          if((tfqtd1!!.getText() == "")) {
            this.base.qtd1 = BigDecimal.ZERO
          }
          else {
            this.base.qtd1 = BigDecimal(tfqtd1!!.getText().replace(",", "."))
          }
          if((tfqtd2!!.getText() == "")) {
            this.base.qtd2 = BigDecimal.ZERO
          }
          else {
            this.base.qtd2 = BigDecimal(tfqtd2!!.getText().replace(",", "."))
          }
          this.base.localizacao = tfLocCD!!.getText()
          this.base.informaPedidoDestino = java.lang.Boolean.valueOf(cbdestino!!.isSelected())
          this.base.usaFiltrosProduto = java.lang.Boolean.valueOf(cbfiltros!!.isSelected())
          val produtos: List<Produto?> = gestorDADOS.listar(this.base)
          val listagem: Listagem = Listagem()
          listagem.setDados(produtos, this.base)
          listagem.setVisible(true)
        }
      }
      else {
        JOptionPane.showMessageDialog(this,
                                      "Os Campos Loja de Origem, Pedido de Origem e Loja de Destino são obrigatórios")
      }
    } catch(e: Exception) {
      JOptionPane.showMessageDialog(this, "Não foi possível Listar os produtos! Erro:" + e)
      e.printStackTrace()
    }
  }

  private fun tfdescIniFocusLost(evt: FocusEvent) {
    tfdescIni!!.setText(tfdescIni!!.getText().toUpperCase())
  }

  private fun tfdescFimFocusLost(evt: FocusEvent) {
    tfdescFim!!.setText(tfdescFim!!.getText().toUpperCase())
  }

  private fun tflocalizacaoFocusLost(evt: FocusEvent) {
    tflocalizacao!!.setText(tflocalizacao!!.getText().toUpperCase())
  }

  private fun cbdestinoActionPerformed(evt: ActionEvent) {
    if(cbdestino!!.isSelected()) {
      tfdestino!!.setEnabled(true)
    }
    else {
      tfdestino!!.setEnabled(false)
    }
  }

  private fun cbfiltrosActionPerformed(evt: ActionEvent) {
    if(cbfiltros!!.isSelected()) {
      tfdescIni!!.setEnabled(true)
      tfdescFim!!.setEnabled(true)
      tffornecedores!!.setEnabled(true)
      tftipos!!.setEnabled(true)
      tflojaloc!!.setEnabled(true)
      tflocalizacao!!.setEnabled(true)
      tfcentrol!!.setEnabled(true)
      tfqtd1!!.setEnabled(true)
      tfqtd2!!.setEnabled(true)
      cbsinalQtd!!.setEnabled(true)
      tfLocCD!!.setEnabled(true)
    }
    else {
      tfdescIni!!.setEnabled(false)
      tfdescFim!!.setEnabled(false)
      tffornecedores!!.setEnabled(false)
      tftipos!!.setEnabled(false)
      tflojaloc!!.setEnabled(false)
      tflocalizacao!!.setEnabled(false)
      tfcentrol!!.setEnabled(false)
      tfqtd1!!.setEnabled(false)
      tfqtd2!!.setEnabled(false)
      cbsinalQtd!!.setEnabled(false)
      tfLocCD!!.setEnabled(false)
    }
  }

  private fun tfcentrolFocusLost(evt: FocusEvent) {}
  private fun tfcentrolKeyReleased(evt: KeyEvent) {}
  private fun tfLocCDKeyTyped(evt: KeyEvent) {
    val max: Boolean = (tfLocCD!!.getText().length > 4)
    if(max) {
      evt.consume()
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
        Logger.getLogger(Separador::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: InstantiationException) {
        Logger.getLogger(Separador::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: IllegalAccessException) {
        Logger.getLogger(Separador::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      } catch(ex: UnsupportedLookAndFeelException) {
        Logger.getLogger(Separador::class.java.getName())
          .log(Level.SEVERE, null as String?, ex)
      }
      EventQueue.invokeLater(object: Runnable {
        public override fun run() {
          (Separador()).setVisible(true)
        }
      })
    }
  }

  init {
    initComponents()
    this.base = Base()
  }
}