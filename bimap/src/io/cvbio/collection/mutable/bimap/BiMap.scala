package io.cvbio.collection.mutable.bimap

import scala.collection.{Factory, StrictOptimizedIterableOps, StrictOptimizedMapOps, mutable}

/** A mutable bidirectional map. There is a 1:1 mapping between keys and values. */
class BiMap[K, V]() extends mutable.Map[K, V]
  with mutable.MapOps[K, V, mutable.Map, BiMap[K, V]]
  with StrictOptimizedMapOps[K, V, mutable.Map, BiMap[K, V]]
  with StrictOptimizedIterableOps[(K, V), mutable.Iterable, BiMap[K, V]] {
  private val forward = mutable.HashMap.empty[K, V]
  private val reverse = mutable.HashMap.empty[V, K]

  /** The class name of this map. */
  override def className = "BiMap"

  /** Adds a key-value pair to the map. */
  override def addOne(elem: (K, V)): this.type = {
    add(elem._1, elem._2)
    this
  }

  /** Adds a key-value pair to the map. */
  def add(key: K, value: V): Unit = {
    removeKey(key)
    removeValue(value)
    val _ = forward.put(key, value)
    val _ = reverse.put(value, key)
  }

  /** Clear this map. */
  override def clear(): Unit = {
    forward.clear()
    reverse.clear()
  }

  /** Concatenate this map with another set of key-value pairs. */ // Implemented to further refine the return type
  override def concat[V2 >: V](suffix: IterableOnce[(K, V2)]): BiMap[K, V2] = {
    strictOptimizedConcat(suffix, BiMap.newBuilder)
  }

  /** Checks if the map contains the given key. */
  def containsKey(key: K): Boolean = forward.contains(key)

  /** Checks if the map contains the given value. */
  def containsValue(value: V): Boolean = reverse.contains(value)

  /** An empty [[BiMap]]. */ // Implemented to further refine the return type
  override def empty: BiMap[K, V] = new BiMap[K, V]

  /** Transform this map and flatten in one level with a function on each key-value pair. */
  override def flatMap[K2, V2](f: ((K, V)) => IterableOnce[(K2, V2)]): BiMap[K2, V2] = {
    strictOptimizedFlatMap(BiMap.newBuilder, f)
  }

  /** Get a value associated with a given key. */
  override def get(key: K): Option[V] = forward.get(key)

  /** An iterator of key-value pairs contained in this map. */
  def iterator: Iterator[(K, V)] = forward.iterator

  /** Transform this map with a function on each key-value pair. */
  override def map[K2, V2](f: ((K, V)) => (K2, V2)): BiMap[K2, V2] = strictOptimizedMap(BiMap.newBuilder, f)

  /** Get a key associated with the given value. */
  def keyFor(value: V): Option[K] = reverse.get(value)

  /** The keys in this map. */
  override def keys: Iterable[K] = forward.keys

  /** Remove a key and its associated value from the map. */
  def removeKey(key: K): Boolean = {
    valueFor(key) match {
      case Some(value) =>
        forward.remove(key)
        reverse.remove(value)
        true
      case None => false
    }
  }

  /** Remove a value and its associated key from the map. */
  def removeValue(value: V): Boolean = {
    keyFor(value) match {
      case Some(key) =>
        forward.remove(key)
        reverse.remove(value)
        true
      case None => false
    }
  }

  /** The number of key-value pairs in this map. */
  override def size: Int = forward.size

  /** Remove a key and its associated value from the map. */
  override def subtractOne(elem: K): this.type = {
    removeKey(elem)
    this
  }

  /** Get a value associated with a given key. */
  def valueFor(key: K): Option[V] = get(key)

  /** The values in this map. */
  override def values: Iterable[V] = reverse.keys

  /** Build a [[BiMap]] from a specific collection. */
  override protected def fromSpecific(coll: IterableOnce[(K, V)]): BiMap[K, V] = BiMap.from(coll)

  /** Build a specific [[BiMap]] builder. */
  override protected def newSpecificBuilder: mutable.Builder[(K, V), BiMap[K, V]] = BiMap.newBuilder
}

/** The companion object for [[BiMap]]. */
object BiMap {

  /** Create a bidirectional map from a collection of key-value pairs. */
  def apply[K, V](kvs: (K, V)*): BiMap[K, V] = from(kvs)

  /** Create an empty bidirectional map. */
  def empty[K, V]: BiMap[K, V] = new BiMap[K, V]

  /** Create a bidirectional map from a collection of key-value pairs. */
  def from[K, V](it: IterableOnce[(K, V)]): BiMap[K, V] = it match {
    case it: Iterable[_] if it.isEmpty => empty[K, V]
    case map: BiMap[K, V]              => map
    case it                            => new BiMap[K, V].addAll(it)
  }

  /** Create a new growable builder for this bidirectional map. */
  def newBuilder[K, V]: mutable.Builder[(K, V), BiMap[K, V]] = new mutable.GrowableBuilder[(K, V), BiMap[K, V]](empty)

  import scala.language.implicitConversions

  /** Create a new factory for this bidirectional map. */
  implicit def toFactory[K, V](self: this.type): Factory[(K, V), BiMap[K, V]] = {
    new Factory[(K, V), BiMap[K, V]] {
      def fromSpecific(it: IterableOnce[(K, V)]): BiMap[K, V] = self.from(it)
      def newBuilder: mutable.Builder[(K, V), BiMap[K, V]] = self.newBuilder[K, V]
    }
  }
}
