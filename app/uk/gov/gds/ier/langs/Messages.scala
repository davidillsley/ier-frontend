package uk.gov.gds.ier.langs

import javax.inject.Inject

import play.api.i18n.{Lang, MessagesApi}
import jsmessages.JsMessagesFactory
import uk.gov.gds.ier.validation.ErrorTransformForm

class Messages @Inject() (jsMessagesFactory: JsMessagesFactory,  messagesApi: MessagesApi) {

  private val playErrorPrefix = "error."

  def translatedGlobalErrors[T](
      form: ErrorTransformForm[T]
  )(implicit lang:Lang): Seq[String] = {
    form.globalErrors.map { error =>
      messagesApi.apply(error.message)(lang)
    }
  }

  def apply(key: String)(implicit lang: Lang): String = {
    messagesApi.apply(key)(lang)
  }

  def apply(key: String, args: Any*)(implicit lang: Lang): String = {
    messagesApi.apply(key, args: _*)(lang)
  }

  lazy val jsMessages = jsMessagesFactory.filtering(!_.startsWith(playErrorPrefix))
}
