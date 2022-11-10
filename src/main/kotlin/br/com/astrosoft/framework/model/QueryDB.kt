package br.com.astrosoft.framework.model

import br.com.astrosoft.framework.util.SystemUtils.readFile
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.sql2o.Connection
import org.sql2o.Query
import org.sql2o.Sql2o
import org.sql2o.converters.ByteArrayConverter
import org.sql2o.converters.Converter
import org.sql2o.quirks.NoQuirks
import java.time.LocalDate
import java.time.LocalTime
import kotlin.reflect.KClass

typealias QueryHandle = Query.() -> Unit

open class QueryDB(driver: String, url: String, username: String, password: String) {
  private val sql2o: Sql2o
  
  init {
    registerDriver(driver)
    val config = HikariConfig()
    config.jdbcUrl = url
    config.username = username
    config.password = password
    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    config.isAutoCommit = false
    val ds = HikariDataSource(config)
    ds.maximumPoolSize = 5

    val maps = HashMap<Class<*>, Converter<*>>()
    maps[LocalDate::class.java] = LocalDateConverter()
    maps[LocalTime::class.java] = LocalSqlTimeConverter()
    maps[ByteArray::class.java] = ByteArrayConverter()
    
    this.sql2o = Sql2o(ds, NoQuirks(maps))
  }
  
  private fun registerDriver(driver: String) {
    try {
      Class.forName(driver)
    } catch(e: ClassNotFoundException) {
      throw RuntimeException(e)
    }
  }
  
  private fun <T> buildQuery(file: String, proc: (Connection, Query) -> T): T {
    val sql = if(file.startsWith("/")) readFile(file) else file
    return this.sql2o.open()
      .use {con ->
        val query = con.createQuery(sql)
        val time = System.currentTimeMillis()
        println("SQL2O ==> $sql")
        val result = proc(con, query)
        val difTime = System.currentTimeMillis() - time
        println("######################## TEMPO QUERY $difTime ms ########################")
        result
      }
  }
  
  protected fun <T> query(file: String, lambda: (Query) -> T): T {
    return buildQuery(file) {con, query ->
      val ret = lambda(query)
      con.close()
      ret
    }
  }

  fun toStratments(file: String, complemento: String? = null      ): List<String> {
    val sql = if (file.startsWith("/")) readFile(file) ?: ""
    else file
    val sqlComplemento = if (complemento == null) sql else "$sql\n$complemento"
    return sqlComplemento.split(";").filter { it.isNotBlank() || it.isNotEmpty() }
  }

  protected fun <T : Any> query2(file: String,
                                classes: KClass<T>,
                                lambda: QueryHandle = {}): List<T> {
    val statements = toStratments(file)
    if (statements.isEmpty()) return emptyList()
    val lastIndex = statements.lastIndex
    val query = statements[lastIndex]
    val updates = if (statements.size > 1) statements.subList(0, lastIndex) else emptyList()
    return transaction { con ->
      scriptSQL(con, updates, lambda)
      val ret: List<T> = querySQL(con, query, classes,  fetch = { query, classes ->
        query.executeAndFetch(classes.java)
      }, lambda)
      ret
    }
  }

  private fun <T : Any, R> querySQL(con: Connection,
                                    sql: String?,
                                    classes: KClass<T>,
                                    fetch: (Query, KClass<T>) -> R,
                                    lambda: QueryHandle = {}): R {
    val query = con.createQueryConfig(sql)
    query.lambda()
    println(sql)
    return fetch(query, classes)
  }

  private fun querySQLResult(con: Connection, sql: String?, lambda: QueryHandle = {}): Query {
    val query = con.createQueryConfig(sql)
    query.lambda()
    return query
  }

  protected fun <T> transaction(block: (Connection) -> T): T {
    return sql2o.beginTransaction().use { con ->
      val ret = block(con)
      con.commit()
      ret
    }
  }

  private fun Connection.createQueryConfig(sql: String?): Query {
    val query = createQuery(sql)
    query.isAutoDeriveColumnNames = true
    return query
  }

  private fun scriptSQL(con: Connection, stratments: List<String>,  lambda: QueryHandle = {}) {
    stratments.forEach { sql ->
      val query = con.createQueryConfig(sql)
      query.lambda()
      query.executeUpdate()
      println(sql)
    }
  }

  protected fun script(file: String, lambda: (Query) -> Unit) {
    val stratments =
      readFile(file)?.split(";")
        .orEmpty()
        .filter {it.isNotBlank() || it.isNotEmpty()}
    transaction {con ->
      stratments.forEach {sql ->
        val query = con.createQuery(sql)
        lambda(query)
      }
    }
  }
  
  fun Query.addOptionalParameter(name: String, value: String): Query {
    if(this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }
  
  fun Query.addOptionalParameter(name: String, value: Int): Query {
    if(this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }
  
  fun Query.addOptionalParameter(name: String, value: Double): Query {
    if(this.paramNameToIdxMap.containsKey(name)) this.addParameter(name, value)
    return this
  }

  protected fun <T> transaction2(block: (Connection) -> T): T {
    return sql2o.beginTransaction().use { con ->
      val ret = block(con)
      con.commit()
      ret
    }
  }
}
