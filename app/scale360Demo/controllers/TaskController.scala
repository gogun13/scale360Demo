package scale360Demo.controllers

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._
import scale360Demo.services.TaskService

import scala.concurrent.{ExecutionContext, Future}

case class TaskFormDataAdd(subject: String, detail: String)
case class TaskFormDataUpd(id:Int,subject: String, detail: String)
case class TaskFormDataUpdStatus(id:Int, status: String)
case class TaskFormDataDelTask(id:Int)

class TaskController @Inject()(taskService:TaskService,cc: PostControllerComponents)(implicit ec: ExecutionContext)
  extends PostBaseController(cc) {

  private val logger = Logger(getClass)

  private val formAdd: Form[TaskFormDataAdd] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "subject" -> text,
        "detail" -> text
      )(TaskFormDataAdd.apply)(TaskFormDataAdd.unapply)
    )
  }

  private val formUpd: Form[TaskFormDataUpd] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "id" -> number,
        "subject" -> text,
        "detail" -> text
      )(TaskFormDataUpd.apply)(TaskFormDataUpd.unapply)
    )
  }

  private val formUpdStatus: Form[TaskFormDataUpdStatus] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "id" -> number,
        "status" -> text
      )(TaskFormDataUpdStatus.apply)(TaskFormDataUpdStatus.unapply)
    )
  }

  private val formDelTask: Form[TaskFormDataDelTask] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "id" -> number
      )(TaskFormDataDelTask.apply)(TaskFormDataDelTask.unapply)
    )
  }

  def index: Action[AnyContent] = PostAction.async { implicit request =>
    logger.info("index: ")
    taskService.listAll.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def findById(id: String): Action[AnyContent] = PostAction.async { implicit request =>
    logger.info("findById: ")
    taskService.findById(Integer.parseInt(id)).map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def addTask: Action[AnyContent] = PostAction.async { implicit request =>
    logger.info("[addTask][Begin]")

    def failure(badForm: Form[TaskFormDataAdd]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: TaskFormDataAdd) = {
      taskService.addTask(input).map { row =>
        Created(Json.toJson(Json.obj("status" -> "SUCCESS", "row" -> row)))
      }
    }

    formAdd.bindFromRequest().fold(failure, success)
  }

  def updateTask: Action[AnyContent] = PostAction.async { implicit request =>
    logger.info("[updateTask][Begin]")

    def failure(badForm: Form[TaskFormDataUpd]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: TaskFormDataUpd) = {
      taskService.updateTask(input).map { row =>
        Created(Json.toJson(Json.obj("status" -> "SUCCESS", "row" -> row)))
      }
    }

    formUpd.bindFromRequest().fold(failure, success)
  }

  def updateTaskStatus: Action[AnyContent] = PostAction.async { implicit request =>
    logger.info("[updateTaskStatus][Begin]")

    def failure(badForm: Form[TaskFormDataUpdStatus]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: TaskFormDataUpdStatus) = {
      taskService.updateTaskStatus(input).map { row =>
        Created(Json.toJson(Json.obj("status" -> "SUCCESS", "row" -> row)))
      }
    }

    formUpdStatus.bindFromRequest().fold(failure, success)
  }

  def deleteTask(): Action[AnyContent] = PostAction.async { implicit request =>
    logger.info("[deleteTask]")

    def failure(badForm: Form[TaskFormDataDelTask]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: TaskFormDataDelTask) = {
      taskService.deleteTask(input.id).map { row =>
        Created(Json.toJson(Json.obj("status" -> "SUCCESS", "row" -> row)))
      }
    }

    formDelTask.bindFromRequest().fold(failure, success)

  }

}
