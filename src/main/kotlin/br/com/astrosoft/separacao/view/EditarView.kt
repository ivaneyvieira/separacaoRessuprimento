package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.viewmodel.EditarViewModel
import br.com.astrosoft.separacao.viewmodel.IEditarView
import com.github.mvysny.karibudsl.v10.addColumnFor
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.comboBox
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.grid
import com.github.mvysny.karibudsl.v10.integerField
import com.github.mvysny.karibudsl.v10.isExpand
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.HeaderRow
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.text.DecimalFormat

@Route(layout = SeparacaoLayout::class)
@PageTitle("Editar")
class EditarView: ViewLayout<EditarViewModel>(), IEditarView {
  override val viewModel: EditarViewModel = EditarViewModel(this)
  
  init{
    form("Editar pedidos") {
      isExpand = false
      cmbPedido = comboBox<Pedido>("Pedido origem") {
        colspan = 1
        setItems(Pedido.pedidosTemporarios)
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
      proximoNumero = integerField("Próximo número") {
        isEnabled = false
      }
    }
    gridProduto = grid(dataProvider = dataProviderProdutos) {
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
      val binder = Binder<ProdutoPedido>(ProdutoPedido::class.java)
      binder.withValidator {value, context ->
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
          .addEventListener("keydown") {event -> this@grid.editor.cancel()}
          .filter = "event.key === 'Tab' && event.shiftKey"
      }
    
      binder.bind(edtQtty, ProdutoPedido::qttyEdit.name)
    
      addItemClickListener {event ->
        editor.editItem(event.item)
        edtQtty.focus()
      }
    
      binder.addValueChangeListener {e ->
        editor.refresh()
      }
    
      editor.addCloseListener {
        binder.writeBean(it.item)
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
    
      shiftSelect()
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
  
  override val pedido: Pedido?
    get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
  
  override fun updateGrid() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}