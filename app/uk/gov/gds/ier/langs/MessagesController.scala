package uk.gov.gds.ier.langs

import play.api.mvc._
import com.google.inject.Singleton

@Singleton
class MessagesController extends Controller {

  def all = Action {
    Ok(Messages.jsMessages.all(Some("GOVUK.registerToVote.messages")))
  }

  def forLang(langCode:String) = Action {
    implicit val lang = Language.Lang(langCode)
    implicit val application = play.api.Play.current
    implicit val messages = play.api.i18n.Messages.Implicits.applicationMessages
    Ok(Messages.jsMessages(Some("GOVUK.registerToVote.messages")))
  }
}


//package uk.gov.gds.ier.langs
//
//import play.api.mvc._
//import com.google.inject.{Inject, Singleton}
//import play.api.i18n.MessagesApi
//
//@Singleton
//class MessagesController @Inject() (messages: Messages, messagesApi: MessagesApi) extends Controller {
//
//  def all = Action {
//    Ok(messages.jsMessages.all(Some("GOVUK.registerToVote.messages")))
//  }
//
//  def forLang(langCode:String) = Action {
//    implicit val mess = messagesApi.preferred(Seq(Language.Lang(langCode)))
//    Ok(messages.jsMessages(Some("GOVUK.registerToVote.messages")))
//  }
//}
