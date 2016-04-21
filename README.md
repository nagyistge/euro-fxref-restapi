# euro-fxref-restapi
A demo SpringBoot project that that uses a RESTful service to return EURO exchange rate versus a given currency going back as far as 90 days.

### To Fetch Live Rates
```
curl -X GET "http://localhost:8989/v1/euroxrate/live/GBP"
```

### To Fetch Historic Rates
```
curl -X GET "http://localhost:8989/v1/euroxrate/historic/GBP?refDate=2016-04-08"
```
