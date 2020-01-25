SELECT MAX(no) AS proximo
FROM ords AS      O
  INNER JOIN oprd P
               ON P.storeno = O.storeno AND P.ordno = O.no
WHERE O.storeno = :storeno
  AND O.no BETWEEN (:destino * 1000 + 1) AND (:destino * 1000 + 998)
  AND O.c4 = 'L'
  AND P.auxStr LIKE :abreviacao