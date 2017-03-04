package quark

import quark.QueryRefs.QueryRef

/**
 * Created by danielchao on 2/6/17.
 */
sealed trait QueryPart[T] {
  def compare[A](target: A): Boolean
  def apply[A](target: A): Boolean = compare(target)

  /**
   * Utility function. Takes two options, if either are None, return false. If both are true, run the compare func.
   */
  def compareOpts[A](leftOpt: Option[A], rightOpt: Option[A])(compare: (A, A) => Boolean) = (for {
    left <- leftOpt
    right <- rightOpt
  } yield compare(left, right)).getOrElse(false)

}

case class Equals[T](left: QueryRef[T], right: QueryRef[T]) extends QueryPart[T] {
  def compare[A](target: A) = compareOpts(left.getValue(target), right.getValue(target)) { (left, right) =>
    left == right
  }
}

case class GreaterThan[T](left: QueryRef[T], right: QueryRef[T])(implicit ordering: Ordering[T]) extends QueryPart[T] {
  import ordering._
  def compare[A](target: A) = compareOpts(left.getValue(target), right.getValue(target)) { (left, right) =>
    left > right
  }
}

case class LessThan[T](left: QueryRef[T], right: QueryRef[T])(implicit ordering: Ordering[T]) extends QueryPart[T] {
  import ordering._
  def compare[A](target: A) = compareOpts(left.getValue(target), right.getValue(target)) { (left, right) =>
    left < right
  }
}

case class GreaterThanOrEquals[T](left: QueryRef[T], right: QueryRef[T])(implicit ordering: Ordering[T]) extends QueryPart[T] {
  import ordering._
  def compare[A](target: A) = compareOpts(left.getValue(target), right.getValue(target)) { (left, right) =>
    left >= right
  }
}

case class LessThanOrEquals[T](left: QueryRef[T], right: QueryRef[T])(implicit ordering: Ordering[T]) extends QueryPart[T] {
  import ordering._
  def compare[A](target: A) = compareOpts(left.getValue(target), right.getValue(target)) { (left, right) =>
    left <= right
  }
}
