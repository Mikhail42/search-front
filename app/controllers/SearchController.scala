package controllers

import javax.inject.Inject
import models.TextDocument
import org.slf4j.LoggerFactory
import play.api.data._
import play.api.i18n._
import play.api.mvc._

class SearchController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  val log = LoggerFactory.getLogger(this.getClass)

  import SearchForm._

  private val articles = scala.collection.mutable.ArrayBuffer(
    new TextDocument("Вики",
      """Ви́ки (англ. wiki) — веб-сайт, содержимое которого пользователи могут самостоятельно
        | изменять с помощью инструментов, предоставляемых самим сайтом. Форматирование текста и вставка различных
        | объектов в текст производится с использованием вики-разметки. На базе этих принципов построена Википедия
        | и другие проекты Фонда Викимедиа[1].""".stripMargin),
    new TextDocument("Сайт",
      """Сайт, или веб-сайт (читается [вэбсайт], от англ. website: web — «паутина, сеть» и site —
        | «место», буквально «место, сегмент, часть в сети»), — совокупность логически связанных между собой веб-страниц;
        |  также место расположения контента сервера. Обычно сайт в Интернете представляет собой массив связанных данных,
        |   имеющий уникальный адрес и воспринимаемый пользователем как единое целое. Веб-сайты называются так, потому
        |   что доступ к ним происходит по протоколу HTTP[1].""".stripMargin)
  )

  // The URL to the widget.  You can call this directly from the template, but it
  // can be more convenient to leave the template completely stateless i.e. all
  // of the "WidgetController" references are inside the .scala file.
  private val postUrl = routes.SearchController.search()

  def index(question: String) = Action {  implicit request: MessagesRequest[AnyContent] =>
    log.debug("request: '{}'", request)
    Ok(views.html.index(question, articles))
  }

  // This will be the action that handles our form post
  def search = Action { implicit request: MessagesRequest[AnyContent] =>
    log.debug("search")
    val errorFunction = { formWithErrors: Form[Data] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      // TODO
      log.warn("search error")
      formWithErrors.value.foreach(data => log.warn("data with error: '{}'", data))
      BadRequest(views.html.indexWithSearch(formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      log.debug("search success")
      articles.append(new TextDocument(data.searchRequest, "Content"))
      // This is the good case, where the form was successfully parsed as a Data object.
      Redirect(routes.SearchController.index(data.searchRequest))
    }

    log.debug("search before valid")
    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }
}
