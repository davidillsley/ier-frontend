package uk.gov.gds.ier.transaction.overseas.applicationFormVote

import uk.gov.gds.ier.transaction.overseas.OverseasControllers
import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.{OverseaStep, Routes}

import scala.Some
import uk.gov.gds.ier.model.WaysToVoteType
import uk.gov.gds.ier.transaction.overseas.InprogressOverseas
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class ProxyVoteStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService : EncryptionService,
    val remoteAssets: RemoteAssets,
    val overseas: OverseasControllers,
    val Messages: Messages
) extends OverseaStep
  with PostalOrProxyVoteForms
  with PostalOrProxyVoteMustache
  with WithMessages {

  val wayToVote = WaysToVoteType.ByProxy

  val validation = postalOrProxyVoteForm

  val routing = Routes(
    get = routes.ProxyVoteStep.get,
    post = routes.ProxyVoteStep.post,
    editGet = routes.ProxyVoteStep.editGet,
    editPost = routes.ProxyVoteStep.editPost
  )

  def nextStep(currentState: InprogressOverseas) = {
    overseas.ContactStep
  }

}

