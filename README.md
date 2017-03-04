# Quark

Serializable, generic queries!

This library allows you to write querying functions that are fully java serializable. For example, you on one end:

```scala
import com.bioball.quark.{Query, QueryModel}
import com.bioball.quark.QueryRefs.implicits._
import some.common.package.User

class UserModel extends QueryModel {
  def firstName = prop[String]("firstName")
  def lastName = prop[String]("lastName")
  def age = prop[Int]("age")
}

implicit val model = new UserModel()

// this query can be serialized.
val query = Query[User, UserModel]()
  .filter(_.firstName === "Bob")

sendQueryToRemoteHost(query)
```

And on the receiving end:

```scala
import com.bioball.quark.Query
import some.common.package.User

val users = Seq(
  User("Bob", "Myers", 50),
  User("Sandra", "Wellington", 15)
)

val query: Query = getQuerySomehow
val result = query.run(people)
// => Seq(User("Bob", "Myers", 50))
sendBackToClient(result)
```


## Why would I even need this?

In most cases, you probably don't need it. This library heavily depends on both applications having packages in the same classpaths. One particular use-case is in applications that keep data in memory (e.g. sometimes used in event-sourcing), and don't keep values in a database.
