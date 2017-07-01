package uk.gov.gds.ier.controller

import com.google.inject.Inject
import uk.gov.gds.ier.service.{AddressService, ScotlandService}
import play.api.mvc.Controller
import play.api.mvc.Action
import uk.gov.gds.ier.client.ApiResults
import uk.gov.gds.ier.serialiser.{JsonSerialiser, WithSerialiser}
import uk.gov.gds.ier.exception.PostcodeLookupFailedException
import uk.gov.gds.ier.validation.IerForms
import uk.gov.gds.ier.model.Addresses
import com.google.inject.Singleton
import uk.gov.gds.ier.guice.WithMessages
import uk.gov.gds.ier.langs.Messages

@Singleton
class PostcodeController @Inject()(
    val addressService: AddressService,
    val serialiser: JsonSerialiser,
    val scotlandService: ScotlandService,
    val Messages: Messages
  ) extends Controller with ApiResults with WithSerialiser with IerForms with WithMessages {

  def lookupAddress(postcode: String) = Action {
    implicit request =>

      postcodeForm.bind(Map("postcode" -> postcode)).fold(
        errors => badResult("errors" -> errors.errorsAsMap),
        postcode =>
          try {
            val addresses = addressService.lookupPartialAddress(postcode)
            okResult(Addresses(addresses))
          } catch {
            case e:PostcodeLookupFailedException => serverErrorResult("error" -> e.getMessage)
          }
      )
  }
}
