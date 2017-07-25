package uk.gov.gds.ier.transaction.ordinary.name

import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.validation._
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.{OrdinaryStep, Routes}
import uk.gov.gds.ier.transaction.ordinary.InprogressOrdinary
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages
import uk.gov.gds.ier.transaction.ordinary.OrdinaryControllers
import uk.gov.gds.ier.service.{AddressService, ScotlandService}

@Singleton
class NameStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService : EncryptionService,
    val remoteAssets: RemoteAssets,
    val ordinary: OrdinaryControllers,
    val addressService: AddressService,
    val scotlandService: ScotlandService)
  extends OrdinaryStep
  with NameForms
  with NameMustache
    with WithMessages {

  val validation = nameForm

  val routing = Routes(
    get = routes.NameStep.get,
    post = routes.NameStep.post,
    editGet = routes.NameStep.editGet,
    editPost = routes.NameStep.editPost
  )

  def nextStep(currentState: InprogressOrdinary) = {
    //IF YOUNG SCOTTISH CITIZEN, SKIP THE NINO STEP...
    if(currentState.dob.exists(_.dob.isDefined)) {
      if (scotlandService.isYoungScot(currentState)) {
        ordinary.AddressStep
      }
      else {
        ordinary.NinoStep
      }
    }
    else {
      ordinary.NinoStep
    }
  }
}
