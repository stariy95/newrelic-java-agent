## ERRest instrumentation module

### Usage

- clone this repo
- setup environment by adding `jdk8=path/to/jdk-8` to the `gradle.properties` file 
- build via `./gradlew.bat build -x test`
- copy and use `newrelic-agent/build/newrelicJar/newrelic.jar` for the instrumentation

### Implementation notes

- ERRest related instrumentation code is located in the module `instrumentation/er-rest` 
- additionally, this implementation changes hardcoded class reference in the `com.newrelic.agent.Transaction`

### Limitation

- no config reloading at runtime
- no error reporting
- no traces other than root call
- no response headers and content type (?)





