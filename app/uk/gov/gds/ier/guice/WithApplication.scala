package uk.gov.gds.ier.guice

trait WithApplication {

  implicit val application : play.api.Application

}
