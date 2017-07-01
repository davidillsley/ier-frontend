
package uk.gov.gds.ier.stubs

import com.google.inject.{Inject, Singleton}
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.model.{Success, ApiResponse}
import play.api.Application

import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.client.LocateApiClient

@Singleton
class LocateStubApiClient @Inject() (
                                      config: Config,
                                      application: Application,
                                      serialiser: JsonSerialiser) extends LocateApiClient(config, application) {

  override def get(url: String, headers: (String, String)*) : ApiResponse = {
      Success("""[
            {
              "property": "1A Fake Flat",
              "street": "Fake House",
              "area": "123 Fake Street",
              "town": "Fakerton",
              "locality": "Fakesbury",
              "uprn": 12345678,
              "postcode": "AB12 3CD",
              "gssCode": "abc"
            }
          ]""", 0)
  }
}
