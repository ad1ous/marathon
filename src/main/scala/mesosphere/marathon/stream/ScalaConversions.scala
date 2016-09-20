package mesosphere.marathon
package stream

import java.util

import scala.collection.immutable.{ IndexedSeq, Seq, Set }
import scala.language.implicitConversions

trait ScalaConversions {

  implicit class RichCollection[T](collection: util.Collection[T]) extends Traversable[T] {
    override def foreach[U](f: (T) => U): Unit = collection.stream().foreach(f)
    override def toSeq: Seq[T] = ScalaConversions.toSeq(collection)
    override def toIndexedSeq: IndexedSeq[T] = ScalaConversions.toIndexedSeq(collection)
    override def toSet[B >: T]: Set[B] = ScalaConversions.toSet(collection)
  }

  implicit class RichMap[K, V](map: util.Map[K, V]) extends Traversable[(K, V)] {
    override def foreach[U](f: ((K, V)) => U): Unit =
      map.entrySet().stream().foreach(entry => f(entry.getKey -> entry.getValue))
    def toMap: Map[K, V] = ScalaConversions.toMap(map)
  }

  def toSeq[T](collection: util.Collection[T]): Seq[T] =
    collection.stream().collect(Collectors.seq[T])

  def toIndexedSeq[T](collection: util.Collection[T]): IndexedSeq[T] =
    collection.stream().collect(Collectors.indexedSeq[T])

  def toSet[T, B >: T](collection: util.Collection[T]): Set[B] =
    collection.stream().collect(Collectors.set[B])

  def toMap[K, V](map: util.Map[K, V]): Map[K, V] =
    map.entrySet().stream().collect(Collectors.map[K, V])

  implicit def toTraversableOnce[T](enum: util.Enumeration[T]): RichEnumeration[T] = new RichEnumeration[T](enum)
  implicit def toTraversable[T](it: util.Iterator[T]): RichIterator[T] = new RichIterator[T](it)
}

object ScalaConversions extends ScalaConversions