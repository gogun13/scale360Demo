package scale360Demo.routes

import scale360Demo.controllers.TaskController

import javax.inject.Inject

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class TaskRoute @Inject()(controller: TaskController)
  extends SimpleRouter
{
  override def routes: Routes = {
    case GET(p"/") =>
      controller.index
    case GET(p"/$id") =>
      controller.findById(id)
    case POST(p"/addTask") =>
      controller.addTask
    case POST(p"/updateTask") =>
      controller.updateTask
    case POST(p"/updateTaskStatus") =>
      controller.updateTaskStatus
    case POST(p"/deleteTask") =>
      controller.deleteTask
  }
}
