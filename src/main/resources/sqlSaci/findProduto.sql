SELECT P.no AS prdno, TRIM(MID(P.name, 1, 37)) AS descricao, IFNULL(B.grade, '') AS grade
FROM sqldados.prd           AS P
  LEFT JOIN sqldados.prdbar AS B
              ON P.no = B.prdno
WHERE P.no = LPAD(:prdno, 16, ' ')