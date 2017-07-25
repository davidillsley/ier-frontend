package uk.gov.gds.ier.test

import jsmessages.JsMessagesFactory
import uk.gov.gds.ier.guice.{WithConfig, WithMessages}
import org.scalatest.mock.MockitoSugar
import play.api.Environment
import play.api.i18n.{DefaultLangs, DefaultMessagesApi, MessagesApi}
import play.api.test.FakeApplication
import uk.gov.gds.ier.langs.Messages

trait WithMockMessages
  extends WithMessages {
//
//  private val mockito2 = new MockitoSugar {}
//
//  private val application = FakeApplication()
//  private val messagesApi = new DefaultMessagesApi(Environment.simple(), application.configuration, new DefaultLangs(application.configuration))

}
