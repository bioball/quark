package quark

/**
 * Created by danielchao on 2/6/17.
 *
 * The root query interface. The query interface is fully serializable and deserializable.
 */
case class Query[TModel, TQueryModel <: QueryModel](
  queries: Seq[QueryFunc[_]] = Nil,
  limit: Option[Int] = None
)(implicit queryModel: TQueryModel) {

  def filter(fn: (TQueryModel) => QueryFunc[_]): Query[TModel, TQueryModel] =
    copy[TModel, TQueryModel](queries = queries ++ Seq(fn(queryModel)))

  def limitTo(number: Int) = copy[TModel, TQueryModel](limit = Some(number))

  def runFilters(target: TModel): Boolean = queries.forall(query => {
    query(target)
  })

  def limitTargets(targets: Seq[TModel]) = limit.map(targets.take).getOrElse(targets)

  def run(targets: Seq[TModel]): Seq[TModel] = limitTargets(targets).filter(runFilters)

}
