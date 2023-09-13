package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.IPendenciaView
import br.com.astrosoft.separacao.viewmodel.PendenciaViewModel
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.grid.ColumnTextAlign.CENTER
import com.vaadin.flow.component.grid.ColumnTextAlign.END
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE_O
import com.vaadin.flow.component.icon.VaadinIcon.CIRCLE_THIN
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.text.DecimalFormat

@Route(layout = SeparacaoLayout::class)
@PageTitle("Pendencia")
@HtmlImport("frontend://styles/shared-styles.html")
class PendenciaView: ViewLayout<PendenciaViewModel>(), IPendenciaView {
  private lateinit var pedidoMae: IntegerField
  private var gridProduto: Grid<ProdutoPedido>
  private lateinit var cmbPedido: Select<Pedido>
  override val viewModel: PendenciaViewModel = PendenciaViewModel(this)
  private val dataProviderProdutos = ListDataProvider<ProdutoPedido>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = user.pendencia
  
  init {
    form("Pendencia pedidos") {
      isExpand = false
      cmbPedido = select("Numero do pedido") {
        colspan = 1
        update()
        setItemLabelGenerator {
          "${it.ordno} - ${it.tipoOrigem.descricao} - ${it.data.format()}"
        }
        addValueChangeListener {evento ->
          if(evento.isFromClient) {
            updateGrid(this.value)
          }
        }
      }
      pedidoMae = integerField("Pedido mãe") {
        isEnabled = false
      }
    }
    gridProduto = grid(dataProvider = dataProviderProdutos) {
      isExpand = true
      this.isMultiSort = true
      addThemeVariants(LUMO_COMPACT)
      setSelectionMode(SelectionMode.SINGLE)

      addColumnFor(ProdutoPedido::estoqueLoja, renderer = ComponentRenderer<Icon, ProdutoPedido> {produto ->
        if(produto.estoqueLoja == true)
          CHECK_CIRCLE_O.create()
        else
          CIRCLE_THIN.create()
      }) {
        setHeader("Loja")
        flexGrow = 1
        this.textAlign = CENTER
        this.setId("colLoja")
      }
      addColumnFor(ProdutoPedido::codigo) {
        setHeader("Código")
        flexGrow = 1
        this.textAlign = END
      }
      addColumnFor(ProdutoPedido::descricao) {
        setHeader("Descrição")
        flexGrow = 8
      }
      addColumnFor(ProdutoPedido::grade) {
        setHeader("Grade")
        flexGrow = 1
      }
      addColumnFor(ProdutoPedido::fornecedor) {
        setHeader("Fornecedor")
        flexGrow = 1
      }
      addColumnFor(ProdutoPedido::localizacao) {
        setHeader("Localização")
        flexGrow = 3
      }
      addColumnFor(ProdutoPedido::qttyEdit, NumberRenderer(ProdutoPedido::qttyEdit, DecimalFormat("0"))) {
        setHeader("Quant")
        flexGrow = 1
        this.textAlign = END
      }
      addColumnFor(ProdutoPedido::saldo, NumberRenderer(ProdutoPedido::saldo, DecimalFormat("0"))) {
        setHeader("Saldo")
        flexGrow = 1
        this.textAlign = END
      }

      sort(listOf(
        GridSortOrder(getColumnBy(ProdutoPedido::localizacao), ASCENDING),
        GridSortOrder(getColumnBy(ProdutoPedido::descricao), ASCENDING),
        GridSortOrder(getColumnBy(ProdutoPedido::grade), ASCENDING)
                 ))
    }
  }

  private fun @VaadinDsl Select<Pedido>.update() {
    val pedido = this.value
    setItems(viewModel.pedidosSeparacao())
    this.value = pedido
  }

  override val pedido: Pedido?
    get() = cmbPedido.value
  override val produtos: List<ProdutoPedido>
    get() = dataProviderProdutos.getAll()
  
  override fun updateGrid() {
    val pedidoAtual = pedido
    updateGrid(pedidoAtual)
    cmbPedido.setItems(viewModel.pedidos())
    cmbPedido.value = pedidoAtual
  }

  private fun updateGrid(pedidoNovo: Pedido?) {
    val userSaci = UserSaci.userAtual
    gridProduto.selectionModel.deselectAll()
    dataProviderProdutos.items.clear()
    dataProviderProdutos.items.addAll(pedidoNovo?.produtosPendente(userSaci).orEmpty())
    dataProviderProdutos.refreshAll()
    pedidoMae.value = pedidoNovo?.ordnoMae
  }
}

