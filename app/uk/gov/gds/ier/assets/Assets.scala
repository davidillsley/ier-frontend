package uk.gov.gds.ier.assets

import controllers.AssetsBuilder
import javax.inject.{Inject, Singleton}
import play.api.http.HttpErrorHandler

@Singleton
class Assets @Inject() (errorHandler: HttpErrorHandler) extends AssetsBuilder(errorHandler)

@Singleton
class GovukToolkit @Inject() (errorHandler: HttpErrorHandler) extends AssetsBuilder(errorHandler)

@Singleton
class Template @Inject() (errorHandler: HttpErrorHandler) extends AssetsBuilder(errorHandler)
