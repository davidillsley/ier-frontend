import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import Global.logger
import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import uk.gov.gds.ier.client.{IerApiClient, LocateApiClient}
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.feedback.{FeedbackClient, FeedbackClientImpl}
import uk.gov.gds.ier.service.apiservice.{ConcreteIerApiService, IerApiService}
import uk.gov.gds.ier.stubs.{FeedbackStubClient, IerApiServiceWithStripNino, IerStubApiClient, LocateStubApiClient}

class Module (environment: Environment,
              configuration: Configuration) extends AbstractModule {

  def configure() = {
    val config = new Config(new play.Configuration(configuration.underlying))
    if (config.fakeIer) {
      logger.debug("Binding IerStubApiClient")
      binder.bind(classOf[IerApiClient]).to(classOf[IerStubApiClient])
    }
    if (config.fakeLocate) {
      logger.debug("Binding LocateStubApiClient")
      binder.bind(classOf[LocateApiClient]).to(classOf[LocateStubApiClient])
    }
    if (config.stripNino) {
      logger.debug("Binding IerApiServiceWithStripNino")
      binder.bind(classOf[IerApiService]).to(classOf[IerApiServiceWithStripNino])
    } else {
      logger.debug("Binding ConcreteIerApiService")
      binder.bind(classOf[IerApiService]).to(classOf[ConcreteIerApiService])
    }

    if (config.fakeFeedbackService) {
      logger.debug("Binding FeedbackStubClient")
      binder.bind(classOf[FeedbackClient]).to(classOf[FeedbackStubClient])
    } else {
      logger.debug("Binding FeedbackClientImpl")
      binder.bind(classOf[FeedbackClient]).to(classOf[FeedbackClientImpl])
    }
    //    binder.bind(classOf[A]).to(classOf[GraphiteConfig]).asEagerSingleton()
  }
}

trait A {}

class GraphiteConfig @Inject() (config: Config) extends A {

  def graphiteUrl = config.graphiteUrl
  def graphitePort = config.graphitePort
  def graphiteApiKey = config.graphiteApiKey
  def graphiteInterval = config.graphiteInterval

  val metricRegistry = new com.codahale.metrics.MetricRegistry()

  val hostedGraphiteService = new Graphite(new InetSocketAddress(graphiteUrl,graphitePort.toInt))

  val graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
    .prefixedWith(graphiteApiKey)
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .build(hostedGraphiteService)

  graphiteReporter.start(graphiteInterval.toInt, TimeUnit.SECONDS)
}