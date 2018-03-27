package controllers

import controllers.SearchForm.Data
import javax.inject.Inject
import models.TextDocument
import org.slf4j.LoggerFactory
import play.api.data._
import play.api.i18n._
import play.api.mvc._

class SearchController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  val log = LoggerFactory.getLogger(this.getClass)

  private def articles(query: String) = scala.collection.mutable.ArrayBuffer(
    new TextDocument("Вики",
      """Ви́ки (англ. wiki) — веб-сайт, содержимое которого пользователи могут самостоятельно
        | изменять с помощью инструментов, предоставляемых самим сайтом. Форматирование текста и вставка различных
        | объектов в текст производится с использованием вики-разметки.""".stripMargin),
    new TextDocument("Сайт",
      """Сайт, или веб-сайт (читается [вэбсайт], от англ. website: web — «паутина, сеть» и site —
        | «место», буквально «место, сегмент, часть в сети»), — совокупность логически связанных между собой веб-страниц;
        |  также место расположения контента сервера. Обычно сайт в Интернете представляет собой массив связанных данных,
        |   имеющий уникальный адрес и воспринимаемый пользователем как единое целое.""".stripMargin)
  )

  // The URL to the widget.  You can call this directly from the template, but it
  // can be more convenient to leave the template completely stateless i.e. all
  // of the "WidgetController" references are inside the .scala file.
  private val postUrl: Call = routes.SearchController.index()

  def start = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.start(SearchForm.form, postUrl))
  }

  def index = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithError: Form[Data] =>
      log.debug("BadRequest: '{}'", formWithError.value)
      BadRequest(views.html.start(SearchForm.form, postUrl))
    }

    val successFunction = { data: Data =>
      log.debug("search success")
      val query = data.searchRequest
      val form = SearchForm.form.fill(new Data(query))//.bind(Map("query" -> question))
      Ok(views.html.index(query, articles(query), form, postUrl))
    }

    log.debug("search before valid")
    val formValidationResult = SearchForm.form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }
}
