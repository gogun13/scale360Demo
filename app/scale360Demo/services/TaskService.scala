package scale360Demo.services

import javax.inject.Inject

import play.api.{Logger, MarkerContext}
import play.api.libs.json.{JsValue, Json, Writes}
import scale360Demo.controllers.{TaskFormDataAdd, TaskFormDataUpd, TaskFormDataUpdStatus}
import scale360Demo.models.{Task, TaskRepo}

import scala.concurrent.{ExecutionContext, Future}

case class TaskResource(id: Int, subject: String, detail: String, status: String)

object TaskResource {
  implicit val implicitWrites = new Writes[TaskResource] {
    def writes(task: TaskResource): JsValue = {
      Json.obj(
        "id" -> task.id,
        "subject" -> task.subject,
        "detail" -> task.detail,
        "status" -> task.status
      )
    }
  }
}

class TaskService @Inject()(taskRepo: TaskRepo)(implicit ec: ExecutionContext) {

  private val logger = Logger(getClass)

  def listAll(implicit mc: MarkerContext): Future[Iterable[TaskResource]] = {
    taskRepo.listAll.map { taskDataList =>
      taskDataList.map(taskData => createTaskResource(taskData))
    }
  }

  def findById(id: Int)(implicit mc: MarkerContext): Future[Option[TaskResource]] = {
    var a = taskRepo.findById(id)

    a.map { taskDataList =>
      taskDataList.map(taskData => createTaskResource(taskData))
    }
  }

  def addTask(input: TaskFormDataAdd): Future[Int] = {
    taskRepo.addTask(Task(0, input.subject, input.detail, "pending"))

  }

  def updateTask(input: TaskFormDataUpd): Future[Int] = {
    taskRepo.updateTask(Task(input.id, input.subject, input.detail, ""))

  }

  def updateTaskStatus(input: TaskFormDataUpdStatus): Future[Int] = {
    taskRepo.updateTaskStatus(Task(input.id, "", "", input.status))
  }

  def deleteTask(id: Int): Future[Int] = {
    taskRepo.deleteTask(id)
  }

  private def createTaskResource(task: Task): TaskResource = {
    TaskResource(task.id,task.subject,task.detail,task.status)
  }

}
