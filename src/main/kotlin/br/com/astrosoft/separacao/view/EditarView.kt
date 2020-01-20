package br.com.astrosoft.separacao.view

import br.com.astrosoft.framework.view.ViewLayout
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.UserSaci
import br.com.astrosoft.separacao.viewmodel.EditarViewModel
import br.com.astrosoft.separacao.viewmodel.IEditarView
import br.com.astrosoft.separacao.viewmodel.ProdutoDlg
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
import com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.ColumnTextAlign
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.textfield.TextFieldVariant.LUMO_ALIGN_RIGHT
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection.ASCENDING
import com.vaadin.flow.data.renderer.NumberRenderer
import com.vaadin.flow.data.value.ValueChangeMode.EAGER
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import java.text.DecimalFormat

@Route(layout = SeparacaoLayout::class)
@PageTitle("Editar")
@HtmlImport("frontend://styles/shared-styles.html")
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
      addThemeVariants(LUMO_COMPACT)
      isColumnReorderingAllowed = false
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
      editor.isBuffered = false
      val edtQtty = IntegerField().apply {
        this.isAutoselect = true
        this.width = "100%"
        this.isAutofocus = true
        this.element
          .addEventListener("keydown") {
            val value = this@grid.editor.item
            if(value.qtty.toInt() != value.qttyEdit)
              this@grid.selectionModel.select(value)
            else
              this@grid.selectionModel.deselect(value)
            this@grid.editor.save()
            this@grid.editor.closeEditor()
          }
          .filter = "event.key === 'Enter'"
        this.element
          .addEventListener("keydown") {
            this@grid.editor.cancel()
            this@grid.editor.closeEditor()
          }
          .filter = "event.key === 'Escape'"
      }
      binder.bind(edtQtty, ProdutoPedido::qttyEdit.name)
      
      addItemClickListener {event ->
        if(event.clickCount == 1) {
          editor.editItem(event.item)
          edtQtty.focus()
        }
      }
      
      editor.addSaveListener {
        val value = it.item
        if(value.qtty.toInt() != value.qttyEdit)
          this.selectedItems.add(value)
        else
          this.selectedItems.remove(value)
      }
      
      binder.addValueChangeListener {e ->
        val value = e.value as? ProdutoPedido
        if(value != null) {
          if(value.qtty.toInt() != value.qttyEdit)
            this.select(value)
          else
            this.selectedItems.remove(value)
        }
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
      button("Processar") {
        icon = VaadinIcon.SPLIT.create()
        addThemeVariants(LUMO_PRIMARY)
        addClickListener {
          viewModel.processar()
        }
      }
      button("Novo produto") {
        icon = VaadinIcon.INSERT.create()
        addClickListener {
          viewModel.novoProduto()
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
  
  override fun novoProduto(pedido: Pedido) {
    ProdutoDialog(viewModel, pedido).apply {
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

class ProdutoDialog(private val viewModel: EditarViewModel, val pedido: Pedido): Dialog() {
  private var produto: ProdutoDlg = ProdutoDlg()
  private lateinit var edtQtty: IntegerField
  private lateinit var edtDescricao: TextField
  private lateinit var edtGrade: ComboBox<String>
  private lateinit var edtLocalizacao: ComboBox<String>
  private lateinit var edtCodigo: TextField
  
  init {
    em("Adiciona Produto")
    formLayout {
      responsiveSteps {
        "0px"(4, top)
      }
      edtCodigo = textField("Código") {
        colspan = 1
        value = produto.codigo
        valueChangeMode = EAGER
        
        addValueChangeListener {event ->
          if(event.isFromClient) {
            val value = event.value
            val produtos = viewModel.findProduto(value)
            edtDescricao.value = produtos.firstOrNull()?.descricao ?: "Não encontrado"
            val grades =
              produtos.map {it.grade}
                .distinct()
                .sorted()
            edtGrade.setItems(grades)
            edtGrade.value = grades.firstOrNull()
            
            val localizacoes = produtos.map {it.localizacao}
                .distinct()
                .sorted()
            edtLocalizacao.setItems(localizacoes)
            edtLocalizacao.value = localizacoes.firstOrNull()
          }
        }
      }
      edtDescricao = textField("Descrição") {
        colspan = 3
        value = ""
        isReadOnly = true
      }
      edtGrade = comboBox("Grade") {
        colspan = 1
        isRequired = false
        isAllowCustomValue = false
      }
      edtQtty = integerField("Quantidade") {
        colspan = 1
        isAutoselect =true
        addThemeVariants(LUMO_ALIGN_RIGHT)
        value = produto.qtty
      }
      edtLocalizacao = comboBox("Localização") {
        colspan = 2
        isRequired = false
        isAllowCustomValue = false
      }
    }
    horizontalLayout {
      button("Salva") {
        addThemeVariants(LUMO_PRIMARY)
        addClickListener {
          val produto = ProdutoDlg().apply {
            codigo = edtCodigo.value ?: ""
            grade = edtGrade.value ?: ""
            qtty = edtQtty.value ?: 0
            produtos = viewModel.findProduto(codigo)
            localizacao = edtLocalizacao.value ?: ""
          }
          viewModel.salvaProduto(produto)
          this@ProdutoDialog.close()
        }
      }
      button("Cancela") {
        addClickListener {
          this@ProdutoDialog.close()
        }
      }
    }
  }
}