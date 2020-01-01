package br.com.astrosoft.separacao.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.lpad
import br.com.astrosoft.separacao.model.beans.Pedido
import br.com.astrosoft.separacao.model.beans.ProdutoPedido
import br.com.astrosoft.separacao.model.beans.UserSaci

class QuerySaci: QueryDB(driver, url, username, password) {
  fun findUser(login: String): UserSaci? {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql) {q ->
      q.addParameter("login", login)
        .executeAndFetch(UserSaci::class.java)
        .firstOrNull()
    }
  }
  
  fun verificaPedido(storeno: Int, ordno: Int): Boolean {
    val sql = "/sqlSaci/verificaPedido.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("ordno", ordno)
        .executeAndFetch(Pedido::class.java)
        .isNotEmpty()
    }
  }
  
  fun proximoNumero(storeno: Int, destino: Int): Int {
    val sql = "/sqlSaci/proximoNumero.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("destino", destino)
        .executeScalar(Int::class.java)
    }
  }
  
  fun listaProduto(storeno: Int, ordno: Int): List<ProdutoPedido> {
    val sql = "/sqlSaci/listaProdutos.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("ordno", ordno)
        .executeAndFetch(ProdutoPedido::class.java)
    }
  }
  
  fun listaPedido(storeno: Int): List<Pedido> {
    val sql = "/sqlSaci/listaPedidos.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
        .executeAndFetch(Pedido::class.java)
    }
  }
  
  fun duplicar(storeno: Int, ordno: Int, ordnoNovo: Int) {
    val sql = "/sqlSaci/duplicaPedido.sql"
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("ordnoNovo", ordnoNovo)
        .executeUpdate()
    }
  }
  
  fun removePedido(storeno: Int, numeroI: Int, numeroF: Int) {
    val sql = "/sqlSaci/removerPedido.sql"
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("numeroI", numeroI)
      q.addOptionalParameter("numeroF", numeroF)
        .executeUpdate()
    }
  }
  
  fun atualizarQuantidade(storeno: Int, ordno: Int, codigo: String, grade: String, quantidade: Double) {
    val sql = "/sqlSaci/atualizarQuantidade.sql"
    val prdno = codigo.lpad(16, " ")
    val qtty: Int = (quantidade * 1000).toInt()
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("prdno", prdno)
      q.addOptionalParameter("grade", grade)
      q.addOptionalParameter("qtty", qtty)
        .executeUpdate()
    }
  }
  
  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    internal val test = db.test
    val ipServer =
      url.split("/")
        .getOrNull(2)
  }
}

val saci = QuerySaci()