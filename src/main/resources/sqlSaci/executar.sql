DO @NOVO := (SELECT (MAX(no) + 1) AS proximo
             FROM ords
             WHERE storeno = :storeno);

INSERT INTO ords (no, date, vendno, discount, amt, package, custo_fin, others, eord_ordno,
                  dataFaturamento, invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega,
                  discountOrig, l1, l2, l3, l4, m1, m2, m3, m4, deliv, storeno, carrno, empno,
                  prazo, eord_storeno, delivOriginal, bits, bits2, bits3, padbyte, indxno, repno,
                  auxShort1, auxShort2, noofinst, status, s1, s2, s3, s4, frete, remarks,
                  ordnoFromVend, remarksInv, remarksRcv, remarksOrd, auxChar, c1, c2, c3, c4)
SELECT @NOVO AS no, date, vendno, discount, 0 AS amt, package, custo_fin, others, eord_ordno,
       dataFaturamento, invno, freightAmt, auxLong1, auxLong2, amtOrigem, dataEntrega, discountOrig,
       l1, l2, l3, l4, m1, m2, m3, m4, deliv, :storeno AS storeno, carrno, empno, prazo,
       eord_storeno, delivOriginal, bits, bits2, bits3, padbyte, indxno, repno, auxShort1,
       auxShort2, noofinst, 0, s1, s2, s3, s4, frete, remarks, ordnoFromVend, remarksInv,
       remarksRcv, remarksOrd, auxChar, c1, c2, c3, c4
FROM ords
WHERE storeno = :storeno AND
      no = :ordno;

INSERT INTO oprd (oprd.storeno, oprd.ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms,
                  oprd.auxLong1, oprd.auxLong2,
                  oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3, oprd.auxLong4,
                  oprd.auxMy3, oprd.auxMy4,
                  oprd.qtty,
                  oprd.qtty_src, oprd.qtty_xfr, oprd.cost, oprd.qttyRcv, oprd.qttyCancel,
                  oprd.qttyVendaMes,
                  oprd.qttyVendaMesAnt, oprd.qttyVendaMedia, oprd.qttyPendente, oprd.stkDisponivel,
                  oprd.qttyAbc,
                  oprd.seqno,
                  oprd.status, oprd.bits, oprd.bits2, oprd.auxShort1, oprd.auxShort2,
                  oprd.auxShort3, oprd.auxShort4,
                  oprd.prdno, oprd.grade, oprd.remarks, oprd.padbyte, oprd.gradeFechada, oprd.obs)
SELECT :storeno AS storeno, @NOVO AS ordno, oprd.mult, oprd.ipi, oprd.freight, oprd.icms,
       oprd.auxLong1, oprd.auxLong2, oprd.auxMy1, oprd.auxMy2, oprd.icmsSubst, oprd.auxLong3,
       oprd.auxLong4, oprd.auxMy3, oprd.auxMy4, oprd.qtty, oprd.qtty_src, oprd.qtty_xfr, oprd.cost,
       0 AS qttyRcv, 0 AS qttyCancel, oprd.qttyVendaMes, oprd.qttyVendaMesAnt, oprd.qttyVendaMedia,
       oprd.qttyPendente, oprd.stkDisponivel, oprd.qttyAbc, oprd.seqno, oprd.status, oprd.bits,
       oprd.bits2, oprd.auxShort1, oprd.auxShort2, oprd.auxShort3, oprd.auxShort4, oprd.prdno,
       oprd.grade, oprd.remarks, oprd.padbyte, oprd.gradeFechada, oprd.obs
FROM oprd
WHERE (oprd.storeno = :storeno) AND
      (oprd.ordno = :ordno)
GROUP BY oprd.prdno, oprd.grade;

UPDATE oprd
SET oprd.qttyCancel = oprd.qtty
WHERE (oprd.storeno = :storeno) AND
      (oprd.ordno = :ordno);