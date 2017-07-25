package uk.gov.gds.ier.transaction.ordinary.openRegister

import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.validation._
import play.api.mvc.Call
import play.twirl.api.Html
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.{OrdinaryStep, Routes}
import uk.gov.gds.ier.transaction.ordinary.{InprogressOrdinary, OrdinaryControllers}
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class OpenRegisterStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService : EncryptionService,
    val remoteAssets: RemoteAssets,
    val ordinary: OrdinaryControllers
) extends OrdinaryStep
  with OpenRegisterForms
  with OpenRegisterMustache
  with WithMessages {

  val validation = openRegisterForm

  val routing = Routes(
    get = routes.OpenRegisterStep.get,
    post = routes.OpenRegisterStep.post,
    editGet = routes.OpenRegisterStep.editGet,
    editPost = routes.OpenRegisterStep.editPost
  )

  def nextStep(currentState: InprogressOrdinary) = {
    ordinary.PostalVoteStep
  }
  override def isStepComplete(currentState: InprogressOrdinary) = {
    currentState.openRegisterOptin.isDefined
  }
}
