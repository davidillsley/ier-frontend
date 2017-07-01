package uk.gov.gds.ier.feedback

import uk.gov.gds.ier.mustache.{InheritedGovukMustache, MustacheModel}
import uk.gov.gds.ier.guice.{WithConfig, WithMessages, WithRemoteAssets}
import uk.gov.gds.ier.validation.ErrorTransformForm

trait FeedbackMustache
  extends InheritedGovukMustache
  with MustacheModel {
    self: WithRemoteAssets
     with FeedbackForm
     with WithConfig
     with WithMessages =>

  case class ThankYouPage (
      override val sourcePath: String = ""
  ) (
      implicit override val lang: Lang
  ) extends ArticleMustachio("feedbackThankYou")

  private[this] implicit val progressForm = ErrorTransformForm(feedbackForm)

  case class FeedbackPage (
      postUrl: String,
      feedbackText: Field = TextField(
        key = keys.feedback.feedbackText
      ),
      contactName: Field = TextField(
        key = keys.feedback.contactName
      ),
      contactEmail: Field = TextField(
        key = keys.feedback.contactEmail
      ),
      maxFeedbackCommentLength: Int = maxFeedbackCommentLength,
      maxFeedbackNameLength: Int = maxFeedbackNameLength,
      maxFeedbackEmailLength: Int = maxFeedbackEmailLength,
      feedbackDetailHint: String = Messages(
        "feedback_detail_hint",
        maxFeedbackCommentLength
      )
  ) (
      implicit override val lang: Lang
  ) extends ArticleMustachio("feedbackForm")
}
