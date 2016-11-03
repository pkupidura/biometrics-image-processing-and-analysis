import java.awt.{Color, Dimension}

import scala.swing._
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked

object MainApp extends SimpleSwingApplication {
  def top = new Frame {
    title = "Image processing and analysis."

    preferredSize = new Dimension(1500, 800)

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

    val thresholdField = new TextArea

    val thresholdButton = new Button {
      text = "Threshold"
      foreground = Color.blue
      enabled = true
    }

    val thresholdGrid = new GridPanel(1, 2) {
      contents += thresholdField
      contents += thresholdButton
    }

    val brightenField = new TextArea

    val brightenButton = new Button {
      text = "Brighten"
      foreground = Color.blue
      enabled = true
    }

    val brightenGrid = new GridPanel(1, 2) {
      contents += brightenField
      contents += brightenButton
    }

    val verticalProjectionThreshold = new TextArea

    val verticalProjectionButton = new Button {
      text = "Rozciąganie histogramu"
      foreground = Color.blue
      enabled = true
    }

    val verticalProjectionGrid = new GridPanel(1, 2) {
      contents += verticalProjectionThreshold
      contents += verticalProjectionButton
    }

    val widthenHistButton = new Button {
      text = "Spread"
      foreground = Color.blue
      enabled = true
    }

    val evenHistButton = new Button {
      text = "Even"
      foreground = Color.blue
      enabled = true
    }

    val horizontalProjectionThreshold = new TextArea

    val horizontalProjectionButton = new Button {
      text = "Wyrównanie histogramu"
      foreground = Color.blue
      enabled = true
    }

    val horizontalProjectionGrid = new GridPanel(1, 2) {
      contents += horizontalProjectionThreshold
      contents += horizontalProjectionButton
    }

    val gaussButton = new Button {
      text = "Gauss"
      foreground = Color.blue
      enabled = true
    }

    val meanButton = new Button {
      text = "Mean"
      foreground = Color.blue
      enabled = true
    }

    val sharpenButton = new Button {
      text = "Sharpen"
      foreground = Color.blue
      enabled = true
    }

    val buttonGrid = new GridPanel(6, 1) {
      contents += greyButton
      contents += reloadButton
      contents += widthenHistButton
      contents += evenHistButton
      contents += gaussButton
      contents += sharpenButton
      contents += meanButton
      resizable = true
      maximumSize = new Dimension(100, 1000)
    }

    val imagePanel = new ImagePanel {
      imagePath = "/home/konalio/biometrics-image-processing-and-analysis/src/main/resources/eye.jpg"
    }

    val profilePanel = new ImagePanel {
      imagePath = "/home/konalio/biometrics-image-processing-and-analysis/src/main/resources/empty.jpg"
    }

    val imageGrid = new GridPanel(2, 1) {
      contents += imagePanel
      resizable = true
      minimumSize = new Dimension(700, 800)
    }

    contents = new GridPanel(1, 2) {
      contents += imageGrid
      contents += buttonGrid
    }

    listenTo(greyButton)
    listenTo(inverseButton)
    listenTo(reloadButton)
    listenTo(contrastButton)
    listenTo(thresholdButton)
    listenTo(brightenButton)
    listenTo(verticalProjectionButton)
    listenTo(horizontalProjectionButton)

    listenTo(widthenHistButton)
    listenTo(evenHistButton)
    listenTo(gaussButton)
    listenTo(meanButton)
    listenTo(sharpenButton)

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

      case ButtonClicked(comp) if comp == thresholdButton =>
        imagePanel.threshold(thresholdField.text.toInt)
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == brightenButton =>
        imagePanel.brighten(brightenField.text.toInt)
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == widthenHistButton =>
        imagePanel.spread()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == evenHistButton =>
        imagePanel.even()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == gaussButton =>
        imagePanel.gauss()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == meanButton =>
        imagePanel.mean()
        imagePanel.repaint()

      case ButtonClicked(comp) if comp == sharpenButton =>
        imagePanel.sharpen()
        imagePanel.repaint()

    }
  }
}
