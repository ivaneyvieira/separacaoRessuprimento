SELECT (MAX(ordno) + 1) AS proximo
FROM (SELECT ordno
      FROM inv
      UNION
      SELECT no
      FROM ords
      WHERE storeno = :storeno) AS D
WHERE ordno BETWEEN (:destino * 100000000) AND (:destino * 100000000 + 99999998);
