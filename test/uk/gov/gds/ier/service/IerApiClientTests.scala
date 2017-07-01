package uk.gov.gds.ier.service

import org.scalatest.{FlatSpec, Matchers}
import uk.gov.gds.ier.client.IerApiClient
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import uk.gov.gds.ier.model.{ApiResponse, Fail, Success}
import uk.gov.gds.ier.controller.MockConfig
import play.api.libs.ws.WSResponse
import play.api.test.FakeApplication

class IerApiClientTests
  extends FlatSpec
  with Matchers
  with IerApiServiceTestsHelper
  with MockitoSugar {

  behavior of "ApiClient.post"
  it should behave like apiClientWithResult(ofClass = classOf[Success], forStatusCode = 200)
  it should behave like apiClientWithResult(ofClass = classOf[Success], forStatusCode = 204)
  it should behave like apiClientWithResult(ofClass = classOf[Fail], forStatusCode = 500)
  it should behave like apiClientWithResult(ofClass = classOf[Fail], forStatusCode = 502)

  def apiClientWithResult[T <: ApiResponse](ofClass: Class[T], forStatusCode: Int) = {
    it should s"return response of type ${ofClass.getSimpleName} for api status code $forStatusCode" in {
      val sutClient = new FakeApiClient(forStatusCode)

      val result = sutClient.post("", "", ("", ""))
      result.isInstanceOf[T] should be(true)
    }
  }
}

class FakeApiClient(returnStatusCode: Int,
                    returnBody: String = "test body")
  extends IerApiClient(new MockConfig, FakeApplication())
  with MockitoSugar {

  override def awaitResultFor(block: => WSResponse): WSResponse = {
    val mockedResponse = mock[WSResponse]
    when(mockedResponse.status).thenReturn(returnStatusCode)
    when(mockedResponse.body).thenReturn(returnBody)
    mockedResponse
  }
}