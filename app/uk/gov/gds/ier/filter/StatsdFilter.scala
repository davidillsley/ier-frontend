package uk.gov.gds.ier.filter

import javax.inject.Inject

import akka.stream.Materializer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import uk.gov.gds.ier.logging.Logging
import uk.gov.gds.ier.client.StatsdClient

class StatsdFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {

  def apply(nextFilter: (RequestHeader) => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      logger.info(
        s"${requestHeader.method} ${requestHeader.uri} " +
          s"took ${requestTime}ms and returned ${result.header.status}")
      val metricPageName =
        requestHeader.path.substring(1).replace('/','.') + "." + requestHeader.method
      StatsdClient.timing(metricPageName, requestTime)
      result
    }
  }
}
