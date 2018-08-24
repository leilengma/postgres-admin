import common.DataStorage
import component.MainMenu
import component.TableDataController
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.scene.layout.VBox
import tornadofx.*


class MainView :View(){
    override val root = VBox()
    private val label = Label("")
    private val button = Button("Press Me")
    private val tableList = ListView(DataStorage.tables)
    private val counter = SimpleIntegerProperty(0)
    private val tableDataController = TableDataController()

    init {
        initLayout()
        initLogic()
    }

    private fun initLayout(){
        root += MainMenu()
        val mainPannel = SplitPane()
        mainPannel.prefHeightProperty().bind(root.heightProperty().divide(1.5))
        mainPannel.orientation = Orientation.HORIZONTAL
        mainPannel.prefWidthProperty().bind(root.widthProperty())
        val tablesView = VBox()
        tablesView.prefWidthProperty().bind(mainPannel.widthProperty().divide(3))
        tablesView += Label("Tables:")
        val searchText = TextField("")
        searchText.setOnKeyTyped {
            event -> run {
                searchText.text()
            }
        }
        tablesView += tableList
        val tablesToolbar = ToolBar()
        val tablesRefresh = Button("Refresh")
        tablesToolbar.items.add(tablesRefresh)
        tablesView += tablesToolbar
        mainPannel += tablesView
        mainPannel.add(tableDataController)
        tableList.prefHeightProperty().bind(tablesView.heightProperty())
        tableDataController.prefWidthProperty().bind(mainPannel.widthProperty().minus(tablesView.widthProperty()))
        root += mainPannel
        root += QueryEditor()

    }

    private fun initLogic(){
        label.textProperty().bind(counter.asString())
        button.setOnAction {
            e -> run{
                counter.set(counter.get()+1)
                DataStorage.tables.add("Object ${counter.get()}")
            }
        }
        tableList.onDoubleClick {
            run {
                val selectedTable = tableList.selectionModel.selectedItem
                tableDataController.tableName = selectedTable
            }
        }
    }
}