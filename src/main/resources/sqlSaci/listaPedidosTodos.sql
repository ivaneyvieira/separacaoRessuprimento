SELECT storeno,
       no AS ordno,
       l2 AS ordnoMae,
       C4 AS tipo
FROM sqldados.ords
WHERE storeno = :storeno
UNION
SELECT storeno,
       no AS ordno,
       l2 AS ordnoMae,
       C4 AS tipo
FROM sqldados.ords
WHERE storeno = 4
  AND no = 2