# jte-datastar-spring-boot-starter

This project is a custom Spring Boot starter project with auto configuration.

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
        <version>3.1.16</version>
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
        id 'gg.jte.gradle' version '3.1.16'
    }

    dependencies {
        implementation 'gg.jte:jte-spring-boot-starter-3:3.1.16'
        implementation 'io.github.gadnex:jte-datastar-spring-boot-starter:${jteDatastarVersion}'
    }
```

### Controller class

SSE events need to be emitted from a separate thread, therefore we:
- Create an SSE emitter
- Run the code to emit Datastar SSE events on a separate thread
- Return the emitter to the browser to start listening for SSE events before events star being emitted

```java
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    @Autowired
    private Datastar datastar;

    @GetMapping("do-something")
    public SseEmitter doSomething() {
        SseEmitter sseEmitter = new SseEmitter();
        EXECUTOR.execute(
                () -> {
                    try {
                        datastar.mergeFragments(sseEmitter)
                                .template("TemplateName")
                                .emit();
                    } catch (EmitException ex) {
                    } finally {
                        sseEmitter.complete();
                    }
                });
        return sseEmitter;
    }
```

In this case we did not do anything in the exception handler. 
In scenarios where we plan to send multiple events to the SSE emitter over time,
we usually keep the emitter in a data structure.
In such a case we would remove the SSE emitter from the data structure to
prevent future events from being sent to this emitter.

If we want to send future events to the SSE emitter, we should not complete
the emitter.

## Datastar emitter

The Datastar plugin supports all Datastar event types:
- MergeFragments
- RemoveFragments
- MergeSignals
- RemoveSignals
- ExecuteScript