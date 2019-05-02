#  Superhoroes API

# Development

* Java 8 properly installed.
* Local memcached to run it and tests.

## Command line
  
To build the project, run the gradle wrapper:

```bash
 ./gradlew build
```

_memcached is needed._

For running tests:

```
 ./gradlew check
```

You can run it in DEV using: 
```
 ./gradlew bootRun
```

## Idea

Just import the project as a gradle project. You need to run IntelliJ Idea using Java 8 or a newer version. Refer
to [this article](https://intellij-support.jetbrains.com/hc/en-us/articles/206544879-Selecting-the-JDK-version-the-IDE-will-run-under) for help if you need to change it.
