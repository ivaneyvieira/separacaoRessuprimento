UPDATE users
SET bits2    = :bitAcesso,
    auxStr   = :abreviacoes,
    auxLong1 = :storeno
WHERE login = :login