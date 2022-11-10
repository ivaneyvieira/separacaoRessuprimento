SELECT storeno,
       no         AS ordno,
       DATE(date) AS data,
       l2         AS ordnoMae,
       C4         AS tipo
FROM sqldados.ords
WHERE (no IN (54) OR no BETWEEN 10000 AND 99999 OR NO BETWEEN 100000000 AND 999999999)
  AND MID(no, 1, 1) * 1 IN (2, 3, 4, 5)
  AND storeno = :storeno
UNION
SELECT O.storeno,
       O.no       AS ordno,
       DATE(date) AS data,
       O.l2       AS ordnoMae,
       'L'        AS tipo
FROM sqldados.ords         AS O
  INNER JOIN sqldados.oprd AS P
	       ON O.storeno = P.storeno AND O.no = P.ordno AND P.prdno <> LPAD('19', 16, ' ')
WHERE O.storeno = 4
  AND O.no = 2
GROUP BY O.storeno, O.no
UNION
SELECT O.storeno,
       O.no       AS ordno,
       DATE(date) AS data,
       O.l2       AS ordnoMae,
       'L'        AS tipo
FROM sqldados.ords         AS O
  INNER JOIN sqldados.oprd AS P
	       ON O.storeno = P.storeno AND O.no = P.ordno AND P.prdno <> LPAD('19', 16, ' ')
WHERE O.storeno = 5
  AND O.no = 2
GROUP BY O.storeno, O.no