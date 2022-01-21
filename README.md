# bimap

[![Unit Tests](https://github.com/clintval/bimap/actions/workflows/unit-tests.yml/badge.svg)](https://github.com/clintval/bimap/actions/workflows/unit-tests.yml)
[![Coverage Status](https://codecov.io/gh/clintval/bimap/branch/main/graph/badge.svg)](https://codecov.io/gh/clintval/bimap)
[![Language](https://img.shields.io/badge/language-scala-c22d40.svg)](https://www.scala-lang.org/)

A modern bidirectional map in Scala

```scala
val map = BiMap("K" -> "V")
map.containsKey("K") shouldBe true
map.containsValue("K") shouldBe true
map.containsKey("V") shouldBe true
map.containsValue("V") shouldBe true
map should have size 1
```

![Pacific City, Oregon](.github/img/cover.jpg)

#### Build with this Package using Mill

```scala
ivyDeps ++ Agg(ivy"io.cvbio::bimap::0.0.1")
```

#### Build with this Package using SBT

```scala
libraryDependencies += "io.cvbio" %% "bimap" % "0.0.1"
```
