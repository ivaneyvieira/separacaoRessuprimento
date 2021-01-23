package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.DuplicarViewModel
import br.com.astrosoft.separacao.viewmodel.IDuplicarView
import com.github.mvysny.karibudsl.v10.br
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.checkBox
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.integerField
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(layout = SeparacaoLayout::class)
@PageTitle("Duplicar")
class DuplicarView: ViewLayout<DuplicarViewModel>(), IDuplicarView {
  private lateinit var chkInformarPedidoDestino: Checkbox
  private lateinit var edtPedidoDestino: IntegerField
  private lateinit var cmbPedidoOrigem: ComboBox<Pedido>
  override val viewModel = DuplicarViewModel(this)
  
  init {
    form("Duplicar") {
      cmbPedidoOrigem = comboBox("Pedido origem") {
        colspan = 1
        val pedidos = viewModel.pedidos()
        setItems(pedidos)
        
        setItemLabelGenerator {it.label}
        isAllowCustomValue = false
        isPreventInvalidInput = false
      }
      br()
      chkInformarPedidoDestino = checkBox("Informar pedido destino") {
        colspan = 2
        addValueChangeListener {event ->
          edtPedidoDestino.isEnabled = event.value
          if(event.value == false) {
            edtPedidoDestino.value = viewModel.proximoNumero()
          }
        }
      }
      edtPedidoDestino = integerField("Pedido destino") {
        colspan = 1
      }
    }
    toolbar {
      button("Duplicar") {
        icon = VaadinIcon.COPY.create()
        addClickListener {
          viewModel.duplicar()
        }
      }
    }
    viewModel.init()
  }
  
  override var pedidoOrigem: Pedido?
    get() = cmbPedidoOrigem.value
    set(value) {
      cmbPedidoOrigem.value = value
    }
  override var numeroDestino: Int?
    get() = edtPedidoDestino.value
    set(value) {
      edtPedidoDestino.value = value
    }
  override var informarNumero: Boolean?
    get() = chkInformarPedidoDestino.value
    set(value) {
      chkInformarPedidoDestino.value = value
    }
  
  override fun isAccept(user: UserSaci) = user.duplicar
}