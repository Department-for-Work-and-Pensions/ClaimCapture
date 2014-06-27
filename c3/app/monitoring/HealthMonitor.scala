package monitoring

import com.codahale.metrics.health.{HealthCheckRegistry, HealthCheck}
import collection.JavaConversions._
import scala.collection.immutable.SortedMap
import play.api.Logger

object ProdHealthMonitor extends HealthMonitor

abstract class HealthMonitor {
  val registry = new HealthCheckRegistry()

  def register(label: String, healthCheck: HealthCheck) {
    registry.register(label, healthCheck)
  }

  def runHealthChecks(): SortedMap[String, HealthCheck.Result] = {
    SortedMap(registry.runHealthChecks().toSeq: _*)
  }

  def reportHealth() {
    runHealthChecks().map(healthCheck =>
      Logger.info(healthCheck.toString())
    )
  }
}
