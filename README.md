[![GitHub Release](https://img.shields.io/github/v/release/Gadnex/jte-datastar-spring-boot-starter)](https://github.com/Gadnex/jte-datastar-spring-boot-starter/releases)
[![License](https://img.shields.io/github/license/Gadnex/jte-datastar-spring-boot-starter)](https://github.com/Gadnex/jte-datastar-spring-boot-starter/blob/main/LICENSE)
[![Stars](https://img.shields.io/github/stars/Gadnex/jte-datastar-spring-boot-starter?style=flat)](https://github.com/Gadnex/jte-datastar-spring-boot-starter/stargazers)

# jte-datastar-spring-boot-starter

This project is a custom Spring Boot starter project with autoconfiguration.

It is used to render HTML fragments using the Java Template Engine (JTE) and emitting
the HTML as Datastar MergeFragments Server Sent Events (SSE). It also emits the other
Datastar event types.

## Using the starter

The following needs to be done to use the starter on your project.

### Add the required dependencies

**Maven**
```xml
    <dependency>
        <groupId>gg.jte</groupId>
        <artifactId>jte-spring-boot-starter-3</artifactId>
        <version>3.2.1</version>
    </dependency>
    <dependency>
        <groupId>io.github.gadnex</groupId>
        <artifactId>jte-datastar-spring-boot-starter</artifactId>
        <version>${jteDatastarVersion}</version>
    </dependency>
```

**Gradle**
```groovy
    plugins {
        id 'gg.jte.gradle' version '3.2.1'
    }

    dependencies {
        implementation 'gg.jte:jte-spring-boot-starter-3:3.2.1'
        implementation 'io.github.gadnex:jte-datastar-spring-boot-starter:${jteDatastarVersion}'
    }
```

### Controller class

SSE events need to be emitted from a separate thread, therefore we:
- Create an SSE emitter
- Run the code to emit Datastar SSE events on a separate thread
- Return the emitter to the browser to start listening for SSE events before events start being emitted

```java
    private static final ExecutorService EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    @Autowired
    private Datastar datastar;

    @GetMapping("do-something")
    public SseEmitter doSomething() {
        SseEmitter sseEmitter = new SseEmitter();
        EXECUTOR.execute(
                () -> {
                    // Perform business logic here
                    var something = "Business logic result";
                    datastar.mergeFragments(sseEmitter)
                            .template("TemplateName")
                            .attribute("something", something)
                            .emit();
                    sseEmitter.complete();
                });
        return sseEmitter;
    }
```

After we know that we are done emitting events to the SSE emitter,
we should complete the emitter to close the HTTP connection from the server side. 

In scenarios where we plan to send multiple events to the SSE emitter over time,
we usually keep the emitter in a data structure like a Set.

```java
    private static final Set<SseEmitter> connections = new HashSet<>();

    @GetMapping(value = "connect", headers = "Datastar-Request")
    public SseEmitter connect() {
        SseEmitter sseEmitter = new SseEmitter(-1L);
        sseEmitter.onError(
                (error) -> {
                    connections.remove(sseEmitter);
                });
        sseEmitter.onCompletion(
                () -> {
                    connections.remove(sseEmitter);
                });
        sseEmitter.onTimeout(
                () -> {
                    connections.remove(sseEmitter);
                });
        connections.add(sseEmitter);
        return sseEmitter;
    }
```
Note that the SseEmitter was constructed with a timeoout of **-1L**.
This special value means that the SseEmitter will not time out on the server.
We also remove the SseEmitter from the data structure on error, completion and timeout. 

If we want to send future events to the SSE emitter, we should not complete
the emitter.

## Datastar emitter

The Datastar plugin supports all Datastar event types:
- MergeFragments
- RemoveFragments
- MergeSignals
- RemoveSignals
- ExecuteScript