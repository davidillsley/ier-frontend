package uk.gov.gds.ier.transaction.crown.applicationFormVote

import uk.gov.gds.ier.transaction.crown.CrownControllers
import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.step.{CrownStep, Routes}
import uk.gov.gds.ier.validation.ErrorTransformForm
import uk.gov.gds.ier.model.WaysToVoteType
import uk.gov.gds.ier.transaction.crown.InprogressCrown
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class ProxyVoteStep @Inject ()(
    val serialiser: JsonSerialiser,
    val config: Config,
    val encryptionService : EncryptionService,
    val remoteAssets: RemoteAssets,
    val crown: CrownControllers,
    val Messages: Messages
) extends CrownStep
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

  def nextStep(currentState: InprogressCrown) = {
    crown.ContactStep
  }

}

