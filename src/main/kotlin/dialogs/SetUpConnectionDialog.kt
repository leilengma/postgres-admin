package dialogs

import com.fasterxml.jackson.module.kotlin.*
import common.ConnectionInfo
import common.Credential
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.control.PasswordField
import java.io.File
import HelloWorldApp
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.collections.FXCollections

class SetUpConnectionDialog:Dialog<ButtonBar.ButtonData>(){
    data class Setting(val tag:String, val url:String, val port:String, var dbName:String,
                       val role:String, val pwd:String){
        override fun toString(): String {
            return tag
        }
    }

    var msg = SimpleStringProperty("")
    val savedSettings = FXCollections.observableArrayList<Setting>()
    var selectedSettingIndex = -1

    init {
        refreshSettingCombo()
        //val setting = Setting("ff", "local", "23123", "subscribe24", "ffee", "dfewfewf")
        //mapper.writeValue(config, setting)
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
        password.textProperty().bindBidirectional(Credential.password)
        val settingCombo = ComboBox<Setting>(savedSettings)
        settingCombo.selectionModel.selectedItemProperty().addListener({
            widget, oldValue, newValue -> kotlin.run {
                selectedSettingIndex = savedSettings.indexOf(newValue)
                ConnectionInfo.url.set(newValue.url)
                ConnectionInfo.port.set(newValue.port)
                ConnectionInfo.dbName.set(newValue.dbName)
                Credential.username.set(newValue.role)
                Credential.password.set(newValue.pwd)
            }
        })
        grid.add(Label("Saved:"), 0, 0)
        grid.add(settingCombo, 1, 0)
        grid.add(Label("Url:"), 0,1)
        grid.add(url, 1, 1)
        grid.add(Label("Port:"), 0,2)
        grid.add(port, 1, 2)
        grid.add(Label("DB Name:"), 0,3)
        grid.add(dbName, 1, 3)
        grid.add(Label("Role:"), 0, 4)
        grid.add(username, 1, 4)
        grid.add(Label("Password:"), 0, 5)
        grid.add(password, 1, 5)
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
        grid.add(connect, 0, 6)
        grid.add(messageLabel, 1, 6)
        val addSettingButton = Button("Save Setting")
        val removeSettingButton = Button("Delete Chosen Setting")
        val tagLabel = TextField()
        grid.add(tagLabel, 0, 7)
        grid.add(addSettingButton, 1, 7)
        grid.add(removeSettingButton, 2, 7)
        addSettingButton.setOnAction {
            kotlin.run {
                savedSettings.add(Setting(tagLabel.text, ConnectionInfo.url.get(), ConnectionInfo.port.get(),
                        ConnectionInfo.dbName.get(), Credential.username.get(), Credential.password.get()))
                saveSettings()
            }
        }
        removeSettingButton.setOnAction {
            kotlin.run {
                if(selectedSettingIndex != -1) {
                    ConnectionInfo.dbName.set("")
                    ConnectionInfo.port.set("")
                    ConnectionInfo.url.set("")
                    Credential.password.set("")
                    Credential.username.set("")
                    savedSettings.remove(selectedSettingIndex, selectedSettingIndex+1)
                    saveSettings()
                }
            }
        }
        val establishButton = ButtonType("Connect", ButtonBar.ButtonData.OK_DONE)
        val cancel = ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        dialogPane.content = grid
        dialogPane.buttonTypes.addAll(cancel, establishButton)
        setResultConverter {
            param: ButtonType? ->  param?.buttonData
        }
    }

    private fun loadSettings():List<Setting>{
        val config =File(HelloWorldApp::class.java!!.getResource("credentials.json").file)
        val JSON = jacksonObjectMapper()
        return JSON.readValue<List<Setting>>(config)
    }

    private fun saveSettings(){
        val config =File(HelloWorldApp::class.java!!.getResource("credentials.json").file)
        val JSON = jacksonObjectMapper()
        JSON.writeValue(config, savedSettings.toList())
    }

    private fun refreshSettingCombo(){
        savedSettings.clear()
        selectedSettingIndex = -1
        val settings = loadSettings()
        settings.forEach {
            setting:Setting -> kotlin.run {
                savedSettings.add(setting)
            }
        }
    }
}