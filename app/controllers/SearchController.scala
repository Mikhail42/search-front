package controllers

import javax.inject.Inject
import models.TextDocument
import play.api.data._
import play.api.i18n._
import play.api.mvc._

class SearchController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

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

  def index = Action {  implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.index(articles))
  }
}
