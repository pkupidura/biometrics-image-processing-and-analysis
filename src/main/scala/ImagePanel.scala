import swing._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImagePanel extends Panel
{
  private var _imagePath = ""
  private var bufferedImage: BufferedImage = _

  def imagePath = _imagePath

  def imagePath_= (value: String)
  {
    _imagePath = value
    bufferedImage = ImageIO.read(new File(_imagePath))
  }

  def getImage() = bufferedImage

  def setImage(image: BufferedImage) =
    bufferedImage = image

  def reload() =
    bufferedImage = ImageIO.read(new File(_imagePath))

  def gauss() =
    bufferedImage = ImageProcessor.gauss(bufferedImage)

  def spread() =
    ImageProcessor.spread(bufferedImage)

  def even() =
    ImageProcessor.even(bufferedImage)

  def inverse() =
    ImageProcessor.inverse(bufferedImage)

  def contrast(c: Int) =
    ImageProcessor.contrast(bufferedImage, c * 2)

  def brighten(b: Int) =
    ImageProcessor.brighten(bufferedImage, b)

  def grey() =
    ImageProcessor.grey(bufferedImage)

  def threshold(value: Int) =
    ImageProcessor.threshold(bufferedImage, value)

  override def paintComponent(g: Graphics2D) =
  {
    if (null != bufferedImage) g.drawImage(bufferedImage, 0, 0, null)
  }
}

object ImagePanel
{
  def apply() = new ImagePanel()
}