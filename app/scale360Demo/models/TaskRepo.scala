package scale360Demo.models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio
import slick.dbio.Effect.Read
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Task(id: Int, subject: String, detail: String, status: String){

  def updateTask(subject: String, detail: String): Task =
    this.copy(subject = subject,detail = detail)

}

class TaskRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.profile.api._
  private[models] val Tasks = TableQuery[TasksTable]

  def listAll(): Future[Iterable[Task]] =
    db.run(Tasks.result)

  def findById(id: Int): Future[Option[Task]] =
    db.run(Tasks.filter(_.id === id).result.headOption)

  def addTask(task: Task): Future[Int] =
    db.run(Tasks returning Tasks.map(_.id) += task)

  def updateTask(taskData: Task): Future[Int] = {
    val q = for { p <- Tasks if p.id === taskData.id } yield (p.subject,p.detail)
    val updateAction = q.update((taskData.subject,taskData.detail))

    db.run(updateAction)
  }

  def updateTaskStatus(taskData: Task): Future[Int] = {
    val q = for { p <- Tasks if p.id === taskData.id } yield (p.status)
    val updateAction = q.update((taskData.status))

    db.run(updateAction)
  }

  def deleteTask(id: Int): Future[Int] = {
    db.run(Tasks.filter(_.id === id).delete)
  }

  private[models] class TasksTable(tag: Tag) extends Table[Task](tag, "tbl_task") {

    def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
    def subject = column[String]("subject")
    def detail = column[String]("detail")
    def status = column[String]("status")

    override def * = (id, subject, detail, status) <> ((Task.apply _).tupled, Task.unapply)
    def ? = (id.?, subject.?, detail.?, status.?).shaped.<>({ r => import r._; _1.map(_ => Task.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}