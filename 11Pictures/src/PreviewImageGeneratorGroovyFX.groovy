import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.paint.Color

import static groovyx.javafx.GroovyFX.start

start {
    stage(title: 'http://www.11pictures.com front page preview', visible: true, resizable: true) {
        scene(width: 1240, height: 800) {
            scrollPane(style: "-fx-background-color: black;") {
                tilePane(prefColumns: 4, hgap: 50, vgap: 50) {
                    File[] directories = new ImageFinder().findPreviewImages()
                    directories.each { File file ->
                        String url = new File(file.absolutePath, "preview.jpg").toURI().toString()
                        Image image = new Image(url)
                        double imageHeight = ((image.height * 221) / image.width)

                        group {
                            imageView(layoutX: 0, layoutY: 0, fitWidth: 221, image: new Image(url), preserveRatio: true, smooth: true)
                            rectangle(layoutX: -2, layoutY: -2, width: 221 + 6, height: imageHeight + 6, arcHeight: 26, arcWidth: 26, stroke: Color.BLACK, strokeWidth: 5, fill: null)
                            rectangle(layoutX: 1, layoutY: 1, width: 221, height: imageHeight, arcHeight: 26, arcWidth: 26, stroke: Color.rgb(0x40, 0x34, 0x00), strokeWidth: 3, fill: null)
                            rectangle(layoutX: 5, layoutY: 5, width: 221 - 8, height: imageHeight - 8, arcHeight: 15, arcWidth: 15, stroke: Color.BLACK, strokeWidth: 2, fill: null)
                            rectangle(layoutX: 3, layoutY: 3, width: 221 - 4, height: imageHeight - 4, arcHeight: 20, arcWidth: 20, stroke: Color.GOLDENROD, strokeWidth: 2, fill: null)
                            label(layoutX: 0, layoutY: 170, minWidth: 221, maxWidth: 221, text: file.name, textFill: Color.GOLD, alignment: Pos.CENTER)
                        }
                    }
                }
            }
        }
    }
}