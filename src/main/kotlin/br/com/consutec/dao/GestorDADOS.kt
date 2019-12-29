package br.com.consutec.dao

import br.com.consutec.factory.ConnectionFactory
import br.com.consutec.modelo.Base
import br.com.consutec.modelo.Oprd
import br.com.consutec.modelo.Produto
import java.math.BigDecimal
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import java.util.logging.*

class GestorDADOS {
  private var conexao: Connection? = null
  @get:Throws(ClassNotFoundException::class, SQLException::class, Exception::class)
  val connection: Connection?
    get() {
      if(conexao == null || conexao!!.isClosed) {
        conexao = ConnectionFactory.instance?.obterConexao()
      }
      return conexao
    }
  
  @Throws(SQLException::class, Exception::class)
  fun removerPedido(loja: Int, pedido: Int) {
    val sql1 = "DELETE FROM ords WHERE storeno = ? AND no = ? "
    val sql2 = "DELETE FROM orddlv WHERE storeno = ? AND ordno = ? "
    val sql3 = "DELETE FROM oprd WHERE storeno = ? AND ordno = ? "
    val sql4 = "DELETE FROM oprdxf WHERE storeno = ? AND ordno = ? "
    val cx = connection
    cx!!.autoCommit = false
    val stmt1 = cx.prepareStatement(sql1)
    stmt1.setInt(1, loja)
    stmt1.setInt(2, pedido)
    stmt1.executeUpdate()
    val stmt2 = cx.prepareStatement(sql2)
    stmt2.setInt(1, loja)
    stmt2.setInt(2, pedido)
    stmt2.executeUpdate()
    val stmt3 = cx.prepareStatement(sql3)
    stmt3.setInt(1, loja)
    stmt3.setInt(2, pedido)
    stmt3.executeUpdate()
    val stmt4 = cx.prepareStatement(sql4)
    stmt4.setInt(1, loja)
    stmt4.setInt(2, pedido)
    stmt4.executeUpdate()
    stmt1.close()
    stmt2.close()
    stmt3.close()
    stmt4.close()
    cx.commit()
    cx.close()
  }
  
