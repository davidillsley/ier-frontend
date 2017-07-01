package uk.gov.gds.ier.transaction.forces.previousAddress

import uk.gov.gds.ier.transaction.forces.ForcesControllers
import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.model.MovedHouseOption
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.validation._
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.service.AddressService
import uk.gov.gds.ier.step.{ForcesStep, Routes}
import uk.gov.gds.ier.transaction.forces.InprogressForces
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class PreviousAddressFirstStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService : EncryptionService,
    val addressService: AddressService,
    val remoteAssets: RemoteAssets,
    val forces: ForcesControllers,
    val Messages: Messages
) extends ForcesStep
  with PreviousAddressFirstMustache
  with PreviousAddressFirstForms
  with WithMessages {

  val validation = previousAddressFirstForm

  val routing = Routes(
    get = routes.PreviousAddressFirstStep.get,
    post = routes.PreviousAddressFirstStep.post,
    editGet = routes.PreviousAddressFirstStep.editGet,
    editPost = routes.PreviousAddressFirstStep.editPost
  )

  def nextStep(currentState: InprogressForces) = {
    if (currentState.previousAddress.flatMap(_.movedRecently) == Some(MovedHouseOption.Yes)) {
      forces.PreviousAddressPostcodeStep
    } else {
      forces.NationalityStep
    }
  }
}

