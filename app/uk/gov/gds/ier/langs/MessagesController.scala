package uk.gov.gds.ier.langs

import play.api.mvc._
import com.google.inject.{Inject, Singleton}
import play.api.i18n.MessagesApi

@Singleton
class MessagesController @Inject() (messages: Messages, messagesApi: MessagesApi) extends Controller {

  def all = Action {
    Ok(messages.jsMessages.all(Some("GOVUK.registerToVote.messages")))
  }

  def forLang(langCode:String) = Action {
    implicit val mess = messagesApi.preferred(Seq(Language.Lang(langCode)))
    Ok(messages.jsMessages(Some("GOVUK.registerToVote.messages")))
  }
}
