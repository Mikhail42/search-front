package controllers

import controllers.SearchForm.Data
import javax.inject.Inject
import org.ionkin.search.{EvaluatorPerformance, QueryPage}
import org.slf4j.LoggerFactory
import play.api.mvc._

import scala.util.{Failure, Success, Try}
// TODO: оценка
object SearchController {
  val log = LoggerFactory.getLogger(SearchController.getClass)
  lazy val evaluator: EvaluatorPerformance = EvaluatorPerformance.loadTest()
  val linksOnPage = 50
}

class SearchController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import SearchController._

  def start = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.start(SearchForm.form))
  }

  // TODO: J-pop word with -
  def indexWithPage(query: String, page: Option[Int]) = Action { implicit request: MessagesRequest[AnyContent] =>
    log.debug("search success")
    log.info("query: {}", query)
    val form = SearchForm.form.fill(new Data(query))
    val pageN = page.getOrElse(1)
    val queryLinks: List[(Int, String, String)] =
      Try(links(query, pageN)).map(_.map(x => (x.getDocId, x.getTitle, x.getSnippet))) match {
        case Success(links) => links
        case Failure(exc) =>
          log.warn("Can't handle request", exc)
          form.withGlobalError(exc.getMessage)
          List()
      }
    log.info(s"links are ready: ${queryLinks.map(_._1)}")
    Ok(views.html.index(query, pageN, queryLinks, form))
  }

  private def links(query: String, page: Int): List[QueryPage] = {
    import scala.jdk.CollectionConverters._
    log.debug("try evaluate '{}'", query)
    val idsJava = evaluator.evaluate(query, linksOnPage * page)
    val ids = idsJava.asScala.toList.map(_.value)
    log.debug("ids.take(10) '{}'...", ids.take(10))
    ids.slice((page - 1) * linksOnPage, page * linksOnPage)
  }
}