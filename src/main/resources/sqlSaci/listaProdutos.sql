REPLACE INTO ordsPendente(no, date, vendno, discount, amt, package, custo_fin, others, eord_ordno,
			  dataFaturamento, invno, freightAmt, auxLong1, auxLong2, amtOrigem,
			  dataEntrega, discountOrig, l1, l2, l3, l4, m1, m2, m3, m4, deliv, storeno,
			  carrno, empno, prazo, eord_storeno, delivOriginal, bits, bits2, bits3,
			  padbyte, indxno, repno, auxShort1, auxShort2, noofinst, status, s1, s2,
			  s3, s4, frete, remarks, ordnoFromVend, remarksInv, remarksRcv, remarksOrd,
			  auxChar, c1, c2, c3, c4)
SELECT no,
       date,
       vendno,
       discount,
       amt,
       package,
       custo_fin,
       others,
       eord_ordno,
       dataFaturamento,
       invno,
       freightAmt,
       auxLong1,
       auxLong2,
       amtOrigem,
       dataEntrega,
       discountOrig,
       l1,
       l2,
       l3,
       l4,
       m1,
       m2,
       m3,
       m4,
       deliv,
       storeno,
       carrno,
       empno,
       prazo,
       eord_storeno,
       delivOriginal,
       bits,
       bits2,
       bits3,
       padbyte,
       indxno,
       repno,
       auxShort1,
       auxShort2,
       noofinst,
       status,
       s1,
       s2,
       s3,
       s4,
       frete,
       remarks,
       ordnoFromVend,
       remarksInv,
       remarksRcv,
       remarksOrd,
       auxChar,
       c1,
       c2,
       c3,
       c4
FROM ords O
WHERE O.storeno = :storeno
  AND O.no = :ordno;

REPLACE INTO oprdPendente(date, ordno, mult, ipi, freight, icms, auxLong1, auxLong2, auxMy1, auxMy2,
		     icmsSubst, auxLong3, auxLong4, auxMy3, auxMy4, qtty, qtty_src, qtty_xfr, cost,
		     qttyRcv, qttyCancel, qttyVendaMes, qttyVendaMesAnt, qttyVendaMedia,
		     qttyPendente, stkDisponivel, qttyAbc, storeno, seqno, status, bits, bits2,
		     auxShort1, auxShort2, auxShort3, auxShort4, prdno, grade, remarks, padbyte,
		     gradeFechada, obs, auxStr)
SELECT oP.date,
       ordno,
       mult,
       ipi,
       freight,
       icms,
       O.auxLong1,
       O.auxLong2,
       auxMy1,
       auxMy2,
       icmsSubst,
       auxLong3,
       auxLong4,
       auxMy3,
       auxMy4,
       qtty,
       qtty_src,
       qtty_xfr,
       cost,
       qttyRcv,
       qttyCancel,
       qttyVendaMes,
       qttyVendaMesAnt,
       qttyVendaMedia,
       qttyPendente,
       stkDisponivel,
       qttyAbc,
       O.storeno,
       seqno,
       O.status,
       O.bits,
       O.bits2,
       O.auxShort1,
       O.auxShort2,
       auxShort3,
       auxShort4,
       prdno,
       grade,
       O.remarks,
       O.padbyte,
       gradeFechada,
       obs,
       auxStr
FROM ords         AS oP
  INNER JOIN oprd AS O
	       ON oP.no = O.ordno AND oP.storeno = O.storeno
WHERE O.storeno = :storeno
  AND O.ordno = :ordno
  AND NOT (O.storeno = 4 AND O.ordno = 2 AND O.prdno = 19)
  AND NOT (O.storeno = 5 AND O.ordno = 2 AND O.prdno = 19);

SELECT O.prdno,
       O.grade,
       DATE(oP.date)                                                  AS data,
       IFNULL(TRIM(MID(P.name, 1, 37)), '')                           AS descricao,
       P.mfno                                                         AS fornecedor,
       IFNULL(LPAD(P.clno, 6, '0'), '')                               AS centrodelucro,
       IFNULL(L.localizacao, IF(LENGTH(O.auxStr) > 20, '', O.auxStr)) AS localizacao,
       P.typeno                                                       AS tipo,
       IFNULL(ROUND(O.auxLong3 / 1000, 2), 0.00)                      AS qttyOriginal,
       IFNULL(ROUND(O.qtty, 2), 0.00)                                 AS qtty,
       IFNULL(ROUND(S.qtty_varejo / 1000, 2), 0.00)                   AS saldo
FROM sqldados.oprd           AS O
  INNER JOIN sqldados.ords      oP
	       ON oP.no = O.ordno AND O.storeno = oP.storeno
  INNER JOIN sqldados.prd    AS P
	       ON (O.prdno = P.no)
  LEFT JOIN  sqldados.prdloc AS L
	       ON (O.prdno = L.prdno AND O.grade = L.grade AND
		   (LENGTH(O.auxStr) > 20 OR O.auxStr = ''))
  LEFT JOIN  sqldados.stk    AS S
	       ON (O.prdno = S.prdno AND O.grade = S.grade AND S.storeno = 4)
WHERE O.storeno = :storeno
  AND O.ordno = :ordno
  AND NOT (O.storeno = 4 AND O.ordno = 2 AND O.prdno = 19)
  AND NOT (O.storeno = 5 AND O.ordno = 2 AND O.prdno = 19)
GROUP BY prdno, grade, IFNULL(L.localizacao, IF(LENGTH(O.auxStr) > 20, '', O.auxStr))