package uk.gov.gds.ier.client

import com.google.inject.Inject
import play.api.Application
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.guice.{WithApplication, WithConfig}

class IerApiClient @Inject() (configuration: Config, app: Application) extends ApiClient with WithConfig with WithApplication {
  val config = configuration
  val application = app
}