package uk.gov.gds.ier.test

import uk.gov.gds.ier.guice.{WithConfig, WithMessages}
import org.scalatest.mock.MockitoSugar
import uk.gov.gds.ier.langs.Messages

trait WithMockMessages
  extends WithMessages {

  private val mockito2 = new MockitoSugar {}

  val Messages = mockito2.mock[Messages]
}
