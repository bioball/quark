package quark

/**
 * Created by danielchao on 2/6/17.
 */
case class Query[M, T <: QueryModel](
  queries: Seq[QueryFunc[_]] = Nil,
  limit: Option[Int] = None
)(implicit queryModel: T) {

  def filter(fn: (T) => QueryFunc[_]) = copy[M, T](queries = queries ++ Seq(fn(queryModel)))

  def limitTo(number: Int) = copy[M, T](limit = Some(number))

  def runFilters(target: M) = queries.forall(query => query(target))

  def run(targets: Seq[M]) = targets.filter(runFilters)

}
