package component

import common.establishConnection
import common.getAllTables
import dialogs.ErrorAlertWithException
import dialogs.SetUpConnectionDialog
import javafx.scene.control.*
import java.util.*


class MainMenu: MenuBar()
{

    init {
        val connection = Menu("Connection")
        val establish = MenuItem("Establish")
        establish.setOnAction {
            run {
                val result:Optional<ButtonBar.ButtonData> = SetUpConnectionDialog().showAndWait()
                if(result.isPresent)
                {
                    if(result.get() == ButtonBar.ButtonData.OK_DONE)
                        try {
                            establishConnection()
                            getAllTables()
                        }catch (e:Exception){
                            val alert = ErrorAlertWithException(e)
                            alert.showAndWait()
                        }
                }
            }
        }
        connection.items.add(establish)
        menus.add(connection)
    }

}