
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MultiUserSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources(BlackList(""".*.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("zh-CN,zh;q=0.9,de-DE;q=0.8,de;q=0.7,en;q=0.6")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36")

	val headers_0 = Map(
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1")

	val headers_1 = Map(
		"Pragma" -> "no-cache",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1")



	val scn = scenario("MultiUserSimulation")
		.exec(http("request_0")
			.get("/connectfour")
			.headers(headers_0)
			.resources(http("request_1")
			.get("/connectfour")
			.headers(headers_1)))
		.pause(2)
		.exec(http("request_6")
			.get("/connectfour/save")
			.headers(headers_0)
			.resources(http("request_7")
			.get("/connectfour/save")
			.headers(headers_1)))
		.pause(2)
		.exec(http("request_8")
			.get("/connectfour/n%20small")
			.headers(headers_0)
			.resources(http("request_9")
			.get("/connectfour/n%20small")
			.headers(headers_1)))
		.pause(2)
		.exec(http("request_10")
			.get("/connectfour/load")
			.headers(headers_0)
			.resources(http("request_11")
			.get("/connectfour/load")
			.headers(headers_1)))

	setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)
}