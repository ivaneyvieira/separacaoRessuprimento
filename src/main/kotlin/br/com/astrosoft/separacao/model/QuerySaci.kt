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
  
  fun verificaPedido(ordno: Int): Boolean {
    val storeno = 1
    val sql = "/sqlSaci/verificaPedido.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("ordno", ordno)
        .executeAndFetch(Pedido::class.java)
        .isNotEmpty()
    }
  }
  
  fun proximoNumero(destino: Int): Int {
    val storeno = 1
    val sql = "/sqlSaci/proximoNumero.sql"
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("destino", destino)
        .executeScalar(Int::class.java)
    }
  }
  
  fun listaProduto(ordno: Int): List<ProdutoPedido> {
    val storeno = 1
    val sql = "/sqlSaci/listaProdutos.sql"
    val lista = query(sql) {q ->
      q.addParameter("storeno", storeno)
      q.addParameter("ordno", ordno)
        .executeAndFetch(ProdutoPedido::class.java)
    }
    lista.forEach {produto ->
      produto.qttyEdit = produto.qtty.toInt()
    }
    return lista
  }
  
  fun listaPedido(): List<Pedido> {
    val storeno = 1
    val sql = "/sqlSaci/listaPedidos.sql"
  
    return query(sql) {q ->
      q.addParameter("storeno", storeno)
        .executeAndFetch(Pedido::class.java)
    }
  }
  
  fun duplicar(ordno: Int, ordnoNovo: Int) {
    val storeno = 1
    val sql = "/sqlSaci/duplicaPedido.sql"
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("ordnoNovo", ordnoNovo)
        .executeUpdate()
    }
  }
  
  fun removePedido(numeroI: Int, numeroF: Int) {
    val storeno = 1
    val sql = "/sqlSaci/removerPedido.sql"
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("numeroI", numeroI)
      q.addOptionalParameter("numeroF", numeroF)
        .executeUpdate()
    }
  }
  
  fun atualizarQuantidade(ordno: Int, ordnoNovo: Int, codigo: String, grade: String,
                          localizacao: String, quantidade: Double) {
    val storeno = 1
    val sql = "/sqlSaci/atualizarQuantidade.sql"
    val prdno = codigo.lpad(16, " ")
    val qtty: Int = (quantidade * 1).toInt()
    script(sql) {q ->
      q.addOptionalParameter("storeno", storeno)
      q.addOptionalParameter("ordnoNovo", ordnoNovo)
      q.addOptionalParameter("ordno", ordno)
      q.addOptionalParameter("prdno", prdno)
      q.addOptionalParameter("grade", grade)
      q.addOptionalParameter("localizacao", localizacao)
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