package models

import java.net.URLEncoder

sealed trait Document

final case class TextDocument(title: String, content: String) {
  def url: String = s"https://ru.wikipedia.org/wiki/${URLEncoder.encode(title, "UTF-8")}"
}
