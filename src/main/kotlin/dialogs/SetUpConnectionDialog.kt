package dialogs

import common.ConnectionInfo
import common.Credential
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.control.PasswordField


class SetUpConnectionDialog:Dialog<ButtonBar.ButtonData>(){
    var msg = SimpleStringProperty("")
    init {
        title = "Setup Connection"
        val grid = GridPane()
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(20.0, 150.0, 10.0, 10.0)
        val url = TextField()
        url.textProperty().bindBidirectional(ConnectionInfo.url)
        var port = TextField()
        port.textProperty().bindBidirectional(ConnectionInfo.port)
        var dbName = TextField()
        dbName.textProperty().bindBidirectional(ConnectionInfo.dbName)
        val username = TextField()
        username.textProperty().bindBidirectional(Credential.username)
        val password = PasswordField()
        username.textProperty().bindBidirectional(Credential.password)

        grid.add(Label("Url:"), 0,0)
        grid.add(url, 1, 0)
        grid.add(Label("Port:"), 0,1)
        grid.add(port, 1, 1)
        grid.add(Label("DB Name:"), 0,2)
        grid.add(dbName, 1, 2)
        grid.add(Label("Role:"), 0, 3)
        grid.add(username, 1, 3)
        grid.add(Label("Password:"), 0, 4)
        grid.add(password, 1, 4)
        val connect = Button("Test Connection")
        connect.setOnAction {
            run {
                try{
                    common.establishConnection()
                    msg.set("OK!")
                }catch (e:Exception){
                    msg.set(e.message)
                }
            }
        }
        val messageLabel = Label()
        messageLabel.textProperty().bind(msg)
        grid.add(connect, 0, 5)
        grid.add(messageLabel, 1, 5)
        val establishButton = ButtonType("Connect", ButtonBar.ButtonData.OK_DONE)
        val cancel = ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.content = grid
        dialogPane.buttonTypes.addAll(cancel, establishButton)
        setResultConverter {
            param: ButtonType? ->  param?.buttonData
        }
    }
}