package uk.gov.gds.ier.validation

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Lang
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.serialiser.WithSerialiser
import uk.gov.gds.ier.transaction.ordinary.confirmation.ConfirmationForms
import uk.gov.gds.ier.transaction.ordinary.InprogressOrdinary

trait IerForms extends OrdinaryMappings with ConfirmationForms {
  self: WithSerialiser with WithMessages =>

  val dobFormat = "yyyy-MM-dd"
  val timeFormat = "yyyy-MM-dd HH:mm:ss"
  val postcodeForm = Form(
    single(
      "postcode" -> nonEmptyText.verifying(PostcodeValidator.isValid(_))
    )
  )
  val completePostcodeForm = Form(
    single(
      keys.address.postcode.key -> nonEmptyText
    )
  )

  implicit class FormWithErrorsAsMap[A](form: Form[A]) {
    def errorsAsMap(implicit lang: Lang) = {
      form.errors.groupBy(_.key).mapValues {
        errors =>
          errors.map(e => Messages(e.message, e.args: _*))
      }
    }
    def simpleErrors(implicit messages: play.api.i18n.Messages): Map[String, String] = {
      form.errors.foldLeft(Map.empty[String, String]){
        (map, error) => map ++ Map(error.key -> play.api.i18n.Messages(error.message, error.args: _*))
      }
    }
  }
}
