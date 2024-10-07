SELECT P.no                      AS prdno,
       TRIM(MID(P.name, 1, 37))  AS descricao,
       IFNULL(B.grade, '')       AS grade,
       IFNULL(L.localizacao, '') AS localizacao
FROM sqldados.prd AS P
       LEFT JOIN sqldados.prdbar AS B
                 ON P.no = B.prdno
       LEFT JOIN sqldados.prdAdicional AS L
                 ON L.prdno = P.no AND L.grade = IFNULL(B.grade, '') AND L.storeno = 4
WHERE P.no = LPAD(:prdno, 16, ' ')