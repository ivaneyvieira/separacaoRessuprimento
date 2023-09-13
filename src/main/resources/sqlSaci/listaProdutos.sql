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
       IFNULL(ROUND(S.qtty_varejo / 1000, 2), 0.00)                   AS saldo,
       mfno_ref                                                       AS fornecedorRef,
       IFNULL((S.qtty_varejo / 1000 + S.qtty_atacado / 1000), 0)      AS estoque,
       P.qttyPackClosed / 1000                                        AS embalagem
FROM sqldados.oprd AS O
       INNER JOIN sqldados.ords oP
                  ON oP.no = O.ordno AND O.storeno = oP.storeno
       INNER JOIN sqldados.prd AS P
                  ON (O.prdno = P.no)
       LEFT JOIN sqldados.prdloc AS L
                 ON (O.prdno = L.prdno AND O.grade = L.grade AND
                     (LENGTH(O.auxStr) > 20 OR O.auxStr = ''))
       LEFT JOIN sqldados.stk AS S
                 ON (O.prdno = S.prdno AND O.grade = S.grade AND S.storeno = 4)
WHERE O.storeno = :storeno
  AND O.ordno = :ordno
GROUP BY prdno, grade, IFNULL(L.localizacao, IF(LENGTH(O.auxStr) > 20, '', O.auxStr))