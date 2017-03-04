package quark

import quark.QueryRefs.PathQueryRef

import scala.reflect.ClassTag

/**
 * Created by danielchao on 2/9/17.
 */
trait QueryModel {

  def prop[A](path: String)(implicit ct: ClassTag[A]): PathQueryRef[A] = PathQueryRef[A](path)

}
