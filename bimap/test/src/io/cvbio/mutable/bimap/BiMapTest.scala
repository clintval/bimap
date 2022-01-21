package io.cvbio.mutable.bimap

import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers

/** Unit tests for [[BiMap]]. */
class BiMapTest extends AnyFlatSpec with Matchers {
  private case class CustomHashCode(code: Int) { override def hashCode(): Int = code }

  "BiMap.className" should "be 'BiMap'" in {
    BiMap.empty.className shouldBe "BiMap"
  }

  "BiMap.addOne" should "add a key-value to the map" in {
    val map = new BiMap[String, String]()
    map.addOne("key1" -> "val1")
    map.containsKey("key1") shouldBe true
    map.containsValue("val1") shouldBe true
    map should have size 1
  }

  "BiMap.add" should "add key-values to the map and remove any that already exists" in {
    val map = new BiMap[String, String]()
    map.add("key1", "val1")
    map.containsKey("key1") shouldBe true
    map.containsValue("val1") shouldBe true
    map should have size 1

    map.add("key2", "val1")
    map.containsKey("key1") shouldBe false
    map.containsKey("key2") shouldBe true
    map.containsValue("val1") shouldBe true
    map should have size 1

    map.add("key2", "val2")
    map.containsKey("key1") shouldBe false
    map.containsKey("key2") shouldBe true
    map.containsValue("val1") shouldBe false
    map.containsValue("val2") shouldBe true
    map should have size 1
  }

  "BiMap.clear" should "clear the map" in {
    val map = new BiMap[String, String]()
    map.addOne("key1" -> "val1")
    map.containsKey("key1") shouldBe true
    map.containsValue("val1") shouldBe true
    map should have size 1
    map.clear()
    map should have size 0
  }

  "BiMap.concat" should "concatenate additional key-value pairs while keeping the correct type" in {
    val map = new BiMap[String, String]()
    map.add("key1", "val1")
    map.containsKey("key1") shouldBe true
    map.containsValue("val1") shouldBe true
    map should have size 1

    val concatenated = map.concat(Seq("key2" -> "val2"))
    map should have size 1
    concatenated should have size 2
    concatenated mustBe a[BiMap[_, _]]
  }

  "BiMap.containsKey" should "return true if it contains the key, false otherwise" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.containsKey(1) shouldBe true
    map.containsKey(2) shouldBe true
    map.containsKey(3) shouldBe false
  }

  "BiMap.containsValue" should "return true if it contains the value, false otherwise" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.containsValue("1") shouldBe true
    map.containsValue("2") shouldBe true
    map.containsValue("3") shouldBe false
  }

  "BiMap.empty(class)" should "return an empty BiMap" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map should have size 1
    map.empty should have size 0
  }

  "BiMap.flatMap" should "map a function and flatten one level and return a BiMap" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    val actual   = map.flatMap { case (key, value) => Seq(key + 1 -> value) }
    val expected = new BiMap[Int, String]()
    expected.add(2, "1")
    actual mustBe a[BiMap[_, _]]
    actual shouldBe expected
  }

  "BiMap.get" should "either get a value for a key if it exists or return None" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.get(1).value shouldBe "1"
    map.get(2) shouldBe None
  }

  "BiMap.iterator" should "return the key-value pairs in an iterator" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.iterator.toSeq shouldBe Seq(1 -> "1")
  }

  "BiMap.map" should "map a function" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    val actual   = map.map { case (key, value) => key + 1 -> value }
    val expected = new BiMap[Int, String]()
    expected.add(2, "1")
    actual mustBe a[BiMap[_, _]]
    actual shouldBe expected
  }

  "BiMap.keyFor" should "either get a key for a value if it exists or return None" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.keyFor("1").value shouldBe 1
    map.keyFor("2") shouldBe None
  }

  "BiMap.keys" should "return the keys of the map" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.keys.toSeq should contain theSameElementsInOrderAs Seq(1, 2)
  }

  "BiMap.removeKey" should "remove the key and associated value from the map, and return true only if it exists" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.size shouldBe 2
    map.removeKey(0) shouldBe false
    map.size shouldBe 2
    map.removeKey(1) shouldBe true
    map.size shouldBe 1
    map.removeKey(2) shouldBe true
    map.isEmpty shouldBe true
  }

  "BiMap.removeValue" should "remove the value and associated key from the map, and return true only if it exists" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.size shouldBe 2
    map.removeValue("0") shouldBe false
    map.size shouldBe 2
    map.removeValue("1") shouldBe true
    map.size shouldBe 1
    map.removeValue("2") shouldBe true
    map.isEmpty shouldBe true
  }

  "BiMap.size" should "return the size of the BiMap" in {
    val map = new BiMap[Int, String]()
    map.isEmpty shouldBe true
    map.add(1, "1")
    map.size shouldBe 1
    map.add(2, "2")
    map.size shouldBe 2
  }

  "BiMap.subtractOne" should "remove the key and associated value from the map" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.size shouldBe 2
    map.subtractOne(0)
    map.size shouldBe 2
    map.subtractOne(1)
    map.size shouldBe 1
    map.subtractOne(2)
    map.isEmpty shouldBe true
  }

  "BiMap.valueFor" should "either get a value for a key if it exists or return None" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.valueFor(1).value shouldBe "1"
    map.valueFor(2) shouldBe None
  }

  "BiMap.values" should "return the values of the map" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    map.values.toSeq should contain theSameElementsInOrderAs Seq("1", "2")
  }

  "BiMap" should "handle hash collisions" in {
    val map = new BiMap[CustomHashCode, Int]()

    val obj1 = CustomHashCode(1)
    val obj2 = CustomHashCode(2)
    val obj3 = CustomHashCode(2)

    map.add(obj1, obj1.code)
    map.add(obj2, obj2.code)
    map.add(obj3, obj3.code) // collision!

    map.size shouldBe 2
  }

  "BiMap.apply" should "build a BiMap from a variadic set of key-value pairs" in {
    val map = BiMap[Int, String](1 -> "1", 2 -> "2")
    map should have size 2
    map.toSeq should contain theSameElementsInOrderAs Seq(1 -> "1", 2 -> "2")
  }

  "BiMap.empty(companion object)" should "build an empty BiMap" in {
    BiMap.empty shouldBe new BiMap()
  }

  "BiMap.from" should "build an empty BiMap from an empty collection" in {
    BiMap.from(Seq.empty) shouldBe new BiMap()
  }

  it should "return a BiMap if a BiMap is itself the iterable" in {
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    BiMap.from(map) shouldBe map
  }

  it should "build a BiMap from a non-empty iterable of key-value pairs" in {
    val seq = Seq(1 -> "1", 2 -> "2")
    val map = new BiMap[Int, String]()
    map.add(1, "1")
    map.add(2, "2")
    BiMap.from(seq) shouldBe map
  }

  "BiMap.toFactory.fromSpecific" should "return build a BiMap from a specific collection" in {
    val map = BiMap.toFactory[Int, String](BiMap).fromSpecific(Seq((1, "1"), (2, "2")))
    map shouldBe BiMap(1 -> "1", 2 -> "2")
  }

  "BiMap.toFactory.newBuilder" should "create a builder that allows one to build up a BiMap" in {
    val builder = BiMap.toFactory[Int, String](BiMap).newBuilder
    builder.addOne(1 -> "1")
    builder.addOne(2 -> "2")
    builder.result() shouldBe BiMap(1 -> "1", 2 -> "2")
  }
}
