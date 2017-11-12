import common.connection
import javafx.application.Application
import javafx.stage.Stage
import tornadofx.*


class HelloWorldApp: App(MainView::class, Styles::class){
    override fun start(stage: Stage) {
        stage.run {
            minWidth = 800.0
            minHeight = 600.0
        }

        super.start(stage)
        stage.scene.stylesheets.add(HelloWorldApp::class.java!!.getResource("java-keywords.css").toExternalForm())
    }

    override fun stop() {
        super.stop()
        connection?.close()
    }


}

class Styles: Stylesheet() {
    init {
//        label {
//            fontSize = 13.px
//            fontWeight = FontWeight.NORMAL
//        }
//        button {
//            fontSize = 13.px
//            fontWeight = FontWeight.NORMAL
//        }

    }
}

fun main(args: Array<String>) {
    Application.launch(HelloWorldApp::class.java, *args)
}
