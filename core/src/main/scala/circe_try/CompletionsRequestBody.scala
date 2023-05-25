package circe_try

import io.circe.{Encoder, Json}
import io.circe.derivation.{Configuration, ConfiguredEncoder}

object CompletionsRequestBody {

  given Configuration = Configuration.default.withSnakeCaseMemberNames

  case class CompletionsBody(
                              model: String,
                              prompt: Option[Prompt] = None,
                              suffix: Option[String] = None,
                              maxTokens: Option[Int] = None,
                              temperature: Option[Double] = None,
                              topP: Option[Double] = None,
                              n: Option[Int] = None,
                              logprobs: Option[Int] = None,
                              echo: Option[Boolean] = None,
                              stop: Option[Stop] = None,
                              presencePenalty: Option[Double] = None,
                              frequencyPenalty: Option[Double] = None,
                              bestOf: Option[Int] = None,
                              logitBias: Option[Map[String, Float]] = None,
                              user: Option[String] = None
                            )
    //derives ConfiguredEncoder

  object CompletionsBody {
    given Encoder[CompletionsBody] = ConfiguredEncoder.derived  //alternativ explizit:(using config) -
    // zuvor: private val config = Configuration.default.withSnakeCaseMemberNames
  }

  sealed trait Prompt

  object Prompt {
    given Encoder[Prompt] = {
      case SinglePrompt(value)    => Json.fromString(value)
      case MultiplePrompt(values) => Json.arr(values.map(Json.fromString): _*)
    }
  }

  case class SinglePrompt(value: String) extends Prompt
  case class MultiplePrompt(values: Seq[String]) extends Prompt

  sealed trait Stop
  object Stop {
    given Encoder[Stop] = {
      case SingleStop(value)    => Json.fromString(value)
      case MultipleStop(values) => Json.arr(values.map(Json.fromString): _*)
    }
  }

  case class SingleStop(value: String) extends Stop
  case class MultipleStop(values: Seq[String]) extends Stop
}