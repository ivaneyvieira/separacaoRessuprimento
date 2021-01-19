SELECT storeno,
       no AS ordno,
       l2 AS ordnoMae,
       C4 AS tipo
FROM sqldados.ords
WHERE (no BETWEEN 10000 AND 99999 OR NO BETWEEN 100000000 AND 999999999)
  AND MID(no, 1, 1) * 1 IN (2, 3, 4, 5)
  AND storeno = :storeno
UNION
SELECT storeno,
       no AS ordno,
       l2 AS ordnoMae,
       C4 AS tipo
FROM sqldados.ords
WHERE storeno = 4
  AND no = 2