import java.awt.image.BufferedImage

import scala.util.Try

/**
  * Created by kupidurap on 10/13/16.
  */
object ImageProcessor {

  implicit class RichBufferedImage(val image: BufferedImage) {
    def pixels =
      (0 until image.getWidth) flatMap {
        x =>
          (0 until image.getHeight) map (x -> _)
      }

    def intensity(x: Int, y: Int): Option[Int] = Try {
      val rgb = image.getRGB(x, y)
      getR(rgb)
    }.toOption

    def setIntensity(x: Int, y: Int, i: Int) = {
      image.setRGB(x, y, toRGB(i, i, i))
    }
  }

  def grey(image: BufferedImage) =
    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)
        val average = (getR(rgb) + getG(rgb) + getB(rgb)) / 3

        image.setRGB(x, y, toRGB(average, average, average))
    }

  def inverse(image: BufferedImage) =
    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)

        val r = math.max(255 - getR(rgb), 0)
        val g = math.max(255 - getG(rgb), 0)
        val b = math.max(255 - getB(rgb), 0)

        image.setRGB(x, y, toRGB(r, g, b))
    }

  def brighten(image: BufferedImage, factor: Int) =
    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)

        val r = getR(rgb)
        val g = getG(rgb)
        val b = getB(rgb)

        val newR = r + factor
        val newG = g + factor
        val newB = b + factor

        image.setRGB(x, y, toRGB(clamp(newR), clamp(newG), clamp(newB)))
    }

  def contrast(image: BufferedImage, contrast: Int) =
    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)

        val r = getR(rgb)
        val g = getG(rgb)
        val b = getB(rgb)

        val f = (259 * (contrast + 255)) / (255 * (259 - contrast))

        val newR = f * (r - 128) + 128
        val newG = f * (g - 128) + 128
        val newB = f * (b - 128) + 128

        image.setRGB(x, y, toRGB(clamp(newR), clamp(newG), clamp(newB)))
    }

  def clamp(x: Int) = math.max(math.min(x, 255), 0)

  def threshold(image: BufferedImage, thresholdValue: Int) =
    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)

        val r = getR(rgb)
        val g = getG(rgb)
        val b = getB(rgb)

        val newVal =
          if (r > thresholdValue && g > thresholdValue && b > thresholdValue)
            255
          else
            0

        image.setRGB(x, y, toRGB(newVal, newVal, newVal))
    }

  def spread(image: BufferedImage) = {
    val values = image.pixels map {
      case (x, y) =>
        val rgb = image.getRGB(x, y)
        getR(rgb)
    }

    val maxVal = values.max
    val minVal = values.min

    val lut = (0 to 255).map {
      intensity =>
        intensity -> (255 * (intensity - minVal) / (maxVal - minVal))
    }.toMap

    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)
        val i = getR(rgb)
        val newI = lut.getOrElse(i, i)
        image.setRGB(x, y, toRGB(newI, newI, newI))
    }
  }

  def even(image: BufferedImage) = {
    val counts = image.pixels.foldLeft(Map.empty[Int, Int]) {
      case (acc, (x, y)) =>
        val rgb = image.getRGB(x, y)
        val i = getR(rgb)

        val count = acc.getOrElse(i, 0)
        acc.updated(i, count + 1)
    }

    val f = (0 to 255).map {
      intensity =>
        val sum = counts.filter(_._1 <= intensity).values.sum.toDouble
        intensity -> (sum / (image.getHeight * image.getWidth).toDouble)
    }.toMap

    val f0 = f collectFirst { case (key, value) if value > 0.0 => value } getOrElse 0.0

    val lut = (0 to 255).map {
      intensity =>
        intensity -> ((f(intensity) - f0) / (1.0 - f0)) * 255
    }.toMap.mapValues(_.toInt)

    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)
        val i = getR(rgb)
        val newI = lut.getOrElse(i, i)
        image.setRGB(x, y, toRGB(newI, newI, newI))
    }
  }

  def gauss(image: BufferedImage) =
    filter(
      Map(
        (-1, -1) -> 1,
        (-1, 0) -> 2,
        (-1, 1) -> 1,
        (0, 1) -> 2,
        (1, 1) -> 1,
        (1, 0) -> 2,
        (1, -1) -> 1,
        (0, -1) -> 2,
        (0, 0) -> 4
      )
    )(image)

  def mean(image: BufferedImage) =
    filter(
      Map(
        (-1, -1) -> 1,
        (-1, 0) -> 1,
        (-1, 1) -> 1,
        (0, 1) -> 1,
        (1, 1) -> 1,
        (1, 0) -> 1,
        (1, -1) -> 1,
        (0, -1) -> 1,
        (0, 0) -> 1
      )
    )(image)

  def sharpen(image: BufferedImage) =
    filter(
      Map(
        (-1, -1) -> 0,
        (-1, 0) -> -2,
        (-1, 1) -> 0,
        (0, 1) -> -2,
        (1, 1) -> 0,
        (1, 0) -> -2,
        (1, -1) -> 0,
        (0, -1) -> -2,
        (0, 0) -> 11
      )
    )(image)

  def filter(kernel: Map[(Int, Int), Int])(image: BufferedImage) = {
    val filtered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB)
    val weightsSum = kernel.values.sum

    image.pixels foreach {
      case (x, y) =>
        val weighted =
          kernel flatMap {
            case (pos, weight) =>
              image.intensity(pos._1 + x, pos._2 + y) map (_ * weight)
          }

        val newI = weighted.sum / weightsSum

        filtered.setIntensity(x, y, newI)
    }

    filtered
  }

  private def getR(in: Int) =
    ((in << 8) >> 24) & 0xff

  private def getG(in: Int) =
    ((in << 16) >> 24) & 0xff

  private def getB(in: Int) =
    ((in << 24) >> 24) & 0xff

  private def toRGB(r: Int, g: Int, b: Int) =
    (((r << 8) | g) << 8) | b
}
