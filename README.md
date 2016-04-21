# Euro Live and Historic Cross Rates RESTful service
A demo SpringBoot project that implements a RESTful service to return EURO exchange rate versus a given currency going back as far as 90 days. The service currently consumes xml feed from the European Central Bank (ECB) resource: http://www.ecb.europa.eu/stats/exchange/eurofxref/html/index.en.html

### Running the app on command line
```
mvn spring-boot:run
```


### To Fetch Live Rates
```
curl -X GET "http://localhost:8989/v1/euroxrate/live/GBP"
```

```json
{"currency":"GBP","rate":0.791,"referenceDate":"2016-04-21"}
```

### To Fetch Historic Rates
```
curl -X GET "http://localhost:8989/v1/euroxrate/historic/GBP?refDate=2016-04-08"
```

```json
{"currency":"GBP","rate":0.8073,"referenceDate":"2016-04-08"}
```
