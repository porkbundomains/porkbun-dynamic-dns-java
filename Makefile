all:
	javac -cp "./libs/httpcomponents-client-5.0.3/lib/httpclient5-5.0.3.jar:./libs/httpcomponents-client-5.0.3/lib/httpcore5-5.0.2.jar:./libs/httpcomponents-client-5.0.3/lib/slf4j-api-1.7.25.jar:./libs/json.jar" -d . PorkbunDynDNSClient.java

jar: all
	jar cmf Manifest.mf porkbun-ddns.jar PorkbunDynDNSClient.class libs/ config.json
