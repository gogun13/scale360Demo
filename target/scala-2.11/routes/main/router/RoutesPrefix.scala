
// @GENERATOR:play-routes-compiler
// @SOURCE:D:/workspaceScala/scale360Demo/conf/routes
// @DATE:Sun Oct 22 11:08:12 ICT 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
