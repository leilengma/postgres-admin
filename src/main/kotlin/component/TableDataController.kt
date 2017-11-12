package component

import common.deleteEntityById
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.layout.VBox
import tornadofx.add
import tornadofx.minus

class TableDataController():VBox(){
    private val dataTable = TableDataView()
    var tableName:String by dataTable

    init {
        add(dataTable)
        val tableToolbar = ToolBar()
        val tableRefresh = Button("Refresh")
        val addEntry = Button("Add")
        val delEntry = Button("Delete")
        tableRefresh.setOnAction {
            run {
                dataTable.refreshTableDetail(tableName)
            }
        }
        addEntry.setOnAction {
            run {

            }
        }
        delEntry.setOnAction {
            run {
                dataTable.selectionModel.selectedItems.forEach { entry ->
                    run {
                        val value = if (entry.keys.contains("id")) {
                            entry.get("id")
                        } else {
                            null
                        }
                        if (value != null)
                            deleteEntityById(tableName, value)
                    }
                }
            }
        }
        tableToolbar.items.addAll(tableRefresh, addEntry, delEntry)
        add(tableToolbar)
        dataTable.minHeightProperty().bind(this.heightProperty().minus(tableToolbar.heightProperty()))
    }
}