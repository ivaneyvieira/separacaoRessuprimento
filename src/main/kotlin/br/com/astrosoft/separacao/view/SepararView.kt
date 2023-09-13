package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.ISepararView
import br.com.astrosoft.separacao.viewmodel.SepararViewModel
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.grid.HeaderRow
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.Autocapitalize
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.function.SerializablePredicate
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.text.DecimalFormat
import kotlin.reflect.KProperty1

@Route(layout = SeparacaoLayout::class)
@PageTitle("Separar")
class SepararView : ViewLayout<SepararViewModel>(), ISepararView {
  private lateinit var proximoNumero: IntegerField
  private var gridProduto: Grid<ProdutoPedido>
  private lateinit var cmbPedido: Select<Pedido>
  override val viewModel = SepararViewModel(this)
  private val dataProviderProdutos = ListDataProvider<ProdutoPedido>(mutableListOf())
  private var produtoInicial: ProdutoPedido? = null
  private var produtoFinal: ProdutoPedido? = null
  override fun isAccept(user: UserSaci) = user.separar

  init {
    form("Separar Pedidos") {
      isExpand = false
      cmbPedido = select("Pedido origem") {
        colspan = 1
        update()
        setItemLabelGenerator { it.label }
        addValueChangeListener { evento ->
          if (evento.isFromClient) {
            updateGrid(this.value)
          }
        }
      }
      proximoNumero = integerField("Próximo número") {
        isEnabled = false
      }
    }
    gridProduto = grid(dataProvider = dataProviderProdutos) {
      isExpand = true
      setSelectionMode(SelectionMode.MULTI)
      addThemeVariants(LUMO_COMPACT)
      this.isMultiSort = true
      this.appendHeaderRow()
      val filterRow: HeaderRow = this.appendHeaderRow()
      val edtCodigo = TextFieldFiltro(ProdutoPedido::codigo)
      val edtDescricao = TextFieldFiltro(ProdutoPedido::descricao)
      val edtGrade = TextFieldFiltro(ProdutoPedido::grade)
      val edtFornecedor = TextFieldFiltro(ProdutoPedido::fornecedor)
      val edtLocalizacao = TextFieldFiltro(ProdutoPedido::localizacao)
      edtCodigo.addValueChangeListener {
        updateFilter(edtCodigo, edtDescricao, edtGrade, edtFornecedor, edtLocalizacao)
      }
      edtDescricao.addValueChangeListener {
        updateFilter(edtCodigo, edtDescricao, edtGrade, edtFornecedor, edtLocalizacao)
      }
      edtGrade.addValueChangeListener {
        updateFilter(edtCodigo, edtDescricao, edtGrade, edtFornecedor, edtLocalizacao)
      }
      edtFornecedor.addValueChangeListener {
        updateFilter(edtCodigo, edtDescricao, edtGrade, edtFornecedor, edtLocalizacao)
      }
      edtLocalizacao.addValueChangeListener {
        updateFilter(edtCodigo, edtDescricao, edtGrade, edtFornecedor, edtLocalizacao)
      }
      val binder = Binder<ProdutoPedido>(ProdutoPedido::class.java)
      binder.withValidator { value, _ ->
        if (value.quantidadeValida) {
          ValidationResult.ok()
        } else {
          val msg = "A quantidade deveria está entre ${value.qttyMin} e ${value.qttyMax}"
          showError(msg)
          ValidationResult.error(msg)
        }
      }
      this.editor.binder = binder
      val edtQtty = IntegerField().apply {
        this.isAutoselect = true
        this.width = "100%"
        this.isAutofocus = true
        this.element.addEventListener("keydown") { this@grid.editor.cancel() }.filter = "event.key === 'Enter'"
      }

      binder.bind(edtQtty, ProdutoPedido::qttyEdit.name)

      addItemClickListener { event ->
        this.editor.editItem(event.item)
        edtQtty.focus()
      }

      binder.addValueChangeListener {
        this.editor.refresh()
      }

      this.editor.addCloseListener {
        binder.writeBean(it.item)
      }

      this.addColumnFor(ProdutoPedido::codigo) {
        setHeader("Código")
        this.flexGrow = 1
        this.textAlign = ColumnTextAlign.END
        filterRow.getCell(this).setComponent(edtCodigo)
      }
      this.addColumnFor(ProdutoPedido::descricao) {
        setHeader("Descrição")
        this.flexGrow = 8
        filterRow.getCell(this).setComponent(edtDescricao)
      }
      this.addColumnFor(ProdutoPedido::grade) {
        setHeader("Grade")
        this.flexGrow = 1
        filterRow.getCell(this).setComponent(edtGrade)
      }
      this.addColumnFor(ProdutoPedido::fornecedor) {
        setHeader("Fornecedor")
        this.flexGrow = 1
        filterRow.getCell(this).setComponent(edtFornecedor)
      }
      this.addColumnFor(ProdutoPedido::localizacao) {
        setHeader("Localização")
        this.flexGrow = 3
        filterRow.getCell(this).setComponent(edtLocalizacao)
      }
      this.addColumnFor(ProdutoPedido::qttyEdit, NumberRenderer(ProdutoPedido::qttyEdit, DecimalFormat("0"))) {
        setHeader("Quant")
        this.flexGrow = 1
        this.textAlign = ColumnTextAlign.END
        setEditorComponent(edtQtty)
      }
      this.addColumnFor(ProdutoPedido::saldo, NumberRenderer(ProdutoPedido::saldo, DecimalFormat("0"))) {
        setHeader("Saldo")
        this.flexGrow = 1
        this.textAlign = ColumnTextAlign.END
      }
      sort(
        listOf(
          GridSortOrder(this.getColumnBy(ProdutoPedido::localizacao), SortDirection.ASCENDING),
          GridSortOrder(this.getColumnBy(ProdutoPedido::descricao), SortDirection.ASCENDING),
          GridSortOrder(this.getColumnBy(ProdutoPedido::grade), SortDirection.ASCENDING)
        )
      )

      shiftSelect()
    }
    toolbar {
      button("Separar") {
        icon = VaadinIcon.SPLIT.create()
        addClickListener {
          viewModel.separar()
          cmbPedido.update()
        }
      }
      button("Imprimir") {
        icon = VaadinIcon.PRINT.create()
        addClickListener {
          viewModel.imprimir()
        }
      }
      if (UserSaci.userAtual?.admin == true) {
        button("Imprimir Selecionado") {
          icon = VaadinIcon.PRINT.create()
          addClickListener {
            viewModel.imprimirSelecionado()
          }
        }
      }
    }
  }

