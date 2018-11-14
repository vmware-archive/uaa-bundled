# UAA Bundled

[![Build Status](https://travis-ci.org/ghillert/uaa-bundled.svg?branch=master)](https://travis-ci.org/ghillert/uaa-bundled)

## Introduction

The [CloudFoundry User Account and Authentication][1] (UAA) Server is an [OpenID][2] certified OAuth2 provider that is distributed as a [WAR file][3]. In order to provide a more streamlined user experience, this project wraps the WAR file into a [Spring Boot][4] application and as such provides an executable JAR that includes both the _UAA_ and an embedded [Tomcat][5] instance.

**IMPORTANT**

This project is not to be used in production environments. It is provided solely as an example / demo.

## Usage

In order to get started, simply execute the following steps:

```bash
$ git clone https://github.com/pivotal/uaa-bundled.git
$ cd uaa-bundled
$ ./mvnw clean install
$ java -jar target/uaa-bundled-1.0.0.BUILD-SNAPSHOT.jar
```

For customization you can also reference, e.g. a `uaa.yml` file, simply specify the respective
environment variable:

```bash
$ export CLOUD_FOUNDRY_CONFIG_PATH=/path/to/dev/ldap-uaa-example
```

For a more detailed sample that uses the UAA to provide security to Spring Cloud Data Flow, please have a look at the [Spring Cloud Data Flow LDAP UAA Sample][6].


[1]: https://github.com/cloudfoundry/uaa
[2]: https://openid.net/certification/
[3]: https://spring.io/understanding/WAR
[4]: https://spring.io/projects/spring-boot
[5]: http://tomcat.apache.org/
[6]: https://github.com/spring-cloud/spring-cloud-dataflow-samples/tree/master/security-ldap-uaa-example