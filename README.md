# porkbun-dynamic-dns-java

This is a quick and dirty dynamic DNS client written in Java for Porkbun's DNS management API. You can find more information about the API at https://porkbun.com/api/json/v3/documentation. The application will check your current IP utilizing the API's ping command and then edit the record at Porkbun if different.

BUILD THE APP

Run make or:

javac -cp "./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar" -d . PorkbunDynDNSClient.java

RUN THE APP

First, rename the file config.json.example to config.json and update it with your API keys. You'll also need to ensure that API access is granted for the domain.

Check A record for www.example.com:
java -cp ./:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar PorkbunDynDNSClient example.com "www" A

Check A record for example.com:
java -cp ./:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar PorkbunDynDNSClient example.com "" A
