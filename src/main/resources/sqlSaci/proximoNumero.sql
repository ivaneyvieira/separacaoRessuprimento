SELECT (MAX(no) + 1) AS proximo
FROM ords
WHERE storeno = :storeno
  AND no BETWEEN (:destino * 1000 + 1) AND (:destino * 1000 + 999)