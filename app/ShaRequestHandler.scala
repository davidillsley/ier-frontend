import javax.inject.Inject

import org.slf4j.{LoggerFactory, MDC}
import play.api.http._
import play.api.mvc.RequestHeader
import play.api.routing.Router
import uk.gov.gds.ier.assets.RemoteAssets

class ShaRouteHandler @Inject()(errorHandler: HttpErrorHandler,
                                configuration: HttpConfiguration,
                                filters: HttpFilters,
                                remoteAssets: RemoteAssets,
                                router: Router
                               ) extends DefaultHttpRequestHandler(
  router, errorHandler, configuration, filters
) {
  val logger = LoggerFactory.getLogger(this.getClass)

  override def routeRequest(request: RequestHeader) = {
    logger.debug(s"routing request ${request.method} ${request.path}")
    MDC.put("clientip", request.headers.get("X-Real-IP").getOrElse("N/A"))
    val strippedRequest = remoteAssets.stripGitSha(request)
    super.routeRequest(strippedRequest)
  }
}