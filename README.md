# API Deprecation Header Companion

Gutter warning icon on any Java/Kotlin Spring MVC endpoint method
marked `@Deprecated` whose body never sets a deprecation-related HTTP
response header — RFC 9745 `Deprecation`, RFC 8594 `Sunset`, or the
common non-standard `X-Deprecated`. The `@Deprecated` annotation only
warns people reading or compiling the source; it tells real API
consumers (a mobile app, a third-party integration, another team's
service) nothing at all, since nothing in the actual HTTP response
signals it.

## Why it exists

Marking an endpoint `@Deprecated` is a source-level, IDE-only signal.
Real API consumers never see Javadoc or compiler warnings — they only
see what's actually on the wire. RFC 9745 and RFC 8594 exist precisely
to close that gap, but nothing in the IDE flags a deprecated endpoint
that forgot to actually set the header.

## Why built this way

- **100% static text/PSI analysis** — matches the mapping annotation,
  the `@Deprecated` annotation, and a broad set of header-setting
  signals by simple text, so it works whether the real Spring classes
  are on the classpath or not.
- **Deliberately broad on what counts as "signaled"** — any of
  `Deprecation`, `Sunset`, or `X-Deprecated` set anywhere in the body is
  accepted; this plugin doesn't validate the header's value is a real
  date or otherwise RFC-compliant, only that a real attempt exists.

## v0.1 scope — stated honestly, not exhaustively

Only checks the method's own body — if the header is set centrally (a
filter, an interceptor, an aspect), this can't see that and will still
flag the endpoint. Spring MVC only, not JAX-RS or other frameworks.

## Usage

Open any Java/Kotlin Spring controller. A `@Deprecated` endpoint with
no deprecation header signal in its body shows a warning icon on the
method name.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
