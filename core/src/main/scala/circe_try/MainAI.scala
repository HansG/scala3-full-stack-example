package circe_try

import circe_try.CompletionsRequestBody.{CompletionsBody, MultiplePrompt, MultipleStop}
import circe_try.CompletionsResponse
import sttp.client3.*
import sttp.client3.circe.circeBodySerializer
import io.circe.syntax.*
import sttp.model.Uri 


//https://softwaremill.com/how-to-serialize-case-class-to-json-in-scala-3-and-scala-2-using-circe/
object MainAI extends App {
  val backend = HttpClientSyncBackend()

  val openAI = new OpenAI("secret-api-key")

  val body = CompletionsBody(
    "text-davinci-003",
    prompt = Some(MultiplePrompt(Seq("multiple prompt", "multiple prompt x 2"))),
    stop = Some(MultipleStop(Seq("multiple stop", "multiple stop x 2")))
  )

  val response = openAI.createCompletion(body).send(backend)
  println(response)
}

class OpenAI(authToken: String) {

  def createCompletion(completionBody: CompletionsBody): RequestT[Identity, Either[ResponseException[String, io.circe.Error], CompletionsResponse], Any] = {

    val json = completionBody.asJson.deepDropNullValues

    openApiAuthRequest
      .post(OpenAIUris.Completions)
      .body(json)
      .response(asJson[CompletionsResponse])
  }

  private val openApiAuthRequest: RequestT[Empty, Either[String, String], Any] = basicRequest.auth
    .bearer(authToken)

}

private object OpenAIUris {
  val Completions: Uri = uri"https://api.openai.com/v1/completions"
}


