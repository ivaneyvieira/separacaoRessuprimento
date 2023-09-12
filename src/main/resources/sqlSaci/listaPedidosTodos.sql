SELECT storeno,
       no         AS ordno,
       DATE(date) AS data,
       l2         AS ordnoMae,
       C4         AS tipo
FROM sqldados.ords
WHERE ((no BETWEEN 10000 AND 99999 OR NO BETWEEN 100000000 AND 999999999)
  AND MID(no, 1, 1) * 1 IN (2, 3, 4, 5)
  AND storeno IN (:storeno))
   OR ((no BETWEEN 8000 AND 8999 OR no BETWEEN 80000 AND 89999 OR NO BETWEEN 800000000 AND 899999999)
  AND storeno IN (:storenoAux))