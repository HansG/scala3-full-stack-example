package circe_try

import io.circe.Decoder
import io.circe.derivation.{Configuration, ConfiguredDecoder}
import io.circe.generic.semiauto.deriveDecoder

object CompletionsResponse {
  given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class CompletionsResponse(
                                  id: String,
                                  `object`: String,
                                  created: Int,
                                  model: String,
                                  choices: Seq[Choices],
                                  usage: Usage
                                )
  //  derives ConfiguredDecoder   - alternativ (semi-manuell):
  object CompletionsResponse {
    given Decoder[CompletionsResponse] = deriveDecoder[CompletionsResponse]
  }

  case class Choices(
                      text: String,
                      index: Int,
                      logprobs: Option[String],
                      finishReason: String
                    )
   // derives ConfiguredDecoder
   object Choices {
     given Decoder[Choices] = ConfiguredDecoder.derived  //(using config)
   }

  case class Usage(promptTokens: Int, completionTokens: Int, totalTokens: Int) 
    //derives ConfiguredDecoder

  object Usage {
    given Decoder[Usage] = ConfiguredDecoder.derived  //(using config)
  }
  

}