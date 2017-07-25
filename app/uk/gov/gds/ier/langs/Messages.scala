package uk.gov.gds.ier.langs

import javax.inject.{Inject, Singleton}

import play.api.i18n.{Lang, MessagesApi}
import jsmessages.JsMessagesFactory
import uk.gov.gds.ier.validation.ErrorTransformForm


import play.api.i18n.Lang
//import jsmessages.api.JsMessages
import uk.gov.gds.ier.validation.ErrorTransformForm

object Messages {
  import play.api.Play.current

  lazy val jsMessagesFactory = play.Play.application().injector().instanceOf(classOf[JsMessagesFactory])

  lazy val messagesApi: MessagesApi = play.Play.application().injector().instanceOf(classOf[MessagesApi])

//  implicit val application = play.api.Play.current
  implicit lazy val messages = play.api.i18n.Messages.Implicits.applicationMessages

  private val playErrorPrefix = "error."
//  private lazy val messages = play.api.i18n.Messages.messages

  def messagesForLang(lang:Lang) = {
    val m = messages.messages.messages("en");
    m
//    messages.filterKeys(_ == lang.language).headOption.map(_._2).getOrElse(Map.empty)
  }

  def translatedGlobalErrors[T](
                                 form: ErrorTransformForm[T]
                               )(implicit lang:Lang): Seq[String] = {
    form.globalErrors.map { error =>
      play.api.i18n.Messages(error.message)
    }
  }

  def apply(key: String)(implicit lang: Lang): String = {
    play.api.i18n.Messages(key)
  }

  def apply(key: String, args: Any*)(implicit lang: Lang): String = {
    play.api.i18n.Messages(key, args: _*)
  }

  lazy val jsMessages = jsMessagesFactory.filtering(!_.startsWith(playErrorPrefix))
}

//
//@Singleton
//class Messages @Inject() (jsMessagesFactory: JsMessagesFactory,  messagesApi: MessagesApi) {
//
//  private val playErrorPrefix = "error."
//
//  def translatedGlobalErrors[T](
//      form: ErrorTransformForm[T]
//  )(implicit lang:Lang): Seq[String] = {
//    form.globalErrors.map { error =>
//      messagesApi.apply(error.message)(lang)
//    }
//  }
//
//  def apply(key: String)(implicit lang: Lang): String = {
//    messagesApi.apply(key)(lang)
//  }
//
//  def apply(key: String, args: Any*)(implicit lang: Lang): String = {
//    messagesApi.apply(key, args: _*)(lang)
//  }
//
//  lazy val jsMessages = jsMessagesFactory.filtering(!_.startsWith(playErrorPrefix))
//}
/*
package uk.gov.gds.ier.langs

import play.api.i18n.Lang
import jsmessages.api.JsMessages
import uk.gov.gds.ier.validation.ErrorTransformForm

object Messages {
  import play.api.Play.current

  private val playErrorPrefix = "error."
  private lazy val messages = play.api.i18n.Messages.messages

  def messagesForLang(lang:Lang) = {
    messages.filterKeys(_ == lang.language).headOption.map(_._2).getOrElse(Map.empty)
  }

  def translatedGlobalErrors[T](
      form: ErrorTransformForm[T]
  )(implicit lang:Lang): Seq[String] = {
    form.globalErrors.map { error =>
      play.api.i18n.Messages(error.message)
    }
  }

  def apply(key: String)(implicit lang: Lang): String = {
    play.api.i18n.Messages(key)
  }

  def apply(key: String, args: Any*)(implicit lang: Lang): String = {
    play.api.i18n.Messages(key, args: _*)
  }

  lazy val jsMessages = JsMessages.filtering(!_.startsWith(playErrorPrefix))
}

 */