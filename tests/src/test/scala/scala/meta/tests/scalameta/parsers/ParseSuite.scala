package scala.meta.tests
package scalameta
package parsers

import org.scalatest._
import scala.meta._
import scala.meta.internal.{ast => impl}
import scala.meta.internal.parsers.Parser
import MoreHelpers._

class ParseSuite extends FunSuite with CommonTrees {
  val EOL = scala.compat.Platform.EOL
  val escapedEOL = if (EOL == "\n") """\n""" else """\r\n"""
  def term(code: String)(implicit dialect: Dialect) = code.parseRule(_.expr())
  def pat(code: String)(implicit dialect: Dialect) = code.parseRule(_.pattern())
  def tpe(code: String)(implicit dialect: Dialect) = code.parseRule(_.typ())
  def topStat(code: String)(implicit dialect: Dialect) = code.parseRule(_.topStatSeq().head)
  def templStat(code: String)(implicit dialect: Dialect) = code.parseRule(_.templateStats().head)
  def blockStat(code: String)(implicit dialect: Dialect) = code.parseRule(_.blockStatSeq().head)
  def source(code: String)(implicit dialect: Dialect) = code.parseRule(_.compilationUnit())
  def tokenize(code: String)(implicit dialect: Dialect) = code.tokens
}

object MoreHelpers {
  implicit class XtensionCode(code: String) {
    def parseRule[T <: impl.Tree](rule: Parser => T)(implicit dialect: Dialect): T = new Parser(Input.String(code)).parseRule(rule)
  }
}
