package com.bioball.quark

import QueryRefs.PathQueryRef

import scala.reflect.ClassTag

/**
 * Created by danielchao on 2/9/17.
 */
trait QueryModel {

  /**
   * A property that may be used within query funs.
   * @param path The field name to be compared
   * @tparam A The type of the value that is being compared.
   * @return
   */
  def prop[A : ClassTag](path: String): PathQueryRef[A] = PathQueryRef[A](path)

}
