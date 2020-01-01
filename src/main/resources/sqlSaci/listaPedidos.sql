SELECT storeno, no AS ordno
FROM sqldados.ords
WHERE (no BETWEEN 1000 AND 9999 OR NO BETWEEN 1000001000 AND 1999999999)
  AND storeno = :storeno