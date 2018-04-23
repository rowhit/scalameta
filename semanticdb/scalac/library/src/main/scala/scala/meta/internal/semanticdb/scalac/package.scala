package scala.meta.internal.semanticdb

import java.nio.file.Files
import java.nio.file.StandardOpenOption
import org.langmeta.internal.semanticdb.{vfs => v}
import org.langmeta.io.AbsolutePath
import scala.meta.internal.{semanticdb3 => s}

package object scalac {

  implicit class XtensionSchemaTextDocument(sdocument: s.TextDocument) {
    private def write(targetroot: AbsolutePath, append: Boolean): Unit = {
      val openOption =
        if (append) StandardOpenOption.APPEND
        else StandardOpenOption.TRUNCATE_EXISTING
      val out = v.SemanticdbPaths.toSemanticdb(sdocument, targetroot)
      if (!Files.exists(out.toNIO.getParent)) {
        Files.createDirectories(out.toNIO.getParent)
      }
      val fos = Files.newOutputStream(out.toNIO, StandardOpenOption.CREATE, openOption)
      try {
        s.TextDocuments(sdocument :: Nil).writeTo(fos)
      } finally {
        fos.close()
      }
    }
    def save(targetroot: AbsolutePath): Unit =
      write(targetroot, append = false)
    def append(targetroot: AbsolutePath): Unit =
      write(targetroot, append = true)
  }

}
