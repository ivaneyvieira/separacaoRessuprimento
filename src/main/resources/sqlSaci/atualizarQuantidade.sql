INSERT INTO oprd (storeno, ordno, mult, ipi, freight, icms, auxLong1, auxLong2, auxMy1, auxMy2,
                  icmsSubst, auxLong3, auxLong4, auxMy3, auxMy4, qtty, qtty_src, qtty_xfr, cost,
                  qttyRcv, qttyCancel, qttyVendaMes, qttyVendaMesAnt, qttyVendaMedia, qttyPendente,
                  stkDisponivel, qttyAbc, seqno, status, bits, bits2, auxShort1, auxShort2,
                  auxShort3, auxShort4, prdno, grade, remarks, padbyte, gradeFechada, obs)
SELECT :storeno AS storeno, @NOVO AS ordno, mult, ipi, freight, icms, auxLong1, auxLong2, auxMy1,
       auxMy2, icmsSubst, auxLong3, auxLong4, auxMy3, auxMy4, ? AS qtty, ? AS qtty_src, qtty_xfr,
       cost, 0 AS qttyRcv, 0 AS qttyCancel, qttyVendaMes, qttyVendaMesAnt, qttyVendaMedia,
       qttyPendente, stkDisponivel, qttyAbc, seqno, status, bits, bits2, auxShort1, auxShort2,
       auxShort3, auxShort4, prdno, grade, remarks, padbyte, gradeFechada, obs
FROM oprd
WHERE (storeno = :storeno) AND
      (ordno = :ordno) AND
      (prdno = :prdno) AND
      (grade = :grade)
GROUP BY storeno, ordno, prdno, grade;

UPDATE oprd
SET oprd.qtty = (oprd.qtty - :qtty)
WHERE (oprd.storeno = :storeno) AND
      (oprd.ordno = :ordno) AND
      (oprd.prdno = :prdno) AND
      (oprd.grade = :grade);

DELETE
FROM oprd
WHERE oprd.storeno = :storeno AND
      oprd.ordno = :ordno AND
      oprd.qtty <= 0;