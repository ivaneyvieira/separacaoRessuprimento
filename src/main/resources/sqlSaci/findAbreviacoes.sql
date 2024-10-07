SELECT MID(localizacao, 1, 4) AS abreviacoes
FROM prdAdicional
GROUP BY abreviacoes
HAVING abreviacoes LIKE 'CD%'
   AND LENGTH(abreviacoes) = 4
ORDER BY abreviacoes