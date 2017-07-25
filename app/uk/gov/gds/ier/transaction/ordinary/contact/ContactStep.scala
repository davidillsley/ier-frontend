package uk.gov.gds.ier.transaction.ordinary.contact

import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.model._
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.OrdinaryStep
import uk.gov.gds.ier.step.Routes
import uk.gov.gds.ier.validation.ErrorTransformForm
import uk.gov.gds.ier.transaction.ordinary.{InprogressOrdinary, OrdinaryControllers}
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class ContactStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService : EncryptionService,
    val remoteAssets: RemoteAssets,
    val ordinary: OrdinaryControllers
) extends OrdinaryStep
  with ContactForms
  with ContactMustache
  with WithMessages {

  val validation = contactForm

  val routing = Routes(
    get = routes.ContactStep.get,
    post = routes.ContactStep.post,
    editGet = routes.ContactStep.editGet,
    editPost = routes.ContactStep.editPost
  )

  def nextStep(currentState: InprogressOrdinary) = {
    ordinary.ConfirmationStep
  }
}
