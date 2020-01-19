package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.EditarViewModel
import br.com.astrosoft.separacao.viewmodel.IEditarView
import com.github.mvysny.karibudsl.v10.addColumnFor
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.em
import com.github.mvysny.karibudsl.v10.formLayout
import com.github.mvysny.karibudsl.v10.getAll
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.responsiveSteps
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.text.DecimalFormat

@Route(layout = SeparacaoLayout::class)
@PageTitle("Editar")
class EditarView: ViewLayout<EditarViewModel>(), IEditarView {
  private lateinit var pedidoMae: IntegerField
  private var gridProduto: Grid<ProdutoPedido>
  private lateinit var cmbPedido: ComboBox<Pedido>
  override val viewModel: EditarViewModel = EditarViewModel(this)
  val dataProviderProdutos = ListDataProvider<ProdutoPedido>(mutableListOf())
  
  override fun isAccept(user: UserSaci) = user.editar
  
  init {
    form("Editar pedidos") {
      isExpand = false
      cmbPedido = comboBox<Pedido>("Numero do pedido") {
        colspan = 1
        setItems(viewModel.pedidosSeparacao)
        setItemLabelGenerator {
          "${it.ordnoOrigem.toString()} - ${it.tipoOrigem.descricao}"
        }
        isAllowCustomValue = false
        isPreventInvalidInput = false
        addValueChangeListener {evento ->
          if(evento.isFromClient) {
            updateGrid(value)
          }
        }
      }
      pedidoMae = integerField("Pedido mãe") {
        isEnabled = false
      }
    }
    gridProduto = grid(dataProvider = dataProviderProdutos) {
      isExpand = true
      isMultiSort = true
      setSelectionMode(SelectionMode.MULTI)
      val binder = Binder<ProdutoPedido>(ProdutoPedido::class.java)
      binder.withValidator {value, _ ->
        if(value.quantidadeValida) {
          ValidationResult.ok()
        }
        else {
          val msg = "A quantidade deveria está entre ${value.qttyMin} e ${value.qttyMax}"
          showError(msg)
          ValidationResult.error(msg)
        }
      }
      editor.binder = binder
      val edtQtty = IntegerField().apply {
        this.isAutoselect = true
        this.width = "100%"
        this.isAutofocus = true
        this.element
          .addEventListener("keydown") {_ -> this@grid.editor.cancel()}
          .filter = "event.key === 'Tab' && event.shiftKey"
      }
  
      binder.bind(edtQtty, ProdutoPedido::qttyEdit.name)
  
      addItemClickListener {event ->
        editor.editItem(event.item)
        edtQtty.focus()
      }
  
      binder.addValueChangeListener {_ ->
        editor.refresh()
      }
  
      editor.addCloseListener {
        binder.writeBean(it.item)
      }
  
      addColumnFor(ProdutoPedido::codigo) {
        setHeader("Código")
        flexGrow = 1
        this.textAlign = ColumnTextAlign.END
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
        this.textAlign = ColumnTextAlign.END
        setEditorComponent(edtQtty)
      }
      addColumnFor(ProdutoPedido::saldo, NumberRenderer(ProdutoPedido::saldo, DecimalFormat("0"))) {
        setHeader("Saldo")
        flexGrow = 1
        this.textAlign = ColumnTextAlign.END
      }
      sort(listOf(
        GridSortOrder(getColumnBy(ProdutoPedido::localizacao), ASCENDING),
        GridSortOrder(getColumnBy(ProdutoPedido::descricao), ASCENDING),
        GridSortOrder(getColumnBy(ProdutoPedido::grade), ASCENDING)
                 ))
    }
    toolbar {
      button("Novo produto") {
        isEnabled = false
        icon = VaadinIcon.INSERT.create()
        addClickListener {
          viewModel.novoProduto()
        }
      }
      button("Processar") {
        icon = VaadinIcon.SPLIT.create()
        addClickListener {
          viewModel.processar()
        }
      }
    }
  }
  
  override val pedido: Pedido?
    get() = Pedido.findTemp(cmbPedido.value?.ordno ?: 0)
  override val produtos: List<ProdutoPedido>
    get() = dataProviderProdutos.getAll()
  override val produtosSelecionados: List<ProdutoPedido>
    get() = gridProduto.selectedItems.toList()
  
  override fun updateGrid() {
    val pedidoAtual = pedido
    updateGrid(pedidoAtual)
  }
  
  override fun novoProduto(pedido: Pedido, processaProduto: (ProdutoPedido) -> Unit) {
    ProdutoDialog(viewModel).apply {
      open()
    }
  }
  
  private fun updateGrid(pedidoNovo: Pedido?) {
    gridProduto.selectionModel.deselectAll()
    dataProviderProdutos.items.clear()
    dataProviderProdutos.items.addAll(pedidoNovo?.produtos.orEmpty())
    dataProviderProdutos.refreshAll()
    pedidoMae.value = pedidoNovo?.ordnoMae
  }
}

class ProdutoDialog(val viewModel: EditarViewModel): Dialog() {
  private lateinit var edtQtty: IntegerField
  private lateinit var edtDescricao: TextField
  private lateinit var edtGrade: TextField
  private lateinit var edtCodigo: TextField
  
  init {
    em("Adiciona Produto")
    formLayout {
      responsiveSteps {
        "0px"(4, top)
      }
      edtCodigo = textField("Código") {
        colspan = 1
        addValueChangeListener {event ->
          val value = event.value
          val produto = viewModel.findProduto(value)
        }
      }
      edtDescricao = textField("Descrição") {
        colspan = 3
        isReadOnly = true
      }
      edtGrade = textField("Grade") {
        colspan = 1
      }
      edtQtty = integerField("Quantidade") {
        colspan = 1
      }
    }
    horizontalLayout {
      button("Salva")
      button("Cancela")
    }
  }
}