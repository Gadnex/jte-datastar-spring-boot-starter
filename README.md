[![GitHub Release](https://img.shields.io/github/v/release/Gadnex/jte-datastar-spring-boot-starter)](https://github.com/Gadnex/jte-datastar-spring-boot-starter/releases)
[![License](https://img.shields.io/github/license/Gadnex/jte-datastar-spring-boot-starter)](https://github.com/Gadnex/jte-datastar-spring-boot-starter/blob/main/LICENSE)
[![Stars](https://img.shields.io/github/stars/Gadnex/jte-datastar-spring-boot-starter?style=flat)](https://github.com/Gadnex/jte-datastar-spring-boot-starter/stargazers)

# jte-datastar-spring-boot-starter

This project is a custom Spring Boot starter project with autoconfiguration.

It is used to render HTML elements using the Java Template Engine (JTE) and emitting
the HTML as Datastar PatchElements Server Sent Events (SSE). It also emits Datastar PatchSignals events.

## Using the starter

The following needs to be done to use the starter on your project.

### Spring Boot version

Version **0.3.x** and later of **jte-datastar-spring-boot-starter** is based on Spring Boot 4.x and does not work with Spring Boot 3.x.

For Spring Boot 3.x support, please use version **0.2.5**.

### Add the required dependencies

**Maven**
```xml
    <dependency>
        <groupId>gg.jte</groupId>
        <artifactId>jte-spring-boot-starter-4</artifactId>
        <version>3.2.4</version>
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
        id 'gg.jte.gradle' version '3.2.4'
    }

    dependencies {
        implementation 'gg.jte:jte-spring-boot-starter-4:3.2.4'
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
                    datastar.patchElements(sseEmitter)
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

### Configuration

The Datastar Spring Bean is autoconfigured without any required configuration.

You can explicitly enable/disbale the autoconfiguration of the bean using the application property:

```properties
datastar.sse.enabled=true // enabled

datastar.sse.enabled=false // disabled
```

## CSP Mode

Datastar version 1.0.3 introduced a new [CSP mode](https://data-star.dev/reference/security#csp-mode) feature.
The JTE Datastar Spring Boot starter adds support for CSP mode by automatically generating a nonce value and adding it to the HTTP request with the key `cspNonce`.
It also adds a `@param String nonce` to your Spring MVC model as an `@ControllerAdvice` `@ModelAttribute("nonce")`.

CSP mode is not enabled by default, but you can enable/disbale the autoconfiguration of the bean using the application property:

```properties
datastar.csp.enabled=true // enabled

datastar.csp.enabled=false // disabled
```

In your JTE layout template with the `html` tag add the following:

```html
@param String nonce

<html data-nonce="${nonce}">
```

## CSP Response Header

In order to enable CSP in your browser, it is recommended to use an HTTP response header instead of a `meta` tag in the HTML `head` tag.

This is your own responsibility, but the starter does offer a filter that can be used if no Spring Security is enabled.

The CSP response header is not enabled by default, but you can enable/disbale the autoconfiguration of the bean using the application property:

```properties
datastar.csp.csp-header=true // enabled

datastar.csp.csp-header=false // disabled
```

When using Spring Security, the fallback filter is automatically disabled.
You can configure the CSP header in your `SecurityFilterChain` using a `HeaderWriter`:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.headers(headers -> headers
        .addHeaderWriter((request, response) -> {
            String nonce = (String) request.getAttribute(CspRequestNonceFilter.NONCE_ATTRIBUTE);
            if (nonce != null && !response.containsHeader("Content-Security-Policy")) {
                response.setHeader(
                    "Content-Security-Policy",
                    "default-src 'self'; script-src 'self' 'nonce-" + nonce + "'; trusted-types datastar; require-trusted-types-for 'script';"
                );
            }
        })
    );
    return http.build();
}
```