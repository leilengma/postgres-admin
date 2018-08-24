package component

import common.DataStorage
import common.getAllDataFromTable
import common.getCollumsName
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import tornadofx.observable
import tornadofx.onChange
import kotlin.reflect.KProperty

class TableDataView:TableView<Map<String, String>>(){

    private var tablename =""

    init {
        editableProperty().set(true)
        DataStorage.customQueryData.onChange {
            run{
                refreshData(DataStorage.customQueryData)
            }
        }
    }


    operator fun getValue(controller:TableDataController, properties:KProperty<*>): String {
        return tablename
    }

    operator fun setValue(controller:TableDataController, properties:KProperty<*>, value:String){
        tablename = value
        refreshTableDetail(value)
    }

    fun refreshTableDetail(tableName:String){
        getAllDataFromTable(tableName, getCollumsName(tableName))
    }

    fun refreshColumns(columns:List<String>){
        getColumns().clear()
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

    }

    fun refreshData(data:List<Map<String,String>>){
        items.clear()
        if (data.isEmpty())
            return
        getColumns().clear()
        val columns = data[0].keys.toList()
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