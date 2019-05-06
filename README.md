# interconectionFlights
Application should return a list of flights departing from a given departure airport not earlier than the specified departure datetime and arriving to a given arrival airport not later than the specified arrival datetime. For interconnected flights the difference between the arrival and the next departure should be 2h or greater

## Assumptions

1. On unit test I only show my skills to build it. i know, in all jobs, i need better coverage and cases.

## Frameworks implemented on this solution

1. SpringBoot (server and Rest)
2. JUnit (testing)
3. Mokito (Testing)
4. Ehcahce (cache to external rest services)

## Services

```
URL: /api/v1/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}
METHOD: GET
EXAMPLE:/api/v1/interconnections?departure=DUB&arrival=LTN&departureDateTime=2019-05-06T09:00&arrivalDateTime=2019-05-06T21:20
```

## Compile

```
mvn clean install
```

## Run

```
CD into folder app
mvn spring-boot:run
```
