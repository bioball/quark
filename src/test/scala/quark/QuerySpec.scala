package quark

import org.scalatest.{FunSpec, MustMatchers}
import quark.QueryRefs.{PathQueryRef, RawQueryRef}
import quark.QueryRefs.implicits._

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
        val ref = new RawQueryRef(3)
        // getValue for raw values don't use the class instance for lookups, so it's ignored.
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

      it("should be able to run Equals queries") {
        query.filter((q) => q.firstName ==== q.lastName).run(people) must equal(Seq(Person("Jacob", "Jacob", 25)))
        query.filter(_.firstName ==== "Bob").run(people) must equal(Seq(Person("Bob", "Kane", 19)))
      }

      it("should be able to run GreaterThan queries") {
        query.filter(_.age > 30).run(people) must equal(Seq(Person("Jane", "Goodall", 38)))
      }

      it("should be able to run LessThan queries") {
        query.filter(_.age < 20).run(people) must equal(Seq(Person("Bob", "Kane", 19)))
      }
    }
  }
}
