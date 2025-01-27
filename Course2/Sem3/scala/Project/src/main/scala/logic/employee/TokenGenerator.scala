package logic.employee

import cats.effect.IO
import models.Aliases.Token
import pdi.jwt._

object TokenGenerator {

  private def pureGenerateToken(username: String, issuedAt: Long): Token =
    Jwt.encode(
      JwtClaim(
        content = s"""{"username": "$username", "issuedAt": $issuedAt}"""
      ),
      "super_secret",
      JwtAlgorithm.HS256
    )

  def generateToken(username: String): IO[Token] =
    for {
      current <- IO(System.currentTimeMillis() / 1000)
      token   <- IO(pureGenerateToken(username, current))
    } yield token

}
