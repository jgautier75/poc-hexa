Before running keycloak in production, you have to build it because of custom spi extensions (spi-kafka & spi-federation)

```bash
docker build . -t "keycloak-spi-test:1.0.0"
```

Once done, a sample command for production would look like:

```bash
kc.sh start \
--optimized \
--hostname=auth.example.com \
--proxy=edge \
--db=postgres \
--db-url=jdbc:postgresql://db:5432/keycloak \
--db-username=keycloak \
--db-password=strongpassword \
--https-certificate-file=/certs/tls.crt \
--https-certificate-key-file=/certs/tls.key
```

Or for containers: https://www.keycloak.org/server/containers