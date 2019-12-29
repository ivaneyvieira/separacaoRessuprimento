package br.com.consutec.modelo

import java.awt.Component
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.JViewport
import javax.swing.UIManager
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableColumn

class RowNumberTable(private val main: JTable): JTable(), ChangeListener, PropertyChangeListener, TableModelListener {
  override fun addNotify() {
    super.addNotify()
    val c: Component = parent
    if(c is JViewport) {
      c.addChangeListener(this)
    }
  }

  override fun getRowCount(): Int {
    return main.rowCount
  }

  override fun getRowHeight(row: Int): Int {
    val rowHeight = main.getRowHeight(row)
    if(rowHeight != super.getRowHeight(row)) {
      setRowHeight(row, rowHeight)
    }
    return rowHeight
  }

  override fun getValueAt(row: Int, column: Int): Any {
    return Integer.toString(row + 1)
  }

  override fun isCellEditable(row: Int, column: Int): Boolean {
    return false
  }

  override fun setValueAt(value: Any, row: Int, column: Int) {}
  override fun stateChanged(e: ChangeEvent) {
    val viewport = e.source as JViewport
    val scrollPane = viewport.parent as JScrollPane
    scrollPane.verticalScrollBar.value = viewport.viewPosition.y
  }

  override fun propertyChange(e: PropertyChangeEvent) {
    if("selectionModel" == e.propertyName) {
      setSelectionModel(main.selectionModel)
    }
    if("rowHeight" == e.propertyName) {
      repaint()
    }
    if("model" == e.propertyName) {
      main.model.addTableModelListener(this)
      revalidate()
    }
  }

  override fun tableChanged(e: TableModelEvent) {
    revalidate()
  }

  private class RowNumberRenderer: DefaultTableCellRenderer() {
    override fun getTableCellRendererComponent(table: JTable,
                                               value: Any,
                                               isSelected: Boolean,
                                               hasFocus: Boolean,
                                               row: Int,
                                               column: Int): Component {
      if(table != null) {
        val header = table.tableHeader
        if(header != null) {
          foreground = header.foreground
          background = header.background
          font = header.font
        }
      }
      if(isSelected) {
        font = font.deriveFont(1)
      }
      text = value?.toString() ?: ""
      border = UIManager.getBorder("TableHeader.cellBorder")
      return this
    }

    init {
      horizontalAlignment = 0
    }
  }

  init {
    main.addPropertyChangeListener(this)
    main.model.addTableModelListener(this)
    isFocusable = false
    setAutoCreateColumnsFromModel(false)
    setSelectionModel(main.selectionModel)
    val column = TableColumn()
    column.headerValue = " "
    addColumn(column)
    column.cellRenderer = RowNumberRenderer()
    getColumnModel().getColumn(0)
      .preferredWidth = 50
    preferredScrollableViewportSize = preferredSize
  }
}