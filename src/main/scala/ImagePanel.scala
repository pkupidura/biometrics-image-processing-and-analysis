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

  def reload() =
    bufferedImage = ImageIO.read(new File(_imagePath))

  def inverse() =
    ImageProcessor.inverse(bufferedImage)

  def contrast(c: Int) =
    ImageProcessor.contrast(bufferedImage, c)

  def grey() =
    ImageProcessor.grey(bufferedImage)

  override def paintComponent(g: Graphics2D) =
  {
    if (null != bufferedImage) g.drawImage(bufferedImage, 0, 0, null)
  }
}

object ImagePanel
{
  def apply() = new ImagePanel()
}