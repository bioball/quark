package quark

import org.scalatest.{MustMatchers, FunSpec, Matchers}
import quark.QueryRefs.{RawQueryRef, PathQueryRef}

/**
 * Created by danielchao on 2/6/17.
 */
class QuerySpec extends FunSpec with MustMatchers {

  case class Person(firstName: String, lastName: String, age: Int)

  describe("Quark") {

    describe("Query Refs") {
      it("path query refs should be able to look up their queries") {
        case class HasFooProp(foo: Int)
        val ref = new PathQueryRef[Int]("foo")
        ref.getValue(HasFooProp(3)) mustBe Some(3)
      }

      it("value query refs should return correct values") {
        // getValue for raw values don't use the class instance for lookups, so it's ignored.
        val ref = new RawQueryRef(3)
        ref.getValue(Unit) mustBe Some(3)
      }
    }

    describe("queries") {

      class PersonModel extends QueryModel {
        def firstName = prop[String]("firstName")
        def lastName = prop[String]("lastName")
        def age = prop[Int]("age")
      }

      implicit val model = new PersonModel()

      val query = new Query[Person, PersonModel]()

      val people = Seq(
        Person("Bob", "Kane", 19),
        Person("Jacob", "Jacob", 25),
        Person("Jane", "Goodall", 38)
      )

      it("should be able to run equals queries") {

        import QueryRefs.implicits._

        query.filter((q) => q.firstName ==== q.lastName).run(people) must equal(Seq(Person("Jacob", "Jacob", 25)))
        query.filter((q) => q.firstName ==== "Bob").run(people) must equal(Seq(Person("Bob", "Kane", 19)))

      }


    }
  }
}
