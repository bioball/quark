package quark

import scala.reflect.ClassTag
import scala.util.Try

/**
 * Created by danielchao on 2/6/17.
 *
 * A QueryRef is a pointer to a value. It is either a lookup by a class field, or a raw value in itself.
 */
object QueryRefs {

  object implicits {
    implicit def toRawQueryRef[T](value: T): QueryRef[T] = QueryRefs.RawQueryRef(value)
  }

  case class PathQueryRef[T: ClassTag](path: String) extends QueryRef[T] {

    implicitly[ClassTag[T]]

    def getValue[M](target: M): Option[T] = Try({
      val field = target
        .getClass
        .getDeclaredField(path)
      field.setAccessible(true)
      field.get(target).asInstanceOf[T]
    }).toOption

  }

  case class RawQueryRef[T](value: T) extends QueryRef[T] {
    def getValue[M](target: M) = Some(value)
  }

  trait QueryRef[T] {
    def ====(ref: QueryRef[T]) = Equals(this, ref)
    def <(ref: QueryRef[T])(implicit ordering: Ordering[T]) = LessThan(this, ref)
    def >(value: QueryRef[T])(implicit ordering: Ordering[T]) = GreaterThan(this, value)
    def >=(value: QueryRef[T])(implicit ordering: Ordering[T]) = GreaterThanOrEquals(this, value)
    def <=(value: QueryRef[T])(implicit ordering: Ordering[T]) = LessThanOrEquals(this, value)

    def getValue[M](target: M): Option[T]
  }

}