  @Throws(SQLException::class, Exception::class)
  fun listar(base: Base): List<Produto> {
    val lista: MutableList<Produto> = ArrayList()
    var sql1 =
      "SELECT * FROM (SELECT oprd.prdno         , oprd.grade         , prd.name AS descricao         , prd.mfno AS fornecedor         , LPAD(prd.clno,6,'0') AS centrodelucro        , MID(prdloc.localizacao,1,4) localizacao        , prd.typeno AS tipo        , oprd.qtty  FROM oprd  INNER JOIN prd ON (oprd.prdno = prd.no)  LEFT  JOIN prdloc ON (oprd.prdno = prdloc.prdno)  WHERE oprd.storeno = ?  AND oprd.ordno = ? "
    val sql2 =
      "SELECT * FROM (SELECT oprd.prdno         , oprd.grade         , prd.name AS descricao         , prd.mfno AS fornecedor         , LPAD(prd.clno,6,'0') AS centrodelucro        , MID(prdloc.localizacao,1,4) AS localizacao        , prd.typeno AS tipo        , oprd.qtty  FROM oprd  INNER JOIN prd ON (oprd.prdno = prd.no)  LEFT  JOIN prdloc ON (oprd.prdno = prdloc.prdno)  WHERE oprd.storeno = ?  AND oprd.ordno = ?  GROUP BY 1,2 ) x ORDER BY x.localizacao, x.prdno, x.grade "
    var filtro1 = false
    var filtro2 = false
    var filtro3 = false
    var filtro4 = false
    var filtro5 = false
    var filtro6 = false
    var filtro7 = false
    if(base.descIni == "") {
      filtro1 = false
    }
    else {
      filtro1 = true
      sql1 = "$sql1 AND (MID(prd.name,1,3) BETWEEN ? AND ? ) "
    }
    when(base.tipos) {
      ""   -> {
        filtro2 = false
      }
      else -> {
        filtro2 = true
        sql1 = sql1 + " AND (prd.typeno IN ( " + base.tipos + " )) "
      }
    }
    when(base.fornecedores) {
      ""   -> {
        filtro3 = false
      }
      else -> {
        filtro3 = true
        sql1 = sql1 + " AND (prd.mfno IN ( " + base.fornecedores + " )) "
      }
    }
    if(base.areas == "") {
      filtro4 = false
    }
    else {
      filtro4 = true
      sql1 = "$sql1 AND (prdloc.storeno = ? ) "
      sql1 = "$sql1 AND (prdloc.localizacao LIKE ? ) "
    }
    when(base.centrodeLucro) {
      ""   -> {
        filtro5 = false
      }
      else -> {
        filtro5 = true
        sql1 = when {
          base.centrodeLucro!!.endsWith("0000") -> {
            "$sql1 AND (prd.groupno = ? ) "
          }
          base.centrodeLucro!!.endsWith("00")   -> {
            "$sql1 AND (prd.deptno = ? ) "
          }
          else                                  -> {
            "$sql1 AND (prd.clno = ? ) "
          }
        }
      }
    }
    when(base.sinalQtd) {
      "todos" -> {
        filtro6 = false
      }
      else    -> {
        filtro6 = true
        sql1 = if(base.sinalQtd == ">") {
          "$sql1 AND (oprd.qtty > ? ) "
        }
        else if(base.sinalQtd == "<") {
          "$sql1 AND (oprd.qtty < ? ) "
        }
        else if(base.sinalQtd == "=") {
          "$sql1 AND (oprd.qtty = ? ) "
        }
        else {
          "$sql1 AND (oprd.qtty BETWEEN ? AND ? ) "
        }
      }
    }
    when(base.localizacao) {
      ""   -> {
        filtro7 = false
      }
      else -> {
        filtro7 = true
        sql1 += "AND ( prdloc.localizacao LIKE ? ) "
      }
    }
    sql1 = "$sql1 GROUP BY 1,2 ) x ORDER BY x.localizacao, x.prdno, x.grade "
    var sql = ""
    sql = if(base.usaFiltrosProduto!!) {
      sql1
    }
    else {
      sql2
    }
    val stmt = connection!!.prepareStatement(sql)
    stmt.setInt(1, base.lojaOrigem!!.toInt())
    stmt.setInt(2, base.pedidoOrigem!!.toInt())
    var index = 2
    if(filtro1) {
      index++
      stmt.setString(index, base.descIni)
      index++
      stmt.setString(index, base.descFim)
    }

    if(filtro4) {
      index++
      stmt.setInt(index, base.lojaArea!!.toInt())
      index++
      stmt.setString(index, "%" + base.areas + "%")
    }
    if(filtro5) {
      index++
      stmt.setInt(index, base.centrodeLucro!!.toInt())
    }
    if(filtro6) {
      if(base.sinalQtd == ">") {
        index++
        val qtd1 = base.qtd1
        stmt.setLong(index, qtd1!!.toLong())
      }
      else if(base.sinalQtd == "<") {
        index++
        val qtd1 = base!!.qtd1
        stmt.setLong(index, qtd1!!.toLong())
      }
      else if(base.sinalQtd == "=") {
        index++
        val qtd1 = base.qtd1
        stmt.setLong(index, qtd1!!.toLong())
      }
      else {
        index++
        val qtd1 = base.qtd1
        stmt.setLong(index, qtd1!!.toLong())
        index++
        val qtd2 = base.qtd2 ?: BigDecimal(0)
        stmt.setLong(index, qtd2.toLong())
      }
    }
    if(filtro7) {
      index++
      stmt.setString(index, base.localizacao + "%")
    }
    stmt.executeQuery()
    val rs = stmt.resultSet
    while(rs.next()) {
      if(rs.findColumn("prdno") > 0) {
        val prd = Produto()
        prd.selecao = java.lang.Boolean.valueOf(false)
        prd.prdno = rs.getString("prdno")
        prd.grade = rs.getString("grade")
        prd.descricao = rs.getString("descricao")
        prd.fornecedor = java.lang.Long.valueOf(rs.getLong("fornecedor"))
        prd.centrodelucro = rs.getString("centrodelucro")
        prd.tipo = java.lang.Long.valueOf(rs.getLong("tipo"))
        prd.qtd = rs.getBigDecimal("qtty")
        prd.localização = rs.getString("localizacao")
        lista.add(prd)
      }
    }
    stmt.close()
    return lista
  }
  
