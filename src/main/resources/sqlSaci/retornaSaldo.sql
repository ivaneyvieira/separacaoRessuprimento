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
FROM ords o
WHERE storeno = :storeno
  AND no BETWEEN :ordno AND :ordno;

REPLACE INTO oprdPendente(date, ordno, mult, ipi, freight, icms, auxLong1, auxLong2, auxMy1,
			  auxMy2, icmsSubst, auxLong3, auxLong4, auxMy3, auxMy4, qtty, qtty_src,
			  qtty_xfr, cost, qttyRcv, qttyCancel, qttyVendaMes, qttyVendaMesAnt,
			  qttyVendaMedia, qttyPendente, stkDisponivel, qttyAbc, storeno, seqno,
			  status, bits, bits2, auxShort1, auxShort2, auxShort3, auxShort4, prdno,
			  grade, remarks, padbyte, gradeFechada, obs, auxStr)
SELECT O.date,
       ordno,
       mult,
       ipi,
       freight,
       icms,
       P.auxLong1,
       P.auxLong2,
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
       P.storeno,
       seqno,
       P.status,
       P.bits,
       P.bits2,
       P.auxShort1,
       P.auxShort2,
       auxShort3,
       auxShort4,
       prdno,
       grade,
       P.remarks,
       P.padbyte,
       gradeFechada,
       obs,
       auxStr
FROM ords         AS O
  INNER JOIN oprd AS P
	       ON O.no = P.ordno AND P.storeno = O.storeno
WHERE O.storeno = :storeno
  AND O.no BETWEEN :ordno AND :ordno;

INSERT IGNORE INTO oprd (storeno, ordno, mult, ipi, freight, icms, auxLong1, auxLong2, auxMy1,
                         auxMy2, icmsSubst, auxLong3, auxLong4, auxMy3, auxMy4, qtty, qtty_src,
                         qtty_xfr, cost, qttyRcv, qttyCancel, qttyVendaMes, qttyVendaMesAnt,
                         qttyVendaMedia, qttyPendente, stkDisponivel, qttyAbc, seqno, status, bits,
                         bits2, auxShort1, auxShort2, auxShort3, auxShort4, prdno, grade, remarks,
                         padbyte, gradeFechada, obs, auxStr)
SELECT :storeno AS storeno, :ordnoMae AS ordno, mult, ipi, freight, icms, auxLong1, auxLong2,
       auxMy1, auxMy2, icmsSubst, @ORIGINAL := auxLong3, auxLong4, auxMy3, auxMy4,
       (auxLong3 / 1000) - :qttyEdit AS qtty, (auxLong3 / 1000) - :qttyEdit AS qtty_src, qtty_xfr,
       cost, 0 AS qttyRcv, 0 AS qttyCancel, qttyVendaMes, qttyVendaMesAnt, qttyVendaMedia,
       qttyPendente, stkDisponivel, qttyAbc, seqno, status, bits, bits2, auxShort1, auxShort2,
       auxShort3, auxShort4, prdno, grade, remarks, padbyte, gradeFechada, obs,
       :localizacao AS auxStr
FROM oprd
WHERE (storeno = :storeno) AND
      (ordno = :ordno) AND
      (prdno = :prdno) AND
      (grade = :grade)
GROUP BY storeno, ordno, prdno, grade;

UPDATE oprd
SET oprd.qtty     = (@ORIGINAL / 1000) - :qttyEdit * 1.00,
    oprd.qtty_src = (@ORIGINAL / 1000) - :qttyEdit * 1.00,
    oprd.auxStr   = :localizacao
WHERE (oprd.storeno = :storeno) AND
      (oprd.ordno = :ordnoMae) AND
      (oprd.prdno = :prdno) AND
      (oprd.grade = :grade);

UPDATE oprd
SET oprd.qtty     = :qttyEdit * 1.00,
    oprd.qtty_src = :qttyEdit * 1.00,
    oprd.auxStr   = :localizacao
WHERE (oprd.storeno = :storeno) AND
      (oprd.ordno = :ordno) AND
      (oprd.prdno = :prdno) AND
      (oprd.grade = :grade);

DELETE
FROM oprd
WHERE oprd.storeno = :storeno AND
      oprd.ordno = :ordnoMae AND
      oprd.prdno = :prdno AND
      oprd.grade = :grade AND
      ROUND(oprd.qtty, 2) <= 0;

DELETE
FROM oprd
WHERE oprd.storeno = :storeno AND
      oprd.ordno = :ordno AND
      oprd.prdno = :prdno AND
      oprd.grade = :grade AND
      ROUND(oprd.qtty, 2) <= 0
