package uk.gov.gds.ier.filter

import javax.inject.Inject

import akka.stream.Materializer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import uk.gov.gds.ier.logging.Logging

class ResultFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext) extends Filter with Logging {
  override def apply(nextFilter: (RequestHeader) => Future[Result])
                    (requestHeader: RequestHeader): Future[Result] = {
    nextFilter(requestHeader).map { result =>
      result.withHeaders("X-Frame-Options" -> "deny")
    }
  }
}
