package br.com.consutec.factory

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConnectionFactory {
  private var connection: Connection? = null
  @Throws(SQLException::class, ClassNotFoundException::class, Exception::class)
  fun obterConexao(): Connection? {
    var host = ""
    var db = ""
    var user = ""
    var pass = ""
    try {
      val local = File("./bdconfig.txt").canonicalFile.toString()
      val arq = File(local)
      val existe = arq.exists()
      if(existe) {
        val fr = FileReader(arq)
        val br = BufferedReader(fr)
        while(br.ready()) {
          val linha = br.readLine()
          if(linha.contains("Host:")) {
            host =
              linha.replace("Host:", "")
                .replace(" ", "")
          }
          if(linha.contains("db:")) {
            db =
              linha.replace("db:", "")
                .replace(" ", "")
          }
          if(linha.contains("user:")) {
            user =
              linha.replace("user:", "")
                .replace(" ", "")
          }
          if(linha.contains("pass:")) {
            pass =
              linha.replace("pass:", "")
                .replace(" ", "")
          }
        }
      }
    } catch(e: Exception) {
      val st = e.stackTrace
      var erro = ""
      var i = 0
      while(i < st.size) {
        erro = erro + st[i].toString() + "\n"
        i++
      }
    }
    if(connection == null || connection!!.isClosed) {
      Class.forName("com.mysql.jdbc.Driver")
      val serverName = host
      val banco = db
      val url = "jdbc:mysql://$serverName/$banco"
      try {
        connection = DriverManager.getConnection(url, user, pass)
        if(connection != null) {
          return connection
        }
      } catch(ex: Exception) {
        ex.printStackTrace()
        throw Exception("Não foi possível conectar ao banco")
      }
      return connection
    }
    return connection
  }

  companion object {
    private var connectionFactory: ConnectionFactory? = null
    val instance: ConnectionFactory?
      get() {
        if(connectionFactory == null) {
          connectionFactory = ConnectionFactory()
        }
        return connectionFactory
      }
  }
}