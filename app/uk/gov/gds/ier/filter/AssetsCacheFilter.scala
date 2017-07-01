package uk.gov.gds.ier.filter

import javax.inject.Inject

import akka.stream.Materializer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import uk.gov.gds.ier.logging.Logging
import uk.gov.gds.ier.assets.RemoteAssets

class AssetsCacheFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, remoteAssets: RemoteAssets) extends Filter with Logging {

  def apply(nextFilter: (RequestHeader) => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    nextFilter(requestHeader).map { result =>
      if(remoteAssets.shouldSetNoCache(requestHeader)){
        logger.error(s"request with unrecognised sha: ${requestHeader.method} ${requestHeader.path}")
        result.withHeaders("Pragma" -> "no-cache")
      } else {
        result
      }
    }
  }
}
