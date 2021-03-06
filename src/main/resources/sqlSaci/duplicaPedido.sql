delete
from sqldados.oprd
where storeno = :storenoNovo
  and ordno = :ordnoNovo
  and storeno = 1
  and ordno = 54;

INSERT INTO sqldados.ords (no, date, vendno, discount, amt, package, custo_fin, others, eord_ordno,
			   dataFaturamento, invno, freightAmt, auxLong1, auxLong2, amtOrigem,
			   dataEntrega,
			   discountOrig, l1, l2, l3, l4, m1, m2, m3, m4, deliv, storeno, carrno,
			   empno,
			   prazo, eord_storeno, delivOriginal, bits, bits2, bits3, padbyte, indxno,
			   repno,
			   auxShort1, auxShort2, noofinst, status, s1, s2, s3, s4, frete, remarks,
			   ordnoFromVend, remarksInv, remarksRcv, remarksOrd, auxChar, c1, c2, c3,
			   c4)
SELECT :ordnoNovo                       AS no,
       date,
       vendno,
       discount,
       0                                AS amt,
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
       :ordno                           AS l2,
       l3,
       l4,
       m1,
       m2,
       m3,
       m4,
       deliv,
       :storenoNovo                     AS storeno,
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
       0,
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
       if(:ordnoNovo IN (54), 'S', 'D') AS c4
FROM sqldados.ords
WHERE storeno = :storeno
  AND no = :ordno;

INSERT INTO sqldados.oprd (storeno, ordno, mult, ipi, freight, icms, auxLong1, auxLong2, auxMy1,
			   auxMy2,
			   icmsSubst, auxLong3, auxLong4, auxMy3, auxMy4, qtty, qtty_src, qtty_xfr,
			   cost,
			   qttyRcv, qttyCancel, qttyVendaMes, qttyVendaMesAnt, qttyVendaMedia,
			   qttyPendente,
			   stkDisponivel, qttyAbc, seqno, status, bits, bits2, auxShort1, auxShort2,
			   auxShort3, auxShort4, prdno, grade, remarks, padbyte, gradeFechada, obs,
			   auxStr)
SELECT :storenoNovo       AS storeno,
       :ordnoNovo         AS ordno,
       mult,
       ipi,
       freight,
       icms,
       auxLong1,
       auxLong2,
       auxMy1,
       auxMy2,
       icmsSubst,
       ROUND(qtty * 1000) AS auxLong3,
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
       seqno,
       status,
       bits,
       bits2,
       auxShort1,
       auxShort2,
       auxShort3,
       auxShort4,
       prdno,
       grade,
       remarks,
       padbyte,
       gradeFechada,
       obs,
       ''                 AS auxStr
FROM sqldados.oprd
WHERE (storeno = :storeno)
  AND (ordno = :ordno)
  AND (prdno != LPAD('19', 16, ' '))
GROUP BY prdno, grade;

delete
from sqldados.oprd
where storeno = :storeno
  and ordno = :ordno
  and storeno = 4
  and ordno = 2
  and prdno <> LPAD('19', 16, ' ');

delete
from sqldados.oprd
where storeno = :storeno
  and ordno = :ordno
  and storeno = 5
  and ordno = 2
  and prdno <> LPAD('19', 16, ' ')
