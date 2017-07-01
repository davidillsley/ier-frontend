package uk.gov.gds.ier.client

import java.util.concurrent.TimeUnit

import uk.gov.gds.ier.model.{ApiResponse, Fail, Success}
import play.api.libs.ws.{WS, WSAuthScheme, WSClient, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.Await
import play.api.http._
import uk.gov.gds.ier.guice.{WithApplication, WithConfig}
import org.joda.time.DateTime
import uk.gov.gds.ier.logging.Logging

trait ApiClient extends Logging {
  self:WithConfig with WithApplication =>

  def get(url: String, headers: (String, String)*) : ApiResponse = {
	val start = new DateTime()
    try {
        val result = awaitResultFor {
          Await.result(
            WS.url(url).withHeaders(headers:_*).get(),
            config.apiTimeout seconds)
        }
        result.status match {
          case Status.OK => {
            val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
            logger.info(s"apiClient.get url:$url result:200 timeTakenMs:$timeTakenMs")
            Success(result.body, timeTakenMs)
          }
          case status => {
            val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
            logger.info(
              s"apiClient.get url:$url result:$status timeTakenMs:$timeTakenMs reason:${result.body}")
            Fail(result.body, timeTakenMs)
          }
        }
    } catch {
      case e:Exception => {
        val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
        logger.error(
          s"apiClient.get url:$url timeTakenMs:$timeTakenMs exception:${e.getStackTraceString}")
        Fail(e.getMessage, timeTakenMs)
      }
    }
  }

  def post(
      url:String,
      content:String,
      headers: (String, String)*) : ApiResponse = {

    val start = new DateTime()
    try {
      val result = awaitResultFor {
        Await.result(
          WS.url(url)
            .withHeaders("Content-Type" -> MimeTypes.JSON)
            .withHeaders(headers: _*)
            .post(content),
          config.apiTimeout seconds
        )
      }
      result.status match {
        case Status.OK => {
          val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
          logger.info(s"apiClient.post url:$url result:200 timeTakenMs:$timeTakenMs")
          Success(result.body, timeTakenMs)
        }
        case Status.NO_CONTENT => {
          val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
          logger.info(s"apiClient.post url:$url result:204 timeTakenMs:$timeTakenMs")
          Success("", timeTakenMs)
        }
        case status => {
          val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
          logger.info(s"apiClient.post url:$url result:$status timeTakenMs:$timeTakenMs")
          Fail(result.body, timeTakenMs)
        }
      }
    } catch {
      case e:Exception => {
        val timeTakenMs = DateTime.now.minus(start.getMillis).getMillis
        logger.error(
          s"apiClient.post url:$url timeTakenMs:$timeTakenMs exception:${e.getStackTraceString}")
        Fail(e.getMessage, timeTakenMs)
      }
    }
  }

  /**
   * Post a serialized JSON content asynchronously to given web service with basic authentication
   * and don't wait for reply
   */
  def postAsync(
      url: String,
      content: String,
      username: String,
      password: String,
      headers: (String, String)*
  ): Unit = {
    implicit val context = scala.concurrent.ExecutionContext.Implicits.global
    WS.url(url)
      .withAuth(username, password, WSAuthScheme.BASIC)
      .withHeaders("Content-Type" -> MimeTypes.JSON)
      .withHeaders(headers: _*)
      .withRequestTimeout((config.apiTimeout * 1000) milliseconds)
      .post(content)
      .map {
      // we are not really interested in response, just log it
      response => response.status match {
        case IsSuccessStatusCode(true) =>
          logger.info(s"apiClient.post url: $url request succeed " +
            s"with status code ${response.status}")
        case _ =>
          logger.error(s"apiClient.post url: $url request failed " +
            s"with error status code ${response.status}")
      }
    }
    .recover {
      case exception => {
        logger.error(s"apiClient.post url: $url request failed " +
          s"with exception ${exception}")
        // this also handles timeouts
      }
    }

    logger.info(s"apiClient.post url: $url request submitted")
  }

  object IsSuccessStatusCode {
    val successStatusCodes =  Set(200, 201, 202)
    def unapply(statusCode: Int) = Option(statusCode).map { c => successStatusCodes.contains(c) }
  }

  protected def awaitResultFor(block: => WSResponse): WSResponse = block
}
