package uk.gov.gds.ier.transaction.crown.address

import uk.gov.gds.ier.test.MockingTestSuite
import uk.gov.gds.ier.config.Config
import uk.gov.gds.ier.model.{LastAddress, PartialAddress}
import uk.gov.gds.ier.security.EncryptionService
import uk.gov.gds.ier.serialiser.JsonSerialiser
import uk.gov.gds.ier.transaction.crown.CrownControllers
import uk.gov.gds.ier.service.AddressService
import uk.gov.gds.ier.step.GoTo
import uk.gov.gds.ier.controller.routes.ExitController
import uk.gov.gds.ier.assets.RemoteAssets
import uk.gov.gds.ier.langs.Messages

/*
 * This test mock the AddressService.
 *
 * So it is separated from the normal AddressStepTests
 */
class AddressStepMockedTests extends MockingTestSuite {

  it should "redirect to Scotland exit page if the gssCode starts with S" in {
    val mockedJsonSerialiser = mock[JsonSerialiser]
    val mockedConfig = mock[Config]
    val mockedEncryptionService = mock[EncryptionService]
    val mockedAddressService = mock[AddressService]
    val mockedRemoteAssets = mock[RemoteAssets]
    val mockCrownControllers = mock[CrownControllers]
    val mockMessages = mock[Messages]

    val addressStep = new AddressStep(
      mockedJsonSerialiser,
      mockedConfig,
      mockedEncryptionService,
      mockedAddressService,
      mockedRemoteAssets,
      mockCrownControllers,
      mockMessages
    )

    val postcode = "EH1 1AA"

    when (mockedAddressService.isScotland(postcode)).thenReturn(true)
    val currentState = completeCrownApplication.copy(
      address = Some(LastAddress(None, Some(PartialAddress(None, None, postcode, None, None)))))

    val transferedState = addressStep.nextStep(currentState)
    transferedState should be (GoTo(ExitController.scotland))
  }
}
