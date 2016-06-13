keytool -genkeypair -keyalg RSA -alias igitras -keypass mdxayjy -keystore server.jks -storepass igitras

keytool -export -keystore server.jks -alias igitras -file igitras.cer


openssl x509 -inform der -in igitras.cer -pubkey -noout

-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz1bpXSTC/tgb9aqQFYb7
Vf2Uxxqz7Cbpq3hM1FDa/zCentyqzv7HUzOvaGkBay+E7JPMIlIFSjaAcJe0M1Bi
3HNUiFjRp39lD0c9drbW8C6EjqLOnOJMVdqz0eoxaISGBhZcHcHYal1pXlQNJlky
rZZRgElDHnA/uXs5UkhiQyVoYSE0+T0MIKaF4DIcL67igWUzK9NwPHxSqizCkaiJ
WwXHxzVJGA9+k81vq1bl6FqysCobK/YrIqpg3vL7wT4SRn0pOVcABF0+PjvdPfUH
naAPU2gNkECwBJmsAhr02/lXmlmgeuQ/N2tCFvHZQHCTbKe/JK+tIMO73Qn8AgLZ
0QIDAQAB
-----END PUBLIC KEY-----
