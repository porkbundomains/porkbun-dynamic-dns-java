# porkbun-dynamic-dns-java

This is a quick and dirty dynamic DNS client written in Java for Porkbun's DNS management API. You can find more information about the API at https://porkbun.com/api/json/v3/documentation.
When updating an IPv4 record, the application will check your current IP utilizing the API's ping command to only then edit the record at Porkbun if different. When updating an IPv6 record, it will get the IPv6 from the local host.

## BUILD THE APP

Run `make`, or alternatively run:
```
javac -cp "./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar" -d . PorkbunDynDNSClient.java
```
### Build a JAR
Run `make jar`.

## RUN THE APP

First, rename the file `config.json.example` to `config.json` and update it with your API keys. You'll also need to ensure that API access is granted for the domain.

Check A record for `www.example.com`:
```
java -cp ./:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar PorkbunDynDNSClient example.com "www" A
```

Check A record for `example.com`:
```
java -cp ./:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar PorkbunDynDNSClient example.com "" A
```
### Run the JAR

Check A record for `example.com`:
```
java -jar porkbun-ddns.jar example.com "" A
```

Check A record for `www.example.com`:
```
java -jar porkbun-ddns.jar example.com "www" A
```

Check AAAA record for `www.example.com`:
```
java -jar porkbun-ddns.jar example.com "www" AAAA
```
