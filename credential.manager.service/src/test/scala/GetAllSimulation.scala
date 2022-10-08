import io.gatling.core.Predef._
import io.gatling.http.Predef._


class GetAllSimulation extends Simulation {
  val httpConf = http.baseUrl("http://localhost:8889/general-api")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("Get All Simulation")
    .exec(http("Get All Simulation")
      .get("/credentials/"))
    .pause(1)

  setUp(
    scn.inject(atOnceUsers(200))
  ).protocols(httpConf)
}