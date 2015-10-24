import scalax.file.PathMatcher._
import scalax.file.{Path, PathSet}

object Main {
  def main(args: Array[String]): Unit = {
    println("START MAIN")
    
    if (args.length >= 1) {
      Path.fromString(args(0)) match {
        case IsDirectory(dirPath) => sortingFiles(dirPath)
        case _ => println("有効なフォルダのパスを指定してください")
      }
    } else {
      println("対象フォルダのパスを指定してください")
    }

    println("END MAIN")
  }

  def sortingFiles(dirPath: Path):Unit = {
    //サブフォルダ内は見ない
    val ch: PathSet[Path] = dirPath.*("*.*")
    //val ch: PathSet[Path] = dirPath * "*.*"
    
    //パス → ファイル → 最終更新日と同一のフォルダが無ければ作成して該当フォルダにファイル移動
    ch.toList.collect{
      case IsFile(fp) => (Path.fromString(dirPath.path + "\\" + fmtDate(fp.lastModified)), fp)
    }.foreach{p =>
      System.out.println(p._1)
      System.out.println(p._2)
      p._1.createDirectory(failIfExists=false)
      p._2.moveTo(Path.fromString(p._1.path + "\\" + p._2.name))
    }
  }

  def fmtDate(lngDate: Long): String = {
    val sdf:java.text.SimpleDateFormat = new java.text.SimpleDateFormat("yyyyMMdd")
    sdf.format(new java.util.Date(lngDate))
  }
}
