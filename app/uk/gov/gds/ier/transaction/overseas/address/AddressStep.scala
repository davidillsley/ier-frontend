package uk.gov.gds.ier.transaction.overseas.address

import uk.gov.gds.ier.transaction.overseas.OverseasControllers
import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.{OverseaStep, Routes}
import uk.gov.gds.ier.transaction.overseas.InprogressOverseas
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class AddressStep @Inject() (
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService: EncryptionService,
    val remoteAssets: RemoteAssets,
    val overseas: OverseasControllers,
    val Messages: Messages
) extends OverseaStep
  with AddressForms
  with AddressMustache
  with WithMessages {

  val validation = addressForm
  val routing = Routes(
    get = routes.AddressStep.get,
    post = routes.AddressStep.post,
    editGet = routes.AddressStep.editGet,
    editPost = routes.AddressStep.editPost
  )

  def nextStep(currentState: InprogressOverseas) = {
    overseas.OpenRegisterStep
  }
}
