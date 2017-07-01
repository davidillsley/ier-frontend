package uk.gov.gds.ier.transaction.ordinary.nino

import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.validation._
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.{OrdinaryStep, Routes}
import uk.gov.gds.ier.transaction.ordinary.{InprogressOrdinary, OrdinaryControllers}
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class NinoStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService: EncryptionService,
    val remoteAssets: RemoteAssets,
    val ordinary: OrdinaryControllers,
    val Messages: Messages
) extends OrdinaryStep
  with NinoForms
  with NinoMustache
  with WithMessages {

  val validation = ninoForm

  val routing = Routes(
    get = routes.NinoStep.get,
    post = routes.NinoStep.post,
    editGet = routes.NinoStep.editGet,
    editPost = routes.NinoStep.editPost
  )

  override val onSuccess = TransformApplication { currentState =>
    val ninoValue = currentState.nino.map { currentNino =>
      if (currentNino.nino.isDefined) currentNino.copy(noNinoReason = None)
      else {
        currentNino.copy(nino = None)
      }
    }
    currentState.copy(nino = ninoValue)
  } andThen GoToNextIncompleteStep()

  def nextStep(currentState: InprogressOrdinary) = {
    ordinary.AddressStep
  }
}
