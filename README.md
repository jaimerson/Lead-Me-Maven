# Lead Me

## Requirements

Java >= 1.8 and Maven >= 3.1

## Installing

```bash
mvn clean
mvn install:install-file -Dfile=./weka.jar -DgroupId=weka -DartifactId=weka  -Dversion=1.0 -Dpackaging=jar
mvn install
```

## Running tests

```bash
mvn test
```

## Running Application

```bash
java -jar target/Lead-Me-1.0-SNAPSHOT.jar
```

### Mockup login
```
user: 20172017
pass: 123456
```
