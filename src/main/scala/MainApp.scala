import java.awt.{Color, Dimension}

import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked

object MainApp extends SimpleSwingApplication {
  def top = new Frame {
    title = "Image processing and analysis."

    preferredSize = new Dimension(800, 400)

    val greyButton = new Button {
      text = "Desaturate"
      foreground = Color.blue
      enabled = true
    }

    val reloadButton = new Button {
      text = "Reload"
      foreground = Color.blue
      enabled = true
    }

    val inverseButton = new Button {
      text = "Inverse"
      foreground = Color.blue
      enabled = true
    }

    val contrastField = new TextArea

    val contrastButton = new Button {
      text = "Contrast"
      foreground = Color.blue
      enabled = true
    }

    val contrastGrid = new GridPanel(1, 2) {
      contents += contrastField
      contents += contrastButton
    }

    val verticalProjectionButton = new Button {
      text = "Vertical projection"
      foreground = Color.blue
      enabled = true
    }

    val horizontalProjectionButton = new Button {
      text = "Horizontal projection"
      foreground = Color.blue
      enabled = true
    }

    val buttonGrid = new GridPanel(4, 1) {
      contents += greyButton
      contents += inverseButton
      contents += contrastGrid
      contents += reloadButton
    }

    val imagePanel = new ImagePanel {
      imagePath = "/home/samba/kupidurap/biometrics/biometrics-image-processing-and-analysis/src/main/resources/eye.jpg"
    }

    val gridPanel = new GridPanel(1, 2) {
      contents += imagePanel
      contents += buttonGrid
    }

    contents = new BorderPanel {
      layout(gridPanel) = North
      layout(imagePanel) = Center
      layout(buttonGrid) = East
    }

    listenTo(greyButton)
    listenTo(inverseButton)
    listenTo(reloadButton)
    listenTo(contrastButton)

    reactions += {
      case ButtonClicked(comp) if comp == greyButton =>
        imagePanel.grey()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == inverseButton =>
        imagePanel.inverse()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == reloadButton =>
        imagePanel.reload()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == contrastButton =>
        imagePanel.contrast(contrastField.text.toInt)
        imagePanel.repaint()
    }
  }
}
