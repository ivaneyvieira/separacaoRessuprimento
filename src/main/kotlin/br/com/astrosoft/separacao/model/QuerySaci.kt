package br.com.astrosoft.separacao.model

import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
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