  @Throws(SQLException::class, Exception::class)
  fun executar(base: Base): Int {
    var novo = Integer.valueOf(0)
    val cx = connection
    cx!!.autoCommit = false
    val sql1 = "SELECT (MAX(no) + 1) AS proximo FROM ords WHERE storeno = ? "
    val stmt1 = cx.prepareStatement(sql1)
    stmt1.setInt(1, base.lojaDestino!!.toInt())
    stmt1.executeQuery()
    val rs1 = stmt1.resultSet
    if(rs1.next()) {
      novo = Integer.valueOf(rs1.getInt("proximo"))
    }
    if(base.informaPedidoDestino!!) {
      novo = base.pedidoDestino
    }
    val sql2 =
      "INSERT INTO ords (  no, date, vendno, discount, amt, package, custo_fin, others, eord_ordno, dataFaturamento ,invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig, l1, l2, l3 , l4, m1, m2, m3, m4, deliv, storeno, carrno, empno, prazo, eord_storeno, delivOriginal , bits, bits2, bits3, padbyte, indxno, repno, auxShort1, auxShort2, noofinst, status , s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd , auxChar, c1, c2, c3, c4 ) SELECT  ? AS no, date, vendno, discount,  0 AS amt, package, custo_fin, others, eord_ordno, dataFaturamento ,invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig, l1, l2, l3 , l4, m1, m2, m3, m4, deliv, ? AS storeno, carrno, empno, prazo, eord_storeno, delivOriginal , bits, bits2, bits3, padbyte, indxno, repno, auxShort1, auxShort2, noofinst, 0 , s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd , auxChar, c1, c2, c3, c4 FROM   ords WHERE  storeno = ? AND    no = ? "
    val stmt2 = cx.prepareStatement(sql2)
    stmt2.setInt(1, novo.toInt())
    stmt2.setInt(2, base.lojaDestino!!.toInt())
    stmt2.setInt(3, base.lojaOrigem!!.toInt())
    stmt2.setInt(4, base.pedidoOrigem!!.toInt())
    stmt2.executeUpdate()
    var sql3 =
      "INSERT INTO oprd      ( oprd.storeno, oprd.ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms, oprd.auxLong1      , oprd.auxLong2, oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4      , oprd.auxMy3, oprd.auxMy4, oprd.qtty, oprd.qtty_src, oprd.qtty_xfr, oprd.cost, oprd.qttyRcv      , oprd.qttyCancel, oprd.qttyVendaMes, oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente      , oprd.stkDisponivel, oprd.qttyAbc, oprd.seqno, oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1      , oprd.auxShort2, oprd.auxShort3, oprd.auxShort4, oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte      , oprd.gradeFechada, oprd.obs ) SELECT ? AS storeno, ? AS ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms, oprd.auxLong1, oprd.auxLong2      , oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4, oprd.auxMy3, oprd.auxMy4      , oprd.qtty, oprd.qtty_src, oprd.qtty_xfr, oprd.cost, 0 AS qttyRcv, 0 AS qttyCancel, oprd.qttyVendaMes      , oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente, oprd.stkDisponivel, oprd.qttyAbc      , oprd.seqno, oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1, oprd.auxShort2, oprd.auxShort3      , oprd.auxShort4, oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte, oprd.gradeFechada, oprd.obs FROM oprd INNER JOIN prd ON (prd.no = oprd.prdno) LEFT  JOIN prdloc ON (oprd.prdno = prdloc.prdno) WHERE  (oprd.storeno = ? ) AND    (oprd.ordno = ? ) "
    var filtro1 = false
    var filtro2 = false
    var filtro3 = false
    var filtro4 = false
    if(base.descIni == "") {
      filtro1 = false
    }
    else {
      filtro1 = true
      sql3 = "$sql3 AND (MID(prd.name,1,3) BETWEEN ? AND ? ) "
    }
    if(base.tipos == "") {
      filtro2 = false
    }
    else {
      filtro2 = true
      sql3 = sql3 + " AND (prd.typeno IN ( " + base.tipos + ")) "
    }
    if(base.fornecedores == "") {
      filtro3 = false
    }
    else {
      filtro3 = true
      sql3 = sql3 + " AND (prd.mfno IN ( " + base.fornecedores + " )) "
    }
    if(base.areas == "") {
      filtro4 = false
    }
    else {
      filtro4 = true
      sql3 = "$sql3 AND (prdloc.storeno = ? ) "
      sql3 = "$sql3 AND (prdloc.localizacao LIKE ? ) "
    }
    sql3 = "$sql3 GROUP BY oprd.prdno,oprd.grade "
    val stmt3 = cx.prepareStatement(sql3)
    stmt3.setInt(1, base.lojaDestino!!.toInt())
    stmt3.setInt(2, novo.toInt())
    stmt3.setInt(3, base.lojaOrigem!!.toInt())
    stmt3.setInt(4, base.pedidoOrigem!!.toInt())
    var index = 4
    if(filtro1) {
      index++
      stmt3.setString(index, base.descIni)
      index++
      stmt3.setString(index, base.descFim)
    }
    if(filtro2);
    if(filtro3);
    if(filtro4) {
      index++
      stmt3.setInt(index, base.lojaArea!!.toInt())
      index++
      stmt3.setString(index, "%" + base.areas + "%")
    }
    stmt3.executeUpdate()
    var sql4 =
      " UPDATE oprd INNER JOIN prd ON (oprd.prdno = prd.no)  LEFT  JOIN prdloc ON (oprd.prdno = prdloc.prdno)  SET oprd.qttyCancel = oprd.qtty  WHERE  (oprd.storeno = ? )  AND    (oprd.ordno = ? ) "
    filtro1 = false
    filtro2 = false
    filtro3 = false
    filtro4 = false
    if(base.descIni == "") {
      filtro1 = false
    }
    else {
      filtro1 = true
      sql4 = "$sql4 AND (MID(prd.name,1,3) BETWEEN ? AND ? ) "
    }
    if(base.tipos == "") {
      filtro2 = false
    }
    else {
      filtro2 = true
      sql4 = sql4 + " AND (prd.typeno IN ( " + base.tipos + " )) "
    }
    if(base.fornecedores == "") {
      filtro3 = false
    }
    else {
      filtro3 = true
      sql4 = sql4 + " AND (prd.mfno IN ( " + base.fornecedores + " )) "
    }
    if(base.areas == "") {
      filtro4 = false
    }
    else {
      filtro4 = true
      sql4 = "$sql4 AND (prdloc.storeno = ? ) "
      sql4 = "$sql4 AND (prdloc.localizacao LIKE ? ) "
    }
    val stmt4 = cx.prepareStatement(sql4)
    stmt4.setInt(1, base.lojaOrigem!!.toInt())
    stmt4.setInt(2, base.pedidoOrigem!!.toInt())
    index = 2
    if(filtro1) {
      index++
      stmt4.setString(index, base.descIni)
      index++
      stmt4.setString(index, base.descFim)
    }
    if(filtro2);
    if(filtro3);
    if(filtro4) {
      index++
      stmt4.setInt(index, base.lojaArea!!.toInt())
      index++
      stmt4.setString(index, "%" + base.areas + "%")
    }
    stmt4.executeUpdate()
    stmt1.close()
    stmt2.close()
    stmt3.close()
    stmt4.close()
    cx.commit()
    return novo
  }
  
