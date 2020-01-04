SELECT O.prdno, O.grade, IFNULL(TRIM(MID(P.name, 1, 37)), '') AS descricao, P.mfno AS fornecedor,
       IFNULL(LPAD(P.clno, 6, '0'), '') AS centrodelucro, IFNULL(L.localizacao, '') AS localizacao,
       P.typeno AS tipo, IFNULL(ROUND(O.qtty, 2), 0.00) AS qtty,
       IFNULL(ROUND(S.qtty_varejo / 1000, 2), 0.00) AS saldo
FROM sqldados.oprd           AS O
  INNER JOIN sqldados.prd    AS P
               ON (O.prdno = P.no)
  LEFT JOIN  sqldados.prdloc AS L
               ON (O.prdno = L.prdno AND O.grade = L.grade)
  LEFT JOIN  sqldados.stk    AS S
               ON (O.prdno = S.prdno AND O.grade = S.grade AND S.storeno = 4)
WHERE O.storeno = :storeno AND
      O.ordno = :ordno
GROUP BY L.localizacao, prdno, grade
ORDER BY L.localizacao, prdno, grade