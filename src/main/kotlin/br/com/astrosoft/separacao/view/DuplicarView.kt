package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.DuplicarViewModel
import br.com.astrosoft.separacao.viewmodel.IDuplicarView
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.time.LocalDate

@Route(layout = SeparacaoLayout::class)
@PageTitle("Duplicar")
class DuplicarView: ViewLayout<DuplicarViewModel>(), IDuplicarView {
  private lateinit var chkInformarPedidoDestino: Checkbox
  private lateinit var edtPedidoDestino: IntegerField
  private lateinit var cmbPedidoOrigem: Select<Pedido>
  override val viewModel = DuplicarViewModel(this)
  
  init {
    form("Duplicar") {
      cmbPedidoOrigem = select("Pedido origem") {
        colspan = 1
        update()

        setItemLabelGenerator {it.label}

        addValueChangeListener {event ->
          if(event.isFromClient) {
            if(chkInformarPedidoDestino.value == false) {
              edtPedidoDestino.value = viewModel.proximoNumero()
            }
          }
        }
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
          cmbPedidoOrigem.update()
        }
      }
    }
    viewModel.init()
  }

  private fun @VaadinDsl Select<Pedido>.update() {
    val pedido = this.value
    val pedidos = viewModel.pedidos()
    setItems(pedidos)
    this.value = pedido
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
  override val data: LocalDate?
    get() = null
  override var informarNumero: Boolean?
    get() = chkInformarPedidoDestino.value
    set(value) {
      chkInformarPedidoDestino.value = value
    }
  
  override fun isAccept(user: UserSaci) = user.duplicar
}