  @Throws(SQLException::class, Exception::class)
  fun executarNovo(lista: List<Oprd>, base: Base): Int {
    var novo = Integer.valueOf(0)
    val cx = connection
    cx!!.autoCommit = false
    val sql1 = "SELECT (MAX(no) + 1) AS proximo FROM ords WHERE storeno = ? "
    val stmt1 = cx.prepareStatement(sql1)
    stmt1.setInt(1, base.lojaDestino!!.toInt())
    stmt1.executeQuery()
    val rs1 = stmt1.resultSet
    if(rs1.next()) {
      novo = if(rs1.getInt("proximo") == 0) {
        Integer.valueOf(1)
      }
      else {
        Integer.valueOf(rs1.getInt("proximo"))
      }
    }
    if(base.informaPedidoDestino!!) {
      novo = base.pedidoDestino
    }
    val sql2 =
      "INSERT INTO ords (  no, date, vendno, discount, amt, package, custo_fin, others, eord_ordno, dataFaturamento ,invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig, l1, l2, l3 , l4, m1, m2, m3, m4, deliv, storeno, carrno, empno, prazo, eord_storeno, delivOriginal , bits, bits2, bits3, padbyte, indxno, repno, auxShort1, auxShort2, noofinst, status , s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd , auxChar, c1, c2, c3, c4 ) SELECT  ? as no, date, vendno, discount,  0 as amt, package, custo_fin, others, eord_ordno, dataFaturamento ,invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig, l1, l2, l3 , l4, m1, m2, m3, m4, deliv, ? as storeno, carrno, empno, prazo, eord_storeno, delivOriginal , bits, bits2, bits3, padbyte, indxno, repno, auxShort1, auxShort2, noofinst, 0 , s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd , auxChar, c1, c2, c3, c4 FROM   ords WHERE  storeno = ? AND    no = ? "
    val stmt2 = cx.prepareStatement(sql2)
    stmt2.setInt(1, novo.toInt())
    stmt2.setInt(2, base.lojaDestino!!.toInt())
    stmt2.setInt(3, base.lojaOrigem!!.toInt())
    stmt2.setInt(4, base.pedidoOrigem!!.toInt())
    stmt2.executeUpdate()
    val sql3 =
      "INSERT INTO oprd      ( oprd.storeno, oprd.ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms, oprd.auxLong1      , oprd.auxLong2, oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4      , oprd.auxMy3, oprd.auxMy4, oprd.qtty, oprd.qtty_src, oprd.qtty_xfr, oprd.cost, oprd.qttyRcv      , oprd.qttyCancel, oprd.qttyVendaMes, oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente      , oprd.stkDisponivel, oprd.qttyAbc, oprd.seqno, oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1      , oprd.auxShort2, oprd.auxShort3, oprd.auxShort4, oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte      , oprd.gradeFechada, oprd.obs ) SELECT ? AS storeno, ? AS ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms, oprd.auxLong1, oprd.auxLong2      , oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4, oprd.auxMy3, oprd.auxMy4      , ? AS qtty, ? AS qtty_src, oprd.qtty_xfr, oprd.cost, 0 AS qttyRcv, 0 AS qttyCancel, oprd.qttyVendaMes      , oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente, oprd.stkDisponivel, oprd.qttyAbc      , oprd.seqno, oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1, oprd.auxShort2, oprd.auxShort3      , oprd.auxShort4, oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte, oprd.gradeFechada, oprd.obs FROM oprd INNER JOIN prd ON (prd.no = oprd.prdno) WHERE  (oprd.storeno = ? ) AND    (oprd.ordno = ? ) AND    (oprd.prdno = ? )AND    (oprd.grade = ? )GROUP BY oprd.storeno, oprd.ordno, oprd.prdno,oprd.grade "
    for(oprd in lista) {
      val stmt3 = cx.prepareStatement(sql3)
      stmt3.setInt(1, base.lojaDestino!!.toInt())
      stmt3.setInt(2, novo.toInt())
      stmt3.setBigDecimal(3, oprd.qtd)
      stmt3.setBigDecimal(4, oprd.qtd)
      stmt3.setInt(5, base.lojaOrigem!!.toInt())
      stmt3.setInt(6, base.pedidoOrigem!!.toInt())
      stmt3.setString(7, oprd.prdno)
      stmt3.setString(8, oprd.grade)
      stmt3.executeUpdate()
      stmt3.close()
    }
    val sql4 =
      " UPDATE oprd  SET oprd.qtty = ( oprd.qtty - ? )  WHERE  (oprd.storeno = ? )  AND    (oprd.ordno = ? )  AND    (oprd.prdno = ? )  AND    (oprd.grade = ? )"
    for(oprd in lista) {
      val stmt4 = cx.prepareStatement(sql4)
      stmt4.setBigDecimal(1, oprd.qtd)
      stmt4.setInt(2, base.lojaOrigem!!.toInt())
      stmt4.setInt(3, base.pedidoOrigem!!.toInt())
      stmt4.setString(4, oprd.prdno)
      stmt4.setString(5, oprd.grade)
      stmt4.executeUpdate()
      stmt4.close()
    }
    val sql5 = " DELETE FROM oprd  WHERE oprd.storeno = ?  AND oprd.ordno = ?  AND oprd.qtty = 0 "
    val stmt5 = cx.prepareStatement(sql5)
    stmt5.setInt(1, base.lojaOrigem!!.toInt())
    stmt5.setInt(2, base.pedidoOrigem!!.toInt())
    stmt5.executeUpdate()
    stmt1.close()
    stmt2.close()
    stmt5.close()
    cx.commit()
    cx.close()
    return novo
  }
  
