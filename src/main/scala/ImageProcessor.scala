import java.awt.image.BufferedImage

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

  def contrast(image: BufferedImage, contrast: Int) =
    image.pixels foreach {
      case (x, y) =>
        val rgb = image.getRGB(x, y)

        val r = getR(rgb)
        val g = getG(rgb)
        val b = getB(rgb)

        val newR = math.min(math.max(contrast * (r - 128), 0), 255)
        val newG = math.min(math.max(contrast * (g - 128), 0), 255)
        val newB = math.min(math.max(contrast * (b - 128), 0), 255)

        image.setRGB(x, y, toRGB(newR, newG, newB))
    }

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

  private def getR(in: Int) =
    ((in << 8) >> 24) & 0xff

  private def getG(in: Int) =
    ((in << 16) >> 24) & 0xff

  private def getB(in: Int) =
    ((in << 24) >> 24) & 0xff

  private def toRGB(r: Int, g: Int, b: Int) =
    (((r << 8)|g) << 8)|b
}
