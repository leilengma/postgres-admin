package component

import common.getAllDataFromTable
import common.getCollumsName
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import kotlin.reflect.KProperty

class TableDataView:TableView<Map<String, String>>(){
    var columns:List<String> = ArrayList<String>()
    var data:List<Map<String, String>> = ArrayList<HashMap<String, String>>()
    private var tablename =""

    init {
        editableProperty().set(true)
    }

    operator fun getValue(controller:TableDataController, properties:KProperty<*>): String {
        return tablename
    }

    operator fun setValue(controller:TableDataController, properties:KProperty<*>, value:String){
        tablename = value
        refreshTableDetail(value)
    }

    fun refreshTableDetail(tableName:String){
        getColumns().clear()
        items.clear()
        columns = getCollumsName(tableName)
        data = getAllDataFromTable(tableName, columns)
        for(column in columns){
            val tableColumn = TableColumn<Map<String, String>, String>(column)
            tableColumn.setCellValueFactory {
                param: TableColumn.CellDataFeatures<Map<String, String>, String>?
                -> SimpleStringProperty(param?.value?.get(column))
            }
            tableColumn.setCellFactory {
                param: TableColumn<Map<String, String>, String>? ->
                    DataCell()
            }
            getColumns().add(tableColumn)
        }
        items.addAll(data)
    }
}