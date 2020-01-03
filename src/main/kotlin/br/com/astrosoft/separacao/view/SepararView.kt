package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.viewmodel.ISepararView
import br.com.astrosoft.separacao.viewmodel.SepararViewModel
import com.github.mvysny.karibudsl.v10.addColumnFor
import com.github.mvysny.karibudsl.v10.br
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.HeaderRow
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.provider.SortDirection.DESCENDING
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.function.SerializablePredicate
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.text.DecimalFormat
import java.util.*
import kotlin.reflect.KProperty1

@Route(layout = SeparacaoLayout::class)
@PageTitle("Separar")
class SepararView: ViewLayout<SepararViewModel>(), ISepararView {
  private lateinit var cmbPedido: ComboBox<Pedido>
  override val viewModel = SepararViewModel(this)
  //ListDataProvider<ProdutoPedido> = ListDataProvider(mutableListOf())
  val dataProviderProdutos = ListDataProvider<ProdutoPedido>(mutableListOf())
  var produtoInicial: ProdutoPedido? = null
  var produtoFinal: ProdutoPedido? = null
  
  init {
    form("Separar") {
      isExpand = false
      cmbPedido = comboBox<Pedido>("Pedido origem") {
        colspan = 1
        setItems(Pedido.pedidosTemporarios)
        setItemLabelGenerator {it.ordnoOrigem.toString()}
        isAllowCustomValue = false
        isPreventInvalidInput = false
        addValueChangeListener {evento ->
          if(evento.isFromClient) {
            val value = evento.value
            dataProviderProdutos.items.clear()
            dataProviderProdutos.items.addAll(value.produtos)
            dataProviderProdutos.refreshAll()
          }
        }
      }
      br()
    }
    grid(dataProvider = dataProviderProdutos) {
      isExpand = true
      setSelectionMode(SelectionMode.MULTI)
      isMultiSort = true
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
      addColumnFor(ProdutoPedido::codigo) {
        setHeader("Código")
        flexGrow = 1
        this.textAlign = ColumnTextAlign.END
        filterRow.getCell(this)
          .setComponent(edtCodigo)
      }
      addColumnFor(ProdutoPedido::descricao) {
        setHeader("Descrição")
        flexGrow = 8
        filterRow.getCell(this)
          .setComponent(edtDescricao)
      }
      addColumnFor(ProdutoPedido::grade) {
        setHeader("Grade")
        flexGrow = 1
        filterRow.getCell(this)
          .setComponent(edtGrade)
      }
      addColumnFor(ProdutoPedido::fornecedor) {
        setHeader("Fornecedor")
        flexGrow = 1
        filterRow.getCell(this)
          .setComponent(edtFornecedor)
      }
      addColumnFor(ProdutoPedido::localizacao) {
        setHeader("Localização")
        flexGrow = 3
        filterRow.getCell(this)
          .setComponent(edtLocalizacao)
      }
      addColumnFor(ProdutoPedido::qtty, NumberRenderer(ProdutoPedido::qtty, DecimalFormat("0.##"))) {
        setHeader("Quant")
        flexGrow = 1
        this.textAlign = ColumnTextAlign.END
      }
      sort(listOf(
        GridSortOrder(getColumnBy(ProdutoPedido::localizacao), SortDirection.ASCENDING),
        GridSortOrder(getColumnBy(ProdutoPedido::grade), SortDirection.ASCENDING),
        GridSortOrder(getColumnBy(ProdutoPedido::descricao), SortDirection.ASCENDING)
                 )
          )
      val grade = this
      this.addItemClickListener {evento ->
        if(evento.isShiftKey) {
          val pedido = evento.item
          if(produtoInicial == null) {
            produtoInicial = pedido
            grade.select(pedido)
          }
          else {
            if(produtoFinal == null) {
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
            }
            else {
              produtoFinal = null
              produtoInicial = null
            }
          }
        }
        else {
          produtoFinal = null
          produtoInicial = null
        }
      }
    }
    toolbar {
      button("Separar") {
        icon = VaadinIcon.SPLIT.create()
        addClickListener {
          viewModel.separar()
        }
      }
      button("Imprimir") {
        icon = VaadinIcon.PRINT.create()
        addClickListener {
          viewModel.imprimir()
        }
      }
    }
  }
  
  private fun updateFilter(edtCodigo: TextFieldFiltro,
                           edtDescricao: TextFieldFiltro,
                           edtGrade: TextFieldFiltro,
                           edtFornecedor: TextFieldFiltro,
                           edtLocalizacao: TextFieldFiltro) {
    val filter = edtCodigo.filtro
      .and(edtDescricao.filtro)
      .and(edtGrade.filtro)
      .and(edtFornecedor.filtro)
      .and(edtLocalizacao.filtro)
    dataProviderProdutos.filter = filter
  }
  
  private fun list(grade: Grid<ProdutoPedido>): List<ProdutoPedido> {
    val filter = dataProviderProdutos.filter
    val queryOrdem = comparator(grade)
    return dataProviderProdutos.items.toList()
      .filter {
        filter.test(it)
      }
      .sortedWith<ProdutoPedido>(queryOrdem)
  }
  
  private fun comparator(grade: Grid<ProdutoPedido>): Comparator<ProdutoPedido> {
    val queryOrdem = grade.sortOrder.mapNotNull {gridSort ->
      val prop = ProdutoPedido::class.members.toList()
        .filterIsInstance<KProperty1<ProdutoPedido, Comparable<*>>>()
        .firstOrNull {prop ->
          prop.name == gridSort.sorted.key
        }
      if(gridSort.direction == DESCENDING)
        compareByDescending<ProdutoPedido> {
          prop?.get(it)
        }
      else
        compareBy<ProdutoPedido> {
          prop?.get(it)
        }
    }
      .reduce {acc, comparator ->
        acc.thenComparing(comparator)
      }
    return queryOrdem
  }
  
  override val pedido: Pedido?
    get() = Pedido.findTemp(cmbPedido.value.ordno)
  override val produtosSelecionados: List<ProdutoPedido>
    get() = emptyList()
}

class TextFieldFiltro(val property: KProperty1<ProdutoPedido, Any>): TextField() {
  val filtro
    get() = SerializablePredicate<ProdutoPedido> {produto ->
      property.get(produto)
        .toString()
        .startsWith(value, ignoreCase = true)
    }
  
  init {
    setSizeFull()
    valueChangeMode = ValueChangeMode.TIMEOUT
    placeholder = "Filtro"
  }
}


