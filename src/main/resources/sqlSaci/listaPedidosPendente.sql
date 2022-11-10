DROP TABLE IF EXISTS T_ORDS;
CREATE TEMPORARY TABLE T_ORDS
SELECT O.storeno,
       O.no         AS ordno,
       DATE(O.date) AS data,
       O.l2         AS ordnoMae,
       O.C4         AS tipo,
       P.prdno,
       P.grade
FROM sqldados.ordsPendente            O
  INNER JOIN sqldados.oprdPendente AS P
	       ON O.storeno = P.storeno AND O.no = P.ordno AND P.date = O.date AND
		  P.prdno <> LPAD('19', 16, ' ')
WHERE (O.no IN (54) OR no BETWEEN 10000 AND 99999 OR NO BETWEEN 100000000 AND 999999999)
  AND MID(no, 1, 1) * 1 IN (2, 3, 4, 5)
  AND O.storeno = :storeno;


SELECT storeno,
       ordno,
       data,
       ordnoMae,
       tipo
FROM T_ORDS                                                              AS P
  LEFT JOIN (SELECT O.storeno,
		    O.no   AS ordno,
		    O.date AS data,
		    prdno,
		    grade
	     FROM sqldados.ords          AS O
	       INNER JOIN sqldados.oprd AS E
			    ON O.storeno = E.storeno AND O.no = E.ordno) AS D
	      USING (storeno, ordno, data, prdno, grade)
WHERE D.ordno IS NULL
GROUP BY P.storeno, P.ordno