SELECT (MAX(no) + 1) AS proximo
FROM ords
WHERE storeno = :storeno
  AND no BETWEEN (:destino * 10000) AND (:destino * 10000 + 9998)