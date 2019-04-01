package utils.io

import java.io.{BufferedWriter, File, FileWriter, IOException}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardCopyOption}

import scala.io.Source
import scala.util.{Failure, Success, Try}

object FileUtil {

  def fileFromPath(file: String): String = file.split('/').last.split('\\').last

  def append(filename: String, msg: String): Unit = {
    var bw: BufferedWriter = null
    try {
      bw = new BufferedWriter(new FileWriter(filename, true))
      bw.write(msg)
      bw.newLine()
      bw.flush()
    } catch  {
      case ioe: IOException => ioe.printStackTrace()
    } finally {
      if (bw!=null) try {
        bw.close()
      } catch {
        case _: Exception =>
      }
    }
  }

  def write(filename: String, content: String): Unit =
    Files.write(Paths.get(filename), content.getBytes(StandardCharsets.ISO_8859_1))

  def load(filename: String): String = {
    Try(Source.fromFile(filename).mkString) match {
      case Success(src) => src
      case Failure(_) => Source.fromFile(filename, enc = "UTF-8").mkString
    }
  }

  def loadResource(filename: String): String = {
    val src = if (filename.startsWith("/")) filename.substring(1) else filename
    val content = Source.fromResource(src)
    content.mkString
  }

  def loadLines(filename: String): List[String] = Source.fromFile(filename).getLines().toList

  def deleteFile(filename: String): Unit = Files.delete(Paths.get(filename))

  def getFiles(dir: String): List[String] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).map(_.toPath.toString).toList
    } else {
      List[String]()
    }
  }

  def download(url: String, fileToDownload: String, downloadPath: String, fileToSave: String): Unit = {
    val src = Source.fromURL(url + fileToDownload, "ISO-8859-1")
    write(downloadPath + fileToSave, src.mkString)
  }

  def readUrl(url: String): String = Source.fromURL(url, "ISO-8859-1").mkString

  def createDir(theFilePath: String): Boolean = {
    val directory = new File(theFilePath)
    if (directory.exists) false
    else directory.mkdirs
  }

  def copyFile(pathSrc: String, pathDest: String): Unit = {
    val srcPath = new File(pathSrc).toPath
    val destPath = new File(pathDest).toPath
    Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING)
  }

  def copyFromResource(pathSrc: String, pathDest: String): Unit = {
    val src = getClass.getResourceAsStream(pathSrc)
    Files.copy(src, Paths.get(pathDest), StandardCopyOption.REPLACE_EXISTING)
  }

}