  private fun @VaadinDsl Select<Pedido>.update() {
    val pedido = this.value
    setItems(viewModel.pedidos())
    this.value = pedido
  }

  private fun @VaadinDsl Grid<ProdutoPedido>.shiftSelect() {
    this.addItemClickListener { evento ->
      val grade = evento.source
      if (evento.isShiftKey) {
        val pedido = evento.item
        if (produtoInicial == null) {
          produtoInicial = pedido
          grade.select(pedido)
        } else {
          if (produtoFinal == null) {
            val itens = list(grade)
            produtoFinal = pedido
            val p1 = itens.indexOf(produtoInicial!!)
            val p2 = itens.indexOf(produtoFinal!!) + 1
            val subList = itens.subList(p1.coerceAtMost(p2), p1.coerceAtLeast(p2))
            subList.forEach {
              grade.select(it)
            }
            produtoFinal = null
            produtoInicial = null
          } else {
            produtoFinal = null
            produtoInicial = null
          }
        }
      } else {
        produtoFinal = null
        produtoInicial = null
      }
    }
  }

  private fun updateFilter(
    edtCodigo: TextFieldFiltro,
    edtDescricao: TextFieldFiltro,
    edtGrade: TextFieldFiltro,
    edtFornecedor: TextFieldFiltro,
    edtLocalizacao: TextFieldFiltro
  ) {
    val filter =
        edtCodigo.filtro
          .and(edtDescricao.filtro)
          .and(edtGrade.filtro)
          .and(edtFornecedor.filtro)
          .and(edtLocalizacao.filtro)
    dataProviderProdutos.filter = filter
  }

  private fun list(grade: Grid<ProdutoPedido>): List<ProdutoPedido> {
    val filter = dataProviderProdutos.filter
    val queryOrdem = comparator(grade)
    return dataProviderProdutos.items.toList().filter {
      filter?.test(it) ?: true
    }.let { list ->
      if (queryOrdem == null) list
      else list.sortedWith<ProdutoPedido>(queryOrdem)
    }
  }

  private fun comparator(grade: Grid<ProdutoPedido>): Comparator<ProdutoPedido>? {
    if (grade.sortOrder.isEmpty()) return null
    return grade.sortOrder.mapNotNull { gridSort ->
      val prop =
          ProdutoPedido::class.members
            .toList()
            .filterIsInstance<KProperty1<ProdutoPedido, Comparable<*>>>()
            .firstOrNull { prop ->
              prop.name == gridSort.sorted.key
            }
      if (gridSort.direction == DESCENDING) compareByDescending {
        prop?.get(it)
      }
      else compareBy<ProdutoPedido> {
        prop?.get(it)
      }
    }.reduce { acc, comparator ->
      acc.thenComparing(comparator)
    }
  }

  override val pedido: Pedido?
    get() = cmbPedido.value
  override val produtosSelecionados: List<ProdutoPedido>
    get() = gridProduto.selectedItems.toList()

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
    dataProviderProdutos.items.addAll(pedidoNovo?.produtos(userSaci).orEmpty())
    dataProviderProdutos.refreshAll()
    proximoNumero.value = viewModel.proximoNumero()
  }
}

class TextFieldFiltro(private val property: KProperty1<ProdutoPedido, Any>) : TextField() {
  val filtro
    get() = SerializablePredicate<ProdutoPedido> { produto ->
      property.get(produto).toString().startsWith(value, ignoreCase = true)
    }

  init {
    setSizeFull()
    autocapitalize = Autocapitalize.CHARACTERS
    valueChangeMode = ValueChangeMode.TIMEOUT
    placeholder = "Filtro"
  }
}


