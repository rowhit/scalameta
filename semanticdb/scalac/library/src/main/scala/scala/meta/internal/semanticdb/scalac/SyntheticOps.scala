package scala.meta.internal.semanticdb.scalac

import scala.meta.internal.{semanticdb => s}
import scala.meta.internal.semanticdb.Scala._
import scala.meta.internal.inputs._

trait SyntheticOps { self: SemanticdbOps =>
  import g._

  implicit class XtensionGTreeSTree(gTree: g.Tree) {

    def toSemanticTree: s.Tree = gTree match {
      case gTree: g.Apply =>
        s.ApplyTree(
          fn = gTree.fun.toSemanticId,
          args = gTree.args.map(_.toSemanticTree)
        )
      case gTree: g.TypeApply =>
        s.TypeApplyTree(
          fn = gTree.fun.toSemanticTree,
          targs = gTree.args.map(_.tpe.toSemanticTpe)
        )
      case gTree: g.Select => gTree.toSemanticId
      case gTree: g.Ident => gTree.toSemanticId
      case gTree: g.This => gTree.toSemanticId
      case gTree: g.Typed if gTree.hasAttachment[g.analyzer.MacroExpansionAttachment] =>
        s.MacroExpansionTree(
          tpe = gTree.tpt.tpe.toSemanticTpe
        )
      case _ =>
        s.NoTree
    }

    def toSemanticId: s.IdTree = s.IdTree(sym = gTree.symbol.toSemantic)

    def toSemanticOriginal: s.Tree = s.OriginalTree(
      range = Some(gTree.pos.toMeta.toRange)
    )

  }

}
