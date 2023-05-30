package circe_try

import CompletionsRequestBody.{CompletionsBody, Prompt, Stop}
import Prompt.*
import Stop.*
import circe_try.CompletionsResponse.*
import sttp.client3.*
import sttp.client3.circe.{asJson, circeBodySerializer}
import io.circe.syntax.*
import sttp.model.Uri
import org.junit.Test
import munit.CatsEffectSuite


//https://softwaremill.com/how-to-serialize-case-class-to-json-in-scala-3-and-scala-2-using-circe/


val CompletionsUri: Uri = uri"https://api.openai.com/v1/completions"

class OpenAI(authToken: String) {
  def createCompletion(completionBody: CompletionsBody): RequestT[Identity, Either[ResponseException[String, io.circe.Error], CompletionsResponse], Any] = {
    val json = completionBody.asJson.deepDropNullValues

    openApiAuthRequest
      .post(CompletionsUri)
      .body(json)
      .response(asJson[CompletionsResponse])
  }

  private val openApiAuthRequest: RequestT[Empty, Either[String, String], Any] = basicRequest.auth
    .bearer(authToken)
}



object MainAI extends App {
  val backend = HttpClientSyncBackend()

  val openAI = new OpenAI("secret-api-key")

  val response = openAI.createCompletion(BodyEncodeTry().bodyTry).send(backend)
  println(response)
}



