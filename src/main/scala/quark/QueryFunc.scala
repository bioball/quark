package quark

import quark.QueryRefs.QueryRef

/**
 * Created by danielchao on 2/6/17.
 */
trait QueryFunc[TRefValue] {

  val left: QueryRef[TRefValue]
  val right: QueryRef[TRefValue]

  def compare(lhs: TRefValue, rhs: TRefValue): Boolean
  def apply[TModel](target: TModel): Boolean = compareRefs(target)(compare)

  def compareRefs[TModel](target: TModel)(f: (TRefValue, TRefValue) => Boolean) = (for {
    leftVal <- left.getValue(target)
    rightVal <- right.getValue(target)
  } yield f(leftVal, rightVal)).getOrElse(false)

}

case class Equals[TRefValue](
  left: QueryRef[TRefValue],
  right: QueryRef[TRefValue]
) extends QueryFunc[TRefValue] {
  def compare(lhs: TRefValue, rhs: TRefValue) = lhs == rhs
}

case class GreaterThan[TRefValue](
  left: QueryRef[TRefValue],
  right: QueryRef[TRefValue]
)(implicit ordering: Ordering[TRefValue]) extends QueryFunc[TRefValue] {
  import ordering._
  def compare(lhs: TRefValue, rhs: TRefValue) = lhs > rhs
}

case class LessThan[TRefValue](
  left: QueryRef[TRefValue],
  right: QueryRef[TRefValue]
)(implicit ordering: Ordering[TRefValue]) extends QueryFunc[TRefValue] {
  import ordering._
  def compare(lhs: TRefValue, rhs: TRefValue) = lhs < rhs
}

case class GreaterThanOrEquals[TRefValue](
  left: QueryRef[TRefValue],
  right: QueryRef[TRefValue]
)(implicit ordering: Ordering[TRefValue]) extends QueryFunc[TRefValue] {
  import ordering._
  def compare(lhs: TRefValue, rhs: TRefValue) = lhs >= rhs
}

case class LessThanOrEquals[TRefValue](
  left: QueryRef[TRefValue],
  right: QueryRef[TRefValue]
)(implicit ordering: Ordering[TRefValue]) extends QueryFunc[TRefValue] {
  import ordering._
  def compare(lhs: TRefValue, rhs: TRefValue) = lhs <= rhs
}
