package br.com.astrosoft.separacao.viewmodel

import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.util.rpad
import br.com.astrosoft.separacao.viewmodel.EAling.LEFT
import java.text.DecimalFormat

class PrintText<T> {
  private val columns = mutableListOf<Column<T, *>>()
  
  fun columText(header: String, size: Int, process: (T) -> String): PrintText<T> {
    val column = Column(header, size, LEFT, process) {str ->
      str.lpad(size, " ")
    }
    columns.add(column)
    return this
  }
  
  fun columNumber(header: String, size: Int, process: (T) -> Double, format: String = "0,000.##"): PrintText<T> {
    val decimalFormat = DecimalFormat(format)
    val column = Column(header, size, LEFT, process) {int ->
      decimalFormat.format(int)
        .rpad(size, " ")
    }
    columns.add(column)
    return this
  }
  
  fun header() = montaLinha {col ->
    col.columnText
  }
  
  fun detail(value: T) = montaLinha {col ->
    col.dataText(value)
  }
  
  private fun montaLinha(process: (Column<T, *>) -> String): String {
    return columns.joinToString(separator = " ") {col ->
      process(col)
    }
  }
}

data class Column<T, V>(val header: String, val size: Int, val align: EAling,
                        val process: (T) -> V, val posProcess: (V) -> String) {
  val columnText
    get() = header.lpad(size, " ")
  
  fun dataText(value: T) = posProcess(process(value))
}

enum class EAling {
  RIGHT,
  LEFT,
  CENTER
}