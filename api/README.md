# Superheroes API

# Development

* Java 8 properly installed.
* Local memcached to run it and tests.

## Memcached

Download and Install memcached:
- [Linux/Mac OS](https://memcached.org/downloads)
- [Windows](https://commaster.net/content/installing-memcached-windows)

Or run with [Docker](https://www.docker.com/get-started):

`docker run -itd --name memcached -p 11211:11211`
```
connect to memcached: 
$ telnet 127.0.0.1 11211

stop memcached
$ docker ps (to find the container id)
$ docker stop <container_id>
$ docker rm <container_id>
```

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