  @Throws(SQLException::class, Exception::class)
  fun duplicar(lojaOrigem: Int, lojaDestino: Int, numPedidoOrigem: Int, numPedidoDestino: Int): Int {
    var novo = Integer.valueOf(0)
    val cx = connection
    cx!!.autoCommit = false
    val sql1 = "SELECT (MAX(no) + 1) AS proximo FROM ords WHERE storeno = ? "
    val stmt1 = cx.prepareStatement(sql1)
    stmt1.setInt(1, lojaDestino)
    stmt1.executeQuery()
    val rs1 = stmt1.resultSet
    if(rs1.next()) {
      novo = if(rs1.getInt("proximo") == 0) {
        Integer.valueOf(1)
      }
      else {
        Integer.valueOf(rs1.getInt("proximo"))
      }
    }
    if(numPedidoDestino > 0) {
      novo = numPedidoDestino
    }
    val sql2 =
      "INSERT INTO ords (  no, date, vendno, discount, amt, package, custo_fin, others, eord_ordno, dataFaturamento ,invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig, l1, l2, l3 , l4, m1, m2, m3, m4, deliv, storeno, carrno, empno, prazo, eord_storeno, delivOriginal , bits, bits2, bits3, padbyte, indxno, repno, auxShort1, auxShort2, noofinst, status , s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd , auxChar, c1, c2, c3, c4 ) SELECT  ? as no, date, vendno, discount,  0 as amt, package, custo_fin, others, eord_ordno, dataFaturamento ,invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig, l1, l2, l3 , l4, m1, m2, m3, m4, deliv, ? as storeno, carrno, empno, prazo, eord_storeno, delivOriginal , bits, bits2, bits3, padbyte, indxno, repno, auxShort1, auxShort2, noofinst, 0 , s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd , auxChar, c1, c2, c3, c4 FROM   ords WHERE  storeno = ? AND    no = ? "
    val stmt2 = cx.prepareStatement(sql2)
    stmt2.setInt(1, novo.toInt())
    stmt2.setInt(2, lojaDestino)
    stmt2.setInt(3, lojaOrigem)
    stmt2.setInt(4, numPedidoOrigem)
    stmt2.executeUpdate()
    val sql3 =
      "INSERT INTO oprd      ( oprd.storeno, oprd.ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms, oprd.auxLong1      , oprd.auxLong2, oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4      , oprd.auxMy3, oprd.auxMy4, oprd.qtty, oprd.qtty_src, oprd.qtty_xfr, oprd.cost, oprd.qttyRcv      , oprd.qttyCancel, oprd.qttyVendaMes, oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente      , oprd.stkDisponivel, oprd.qttyAbc, oprd.seqno, oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1      , oprd.auxShort2, oprd.auxShort3, oprd.auxShort4, oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte      , oprd.gradeFechada, oprd.obs ) SELECT ? AS storeno, ? AS ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms, oprd.auxLong1, oprd.auxLong2      , oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4, oprd.auxMy3, oprd.auxMy4      , oprd.qtty, oprd.qtty_src, oprd.qtty_xfr, oprd.cost, qttyRcv, qttyCancel, oprd.qttyVendaMes      , oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente, oprd.stkDisponivel, oprd.qttyAbc      , oprd.seqno, oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1, oprd.auxShort2, oprd.auxShort3      , oprd.auxShort4, oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte, oprd.gradeFechada, oprd.obs FROM oprd INNER JOIN prd ON (prd.no = oprd.prdno) LEFT  JOIN prdloc ON (oprd.prdno = prdloc.prdno) WHERE  (oprd.storeno = ? ) AND    (oprd.ordno = ? ) GROUP BY oprd.prdno,oprd.grade "
    val stmt3 = cx.prepareStatement(sql3)
    stmt3.setInt(1, lojaDestino)
    stmt3.setInt(2, novo.toInt())
    stmt3.setInt(3, lojaOrigem)
    stmt3.setInt(4, numPedidoOrigem)
    stmt3.executeUpdate()
    stmt1.close()
    stmt2.close()
    stmt3.close()
    cx.commit()
    cx.close()
    return novo
  }
  
  @Throws(SQLException::class, Exception::class)
  fun verificaPedido(lojaOrigem: Int, pedidoOrigem: Int): Boolean {
    var resp = false
    val cx = connection
    cx!!.autoCommit = false
    val sql = "SELECT no FROM ords WHERE storeno = ? AND no = ? "
    val stmt = cx.prepareStatement(sql)
    stmt.setInt(1, lojaOrigem)
    stmt.setInt(2, pedidoOrigem)
    stmt.executeQuery()
    val rs = stmt.resultSet
    while(rs.next()) {
      if(rs.findColumn("no") > 0) {
        resp = true
        continue
      }
      resp = false
    }
    return resp
  }
  
  init {
    try {
      conexao = ConnectionFactory.instance?.obterConexao()
    } catch(ex: ClassNotFoundException) {
      Logger.getLogger(GestorDADOS::class.java.name)
        .log(Level.SEVERE, null as String?, ex)
    } catch(ex: Exception) {
      Logger.getLogger(GestorDADOS::class.java.name)
        .log(Level.SEVERE, null as String?, ex)
    }
  }
}