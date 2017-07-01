package uk.gov.gds.ier.controller

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.http.HeaderNames
import uk.gov.gds.ier.logging.Logging
import uk.gov.gds.ier.service.DeclarationPdfDownloadService
import com.google.inject.Inject
import com.google.inject.Singleton

@Singleton
class DeclarationPdfDownloadController @Inject()(downloadService: DeclarationPdfDownloadService)
  extends Controller with HeaderNames with Logging {

  def download = Action {
    Ok.sendResource(downloadService.pdfFileName, inline = false).as("application/pdf")
  }
}
