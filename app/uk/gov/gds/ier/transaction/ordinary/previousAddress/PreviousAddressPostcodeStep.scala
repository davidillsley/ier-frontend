package uk.gov.gds.ier.transaction.ordinary.previousAddress

import com.google.inject.Inject
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.model.MovedHouseOption
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.service.{AddressService, ScotlandService}
import uk.gov.gds.ier.step.{OrdinaryStep, Routes}
import uk.gov.gds.ier.validation.{CountryValidator, DateValidator, ErrorTransformForm}
import uk.gov.gds.ier.transaction.ordinary.{InprogressOrdinary, OrdinaryControllers}
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

class PreviousAddressPostcodeStep @Inject() (
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService: EncryptionService,
    val addressService: AddressService,
    val scotlandService: ScotlandService,
    val remoteAssets: RemoteAssets,
    val ordinary: OrdinaryControllers,
    val Messages: Messages
) extends OrdinaryStep
  with PreviousAddressPostcodeMustache
  with PreviousAddressForms
  with WithMessages {

  val validation = postcodeStepForm

  val routing = Routes(
    get = routes.PreviousAddressPostcodeStep.get,
    post = routes.PreviousAddressPostcodeStep.post,
    editGet = routes.PreviousAddressPostcodeStep.editGet,
    editPost = routes.PreviousAddressPostcodeStep.editPost
  )

  def nextStep(currentState: InprogressOrdinary) = {
    val isPreviousAddressNI = currentState.previousAddress.exists(
      _.previousAddress.exists(prevAddr => addressService.isNothernIreland(prevAddr.postcode)))

    if (isPreviousAddressNI) {
      //IF YOUNG SCOTTISH CITIZEN, SKIP THE OPEN REGISTER STEP...
      if(currentState.dob.exists(_.dob.isDefined)) {
        if (scotlandService.isYoungScot(currentState)) {
          ordinary.PostalVoteStep
        }
        else {
          ordinary.OpenRegisterStep
        }
      }
      else {
        ordinary.OpenRegisterStep
      }
    } else {
      ordinary.PreviousAddressSelectStep
    }
  }

  override val onSuccess = TransformApplication { currentState =>
    val prevAddressCleaned = currentState.previousAddress.map { prev =>
      prev.copy(
        previousAddress = prev.previousAddress.map(_.copy(
          addressLine = None,
          uprn = None,
          manualAddress = None,
          gssCode = None
        ))
      )
    }

    currentState.copy(
      previousAddress = prevAddressCleaned,
      possibleAddresses = None
    )

  } andThen GoToNextIncompleteStep()
}
