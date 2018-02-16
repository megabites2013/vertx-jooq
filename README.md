# vertx-jooq
A [jOOQ](http://www.jooq.org/)-CodeGenerator to create [vertx](http://vertx.io/)-ified DAOs and POJOs!
Perform all CRUD-operations asynchronously and convert your POJOs from/into a `io.vertx.core.JsonObject`.

## new in version 3.0
A lot has changed - not only under the hood.
- Starting from this version on, `vertx-jooq` both includes the JDBC
and the async variant (formerly known as [`vertx-jooq-async`](https://github.com/jklingsporn/vertx-jooq-async/)). This
 avoids duplicate code and thus provides better stability.
- Say good bye callback-API: everybody who has written code that is more complex than a simple `HelloWorld` application
 hates callback-APIs. That is why I decided to let the classic-module now return Vertx `Futures` instead of accepting a
 `Handler` to deal with the result.
- `vertx-jooq-future` becomes `vertx-jooq-completablefuture`: that was more or less a consequence of the decision to let the
classic API return `Futures` now.
- Consistent naming: I decided to prefix any DAO-method that is based on a `SELECT` with `find`, followed by `One` if
it returns one value, or `Many` if it is capable to return many values, followed by a condition to define how the value is
obtained, eg `byId`. If you are upgrading from a previous version, you will have to run some search and replace sessions in your favorite IDE.
- DAOs are no longer capable of executing arbitrary SQL. There were two main drivers for this decision: 1. joining the JDBC
 and the async API did not allow it. 2. DAOs are bound to a POJO and should only operate on the POJO's type. With the option to execute any
  SQL one could easily join on POJOs of other types and thus break boundaries. You can still execute type-safe SQL asynchronously
  using one of the `QueryExecutors` though.
- Never again call blocking DAO-methods by accident: in previous vertx-jooq versions each `VertxDAO` extended from jOOQ's `DAOImpl` class.
This made it easy to just wrap the blocking variant of a CRUD-operation in a `Vertx.executeBlocking` block to get the async variant
  of it. The downside however was that the blocking CRUD-operations were still visible in the DAO-API and it was up to the user
  to call the correct (async) method.

## different needs, different apis
![What do you want](https://media.giphy.com/media/E87jjnSCANThe/giphy.gif)

Before you start generating code using vertx-jooq, you have to answer these questions:
- What API do you want to use? There are three options:
  - a `io.vertx.core.Future`-based API. This is `vertx-jooq-classic`.
  - a [rxjava2](https://github.com/ReactiveX/RxJava) based API. This is `vertx-jooq-rx`.
  - a API that returns a [vertx-ified implementation](https://github.com/cescoffier/vertx-completable-future)
  of `java.util.concurrent.CompletableFuture` for all async DAO operations and thus makes chaining your async operations easier.
  It has some limitations which you need to be aware about (see [known issues](https://github.com/jklingsporn/vertx-jooq#known-issues)).
  This is `vertx-jooq-completablefuture`.
- How do you want to communicate with the database? There are two options:
  - Using good old JDBC.
  - Using an [asynchronous driver](https://github.com/mauricio/postgresql-async) database driver.
- Do you use [Guice](https://github.com/google/guice) for dependency injection or not.

When you made your choice, you can start to configure the code-generator. This can be either done programmatically or
 using a maven- or gradle-plugin. Please check the documentation in one of the variants of how to set it up.

- [`vertx-jooq-classic-async`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-classic-async)
- [`vertx-jooq-classic-jdbc`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-classic-jdbc)
- [`vertx-jooq-rx-async`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-rx-async)
- [`vertx-jooq-rx-jdbc`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-rx-jdbc)
- [`vertx-jooq-completablefuture-async`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-completablefuture-async)
- [`vertx-jooq-completablefuture-jdbc`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-completablefuture-jdbc)


## example
Once the generator is set up, it will create DAOs like in the example below (using `vertx-jooq-classic-jdbc`).
```
//Setup your jOOQ configuration
Configuration configuration = ...

//setup Vertx
Vertx vertx = Vertx.vertx();

//instantiate a DAO (which is generated for you)
SomethingDao dao = new SomethingDao(configuration,vertx);

//fetch something with ID 123...
dao.findOneByIdAsync(123)
    .setHandler(res->{
        		if(res.succeeded()){
            		vertx.eventBus().send("sendSomething",something.toJson())
        		}else{
        				System.err.println("Something failed badly: "+res.cause().getMessage());
        		}
        });

//maybe consume it in another verticle
vertx.eventBus().<JsonObject>consumer("sendSomething", jsonEvent->{
    JsonObject message = jsonEvent.body();
    //Convert it back into a POJO...
    Something something = new Something(message);
    //... change some values
    something.setSomeregularnumber(456);
    //... and update it into the DB
    Future<Void> updatedFuture = dao.updateAsync(something);
});

//or do you prefer writing your own type-safe SQL?
JDBCClassicGenericQueryExecutor queryExecutor = new JDBCClassicGenericQueryExecutor(configuration,vertx);
Future<Integer> updatedCustomFuture = queryExecutor.execute(dslContext ->
				dslContext
				.update(Tables.SOMETHING)
				.set(Tables.SOMETHING.SOMEREGULARNUMBER,456)
				.where(Tables.SOMETHING.SOMEID.eq(something.getSomeid()))
				.execute()
);

//check for completion
updatedCustomFuture.setHandler(res->{
		if(res.succeeded()){
				System.out.println("Rows updated: "+res.result());
		}else{
				System.err.println("Something failed badly: "+res.cause().getMessage());
		}
});
```

Do you use dependency injection? In addition to the `FutureVertxGenerator`, there is also a generator with [Guice](https://github.com/google/guice) support. If you're using the `FutureVertxGuiceGenerator`,
the `setConfiguration(org.jooq.Configuration)` and `setVertx(io.core.Vertx)` methods are annotated with `@javax.inject.Inject` and a
Guice `Module` is created which binds all created VertxDAOs to their implementation. It plays nicely together with the [vertx-guice](https://github.com/ef-labs/vertx-guice) module that enables dependency injection for vertx.

# maven
```
<dependency>
  <groupId>io.github.jklingsporn</groupId>
  <artifactId>vertx-jooq-future</artifactId>
  <version>3.0.0-BETA</version>
</dependency>
```
# maven code generator configuration example for mysql
The following code-snippet can be copy-pasted into your pom.xml to generate code from your MySQL database schema.

**Watch out for placeholders beginning with 'YOUR_xyz' though! E.g. you have to define credentials for DB access and specify the target directory where jOOQ
should put the generated code into, otherwise it won't run!**

After you replaced all placeholders with valid values, you should be able to run `mvn generate-sources` which creates all POJOs and DAOs into the target directory you specified.

If you are new to jOOQ, I recommend to read the awesome [jOOQ documentation](http://www.jooq.org/doc/latest/manual/), especially the chapter about
[code generation](http://www.jooq.org/doc/latest/manual/code-generation/).

```
<project>
...your project configuration here...

  <dependencies>
    ...your other dependencies...
    <dependency>
      <groupId>org.jooq</groupId>
      <artifactId>jooq</artifactId>
      <version>3.10.1</version>
    </dependency>
    <dependency>
      <groupId>io.github.jklingsporn</groupId>
      <artifactId>vertx-jooq-future</artifactId>
      <version>3.0.0-BETA</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
          <!-- Specify the maven code generator plugin -->
          <groupId>org.jooq</groupId>
          <artifactId>jooq-codegen-maven</artifactId>
          <version>3.10.1</version>

          <!-- The plugin should hook into the generate goal -->
          <executions>
              <execution>
                  <goals>
                      <goal>generate</goal>
                  </goals>
              </execution>
          </executions>

          <dependencies>
              <dependency>
                  <groupId>mysql</groupId>
                  <artifactId>mysql-connector-java</artifactId>
                  <version>5.1.37</version>
              </dependency>
              <dependency>
                  <groupId>io.github.jklingsporn</groupId>
                  <artifactId>vertx-jooq-generate</artifactId>
                  <version>3.0.0-BETA</version>
              </dependency>
          </dependencies>

          <!-- Specify the plugin configuration.
               The configuration format is the same as for the standalone code generator -->
          <configuration>
              <!-- JDBC connection parameters -->
              <jdbc>
                  <driver>com.mysql.jdbc.Driver</driver>
                  <url>YOUR_JDBC_URL_HERE</url>
                  <user>YOUR_DB_USER_HERE</user>
                  <password>YOUR_DB_PASSWORD_HERE</password>
              </jdbc>

              <!-- Generator parameters -->
              <generator>
                  <name>io.github.jklingsporn.vertx.jooq.generate.future.FutureVertxGenerator</name>
                  <database>
                      <name>org.jooq.util.mysql.MySQLDatabase</name>
                      <includes>.*</includes>
                      <inputSchema>YOUR_INPUT_SCHEMA</inputSchema>
                      <outputSchema>YOUR_OUTPUT_SCHEMA</outputSchema>
                      <unsignedTypes>false</unsignedTypes>
                      <forcedTypes>
                          <!-- Convert tinyint to boolean -->
                          <forcedType>
                              <name>BOOLEAN</name>
                              <types>(?i:TINYINT)</types>
                          </forcedType>
                          <!-- Convert varchar column with name 'someJsonObject' to a io.vertx.core.json.JsonObject-->
                          <forcedType>
                              <userType>io.vertx.core.json.JsonObject</userType>
                              <converter>io.github.jklingsporn.vertx.jooq.shared.JsonObjectConverter</converter>
                              <expression>someJsonObject</expression>
                              <types>.*</types>
                          </forcedType>
                          <!-- Convert varchar column with name 'someJsonArray' to a io.vertx.core.json.JsonArray-->
                          <forcedType>
                              <userType>io.vertx.core.json.JsonArray</userType>
                              <converter>io.github.jklingsporn.vertx.jooq.shared.JsonArrayConverter</converter>
                              <expression>someJsonArray</expression>
                              <types>.*</types>
                          </forcedType>
                      </forcedTypes>
                  </database>
                  <target>
                      <!-- This is where jOOQ will put your files -->
                      <packageName>YOUR_TARGET_PACKAGE_HERE</packageName>
                      <directory>YOUR_TARGET_DIRECTORY_HERE</directory>
                  </target>
                  <generate>
                      <interfaces>true</interfaces>
                      <daos>true</daos>
                      <fluentSetters>true</fluentSetters>
                  </generate>


                  <strategy>
                      <name>io.github.jklingsporn.vertx.jooq.generate.future.FutureGeneratorStrategy</name>
                  </strategy>
              </generator>

          </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```
# gradle

The following code-snippet can be copy-pasted into your `build.gradle` to generate code from your postgresql database schema.

```gradle
buildscript {
    ext {
        vertx_jooq_version = '2.4.1'
        postgresql_version = '42.1.4'
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath "io.github.jklingsporn:vertx-jooq-generate:$vertx_jooq_version"
        classpath "org.postgresql:postgresql:$postgresql_version"
    }
}

import groovy.xml.MarkupBuilder
import org.jooq.util.GenerationTool

import javax.xml.bind.JAXB

group 'your group id'
version 'your project version'

apply plugin: 'java'

dependencies {
    compile "io.github.jklingsporn:vertx-jooq-classic:$vertx_jooq_version"
    testCompile group: 'junit', name: 'junit', version: '4.12'
}

task jooqGenerate {
    doLast() {
        def writer = new StringWriter()
        new MarkupBuilder(writer)
                .configuration('xmlns': 'http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd') {
            jdbc {
                driver('org.postgresql.Driver')
                url('jdbc:postgresql://IP:PORT/DATABASE')
                user('YOUR_USER')
                password('YOUR_PASSWORD')
            }
            generator {
                name('io.github.jklingsporn.vertx.jooq.generate.classic.ClassicVertxGenerator')
                database {
                    name('org.jooq.util.postgres.PostgresDatabase')
                    include('.*')
                    excludes('schema_version')
                    inputSchema('public')
                    includeTables(true)
                    includeRoutines(true)
                    includePackages(false)
                    includeUDTs(true)
                    includeSequences(true)
                }
                generate([:]) {
                    deprecated(false)
                    records(false)
                    interfaces(true)
                    fluentSetters(true)
                    pojos(true)
                    daos(true)
                }
                target() {
                    packageName('io.one.sys.db')
                    directory("$projectDir/src/main/java")
                }
                strategy {
                    name('io.github.jklingsporn.vertx.jooq.generate.classic.ClassicGeneratorStrategy')
                }
            }
        }
        GenerationTool.generate(
                JAXB.unmarshal(new StringReader(writer.toString()), org.jooq.util.jaxb.Configuration.class)
        )
    }
}
```

# programmatic configuration of the code generator
See the [TestTool](https://github.com/jklingsporn/vertx-jooq/blob/master/vertx-jooq-generate/src/test/java/io/github/jklingsporn/vertx/jooq/generate/TestTool.java)
of how to setup the generator programmatically.

# known issues
- The [`VertxCompletableFuture`](https://github.com/cescoffier/vertx-completable-future) is not part of the vertx-core package.
The reason behind this is that it violates the contract of `CompletableFuture#XXXAsync` methods which states that those methods should
run on the ForkJoin-Pool if no Executor is provided. This can not be done, because it would break the threading model of Vertx. Please
keep that in mind. If you can not tolerate this, please use the [`vertx-jooq-classic`](https://github.com/jklingsporn/vertx-jooq/tree/master/vertx-jooq-classic) dependency.
- The generator will omit datatypes that it does not know, e.g. `java.sql.Timestamp`. To fix this, you can easily subclass the generator, handle these types and generate the code using your generator.
 See the `handleCustomTypeFromJson` and `handleCustomTypeToJson` methods in the `AbstractVertxGenerator`.
- Since jOOQ is using JDBC under the hood, the non-blocking fashion is achieved by using the `Vertx.executeBlocking` method.
