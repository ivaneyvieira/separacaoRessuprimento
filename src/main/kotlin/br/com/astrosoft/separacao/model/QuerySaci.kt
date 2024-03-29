package br.com.astrosoft.separacao.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.framework.util.toSaciDate
import br.com.astrosoft.separacao.model.beans.*
import br.com.astrosoft.separacao.model.enum.ETipoOrigem
import java.time.LocalDate

class QuerySaci: QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql) {q ->
      q.addParameter("login", login).executeAndFetch(UserSaci::class.java).firstOrNull()?.initVars()
    }
  }
  
  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql) {q ->
      q.addParameter("login", "TODOS").executeAndFetch(UserSaci::class.java).map {user ->
        user.initVars()
      }
    }
  }
  
  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {q ->
      q.addParameter("login", user.login)
      q.addParameter("bitAcesso", user.bitAcesso())
      q.addParameter("abreviacoes", user.abreviacoes)
      q.addParameter("storeno", user.storeno)
      q.executeUpdate()
    }
  }
  
  fun verificaPedido(ordno: Int): Boolean {
    val storeno = 1
    val sql = "/sqlSaci/verificaPedido.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("ordno", ordno)
      q.executeAndFetch(Pedido::class.java).isNotEmpty()
    }
  }
  
  fun proximoNumeroDuplicado(storeno: Int, ordno: Int, destino: Int): Int {
    return when {
      storeno == 4 && ordno == 2 -> proximoNumeroDuplicadoLoja(destino)
      storeno == 5 && ordno == 2 -> 54
      else                       -> {
        val sql = "/sqlSaci/proximoNumero.sql"
        val proximoNumero: Int = query(sql) {q ->
          q.addParameter("storeno", storeno)
          q.addParameter("destino", destino)
          q.executeScalarList(Int::class.java)
        }.firstOrNull() ?: 0
        if(proximoNumero == 0) destino * 10000 + 1
        else proximoNumero
      }
    }
  }
  
  fun proximoNumeroDuplicadoLoja(destino: Int): Int {
    val storeno = 1
    val sql = "/sqlSaci/proximoNumeroLoja4.sql"
    val proximoNumero: Int = query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("destino", destino)
      q.executeScalarList(Int::class.java)
    }.firstOrNull() ?: 0
    val numeroInicial = destino * 10000 + 50
    return if(proximoNumero < numeroInicial) numeroInicial
    else proximoNumero
  }
  
  fun proximoNumeroSeparado(destino: Int): Int {
    val sql = "/sqlSaci/proximoNumeroSeparado.sql"
    val proximoNumero = query(sql) {q ->
      q.addParameter("destino", destino)
      q.executeScalarList(Int::class.java)
    }.firstOrNull() ?: 0
    return if(proximoNumero == 0) destino * 100000000 + 1
    else proximoNumero
  }
  
  fun proximoNumeroPedidoLoja(destino: Int, data: LocalDate?): Int {
    val storeno = 1
    val sql = "/sqlSaci/proximoNumeroLoja.sql"
    val proximoNumero = query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("destino", destino)
      q.addParameter("data", data?.toSaciDate() ?: 0)
      q.executeScalarList(Int::class.java)
    }.firstOrNull() ?: 0
    return if(proximoNumero == 0) proximoNumeroSeparado(destino) else proximoNumero
  }
  
  fun insertNewNumber(no: Int) {
    val sql = "/sqlSaci/insertNewNumber.sql"
    script(sql) {q ->
      q.addParameter("no", no)
      q.executeUpdate()
    }
  }
  
  fun listaProduto(storeno: Int, ordno: Int): List<ProdutoPedido> {
    val sql = "/sqlSaci/listaProdutos.sql"
    val lista = query2(sql, ProdutoPedido::class) {
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("ordno", ordno)
    }
    lista.forEach {produto ->
      produto.qttyEdit = produto.qtty.toInt()
    }
    return lista
  }

  fun listaProdutoPendente(storeno: Int, ordno: Int, data: LocalDate?): List<ProdutoPedido> {
    val sql = "/sqlSaci/listaProdutosPendente.sql"
    val lista = query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("ordno", ordno)
      q.addParameter("data", data?.toSaciDate() ?: 0)
      q.executeAndFetch(ProdutoPedido::class.java)
    }
    lista.forEach {produto ->
      produto.qttyEdit = produto.qtty.toInt()
    }
    return lista
  }
  fun listaPedido(): List<Pedido> {
    val storeno = 1
    val sql = "/sqlSaci/listaPedidos.sql"
    val ret = query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.executeAndFetch(Pedido::class.java)
    }
    return ret
  }

  fun listaPedidoPendente(): List<Pedido> {
    val storeno = 1
    val sql = "/sqlSaci/listaPedidosPendente.sql"
    val ret = query2(sql, Pedido::class) {
      addOptionalParameter("storeno", storeno)
    }
    return ret
  }

  fun listaPedidoTodos(): List<Pedido> {
    val storeno = 1
    val sql = "/sqlSaci/listaPedidosTodos.sql"
    
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.executeAndFetch(Pedido::class.java)
    }
  }
  
  fun duplicar(pedido: Pedido, pedidoNovo: Pedido) {
    val sql = "/sqlSaci/duplicaPedido.sql"
    script(sql) {q ->
      q.addOptionalParameter("storeno", pedido.storeno)
      q.addOptionalParameter("storenoNovo", pedidoNovo.storeno)
      q.addOptionalParameter("ordno", pedido.ordno)
      q.addOptionalParameter("ordnoNovo", pedidoNovo.ordno)
      q.executeUpdate()
    }
  }
  
  fun removePedido(numeroI: Int, numeroF: Int) {
    val storeno = 1
    val sql = "/sqlSaci/removerPedido.sql"
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("numeroI", numeroI)
      q.addOptionalParameter("numeroF", numeroF)
      q.executeUpdate()
    }
  }
  
  fun atualizarQuantidade(ordno: Int, ordnoNovo: Int, codigo: String, grade: String, localizacao: String, qtty: Int,
                          tipo: ETipoOrigem) {
    val storeno = 1
    val sql = "/sqlSaci/atualizarQuantidade.sql"
    val prdno = codigo.lpad(16, " ")
    val charTipo = tipo.sigla
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordnoNovo", ordnoNovo)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("prdno", prdno)
      q.addOptionalParameter("grade", grade)
      q.addOptionalParameter("localizacao", localizacao)
      q.addOptionalParameter("tipo", charTipo)
      q.addOptionalParameter("qtty", qtty)
      q.executeUpdate()
    }
    insertNewNumber(ordnoNovo)
  }
  
  fun retornaSaldo(ordnoMae: Int, ordno: Int, codigo: String, grade: String, localizacao: String, qttyEdit: Int) {
    val storeno = 1
    val sql = "/sqlSaci/retornaSaldo.sql"
    val prdno = codigo.lpad(16, " ")
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordnoMae", ordnoMae)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("prdno", prdno)
      q.addOptionalParameter("grade", grade)
      q.addOptionalParameter("localizacao", localizacao)
      q.addOptionalParameter("qttyEdit", qttyEdit)
      q.executeUpdate()
    }
  }
  
  fun findProduto(prdno: String): List<Produto> {
    val sql = "/sqlSaci/findProduto.sql"
    return query(sql) {q ->
      q.addOptionalParameter("prdno", prdno)
      q.executeAndFetch(Produto::class.java)
    }
  }
  
  fun adicionarProduto(pedido: Pedido, codigo: String, grade: String, qtty: Int, localizacao: String) {
    val sql = "/sqlSaci/adicionarProduto.sql"
    val storeno = 1
    val ordno = pedido.ordno
    val prdno = codigo.lpad(16, " ")
    return script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("prdno", prdno)
      q.addOptionalParameter("grade", grade)
      q.addOptionalParameter("localizacao", localizacao)
      q.addOptionalParameter("qtty", qtty)
      q.executeUpdate()
    }
  }
  
  fun listaRelatorio(ordno: Int): List<Relatorio> {
    val sql = "/sqlSaci/relatorio.sql"
    val storeno = 1
    return query(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordno", ordno)
      q.executeAndFetch(Relatorio::class.java)
    }
  }
  
  fun findAbreviacoes(): List<String> {
    val sql = "/sqlSaci/findAbreviacoes.sql"
    return query(sql) {q ->
      q.executeScalarList(String::class.java)
    }
  }
  
  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()