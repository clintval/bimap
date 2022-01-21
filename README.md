# bimap

[![Unit Tests](https://github.com/clintval/bimap/actions/workflows/unit-tests.yml/badge.svg)](https://github.com/clintval/bimap/actions/workflows/unit-tests.yml)
[![Coverage Status](https://codecov.io/gh/clintval/bimap/branch/main/graph/badge.svg)](https://codecov.io/gh/clintval/bimap)
[![Language](https://img.shields.io/badge/language-scala-c22d40.svg)](https://www.scala-lang.org/)

A modern bidirectional map in Scala

![Pacific City, Oregon](.github/img/cover.jpg)

```scala
val map = BiMap(1 -> "1", 2 -> "2")
map.containsKey(1) shouldBe true
map.containsKey(2) shouldBe true
map.containsValue("1") shouldBe true
map.containsValue("2") shouldBe true
map should have size 2
```

#### Build with this Package using Mill

```scala
ivyDeps ++ Agg(ivy"io.cvbio::bimap::0.0.1")
```

#### Build with this Package using SBT

```scala
libraryDependencies += "io.cvbio" %% "bimap" % "0.0.1"
```
