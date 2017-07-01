package uk.gov.gds.ier.feedback

import com.google.inject.Inject
import play.api.Application
import uk.gov.gds.ier.client.ApiClient
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.logging.Logging
import uk.gov.gds.ier.guice.{WithApplication, WithConfig}

trait FeedbackClient {
  def submit(feedbackData: FeedbackSubmissionData)
}

/**
  * See: http://developer.zendesk.com/documentation/rest_api/tickets.html#creating-tickets
  */
class FeedbackClientImpl @Inject() (serialiser: JsonSerialiser, configuration: Config, app: Application)
  extends FeedbackClient
    with ApiClient
    with WithConfig
    with WithApplication
    with Logging {

  val config = configuration
  val application = app

  def submit(feedbackData: FeedbackSubmissionData) = {
    postAsync(
      url = config.zendeskUrl,
      content = serialiser.toJson(feedbackData),
      username = config.zendeskUsername,
      password = config.zendeskPassword)
  }
}
