import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import uk.gov.gds.ier.filter.{AssetsCacheFilter, ResultFilter, StatsdFilter}

class Filters @Inject() (
                          statsdFilter: StatsdFilter,
                          resultFilter: ResultFilter,
                          assetsCacheFilter: AssetsCacheFilter
                        ) extends DefaultHttpFilters(statsdFilter, resultFilter, assetsCacheFilter)