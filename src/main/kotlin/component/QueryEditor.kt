import common.executeCustomQuery
import java.util.Collections
import java.util.regex.Matcher
import java.util.regex.Pattern

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import tornadofx.add

class QueryEditor: VBox() {
    private val PATTERN = generatePattern()

    init{

        val codeArea = CodeArea()
            codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)

        codeArea.richChanges()
        .filter { ch-> ch.getInserted() != ch.getRemoved() } // XXX
        .subscribe { change-> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())) }
        val toolbar = ToolBar()
        val executeButton = Button("Execute")
        executeButton.setOnAction {
            run {
                executeCustomQuery(codeArea.text)
            }
        }
        toolbar.items.add(executeButton)
        add(toolbar)
        add(codeArea)
    }

    fun generatePattern(): Pattern {
        val keyWordsText = QueryEditor::class.java!!.getResource("sql_key_words").readText()
        val KEYWORDS:List<String> = keyWordsText.replace('\t',' ').replace('\n',' ').split(" ")
        val KEYWORDS_LOWER:List<String> = KEYWORDS.map {
            keyWordsText -> run {
                keyWordsText.toLowerCase()
            }
        }
        val KEYWORD_PATTERN = "\\b(" + (KEYWORDS+KEYWORDS_LOWER).joinToString("|") + ")\\b"
        val PAREN_PATTERN = "\\(|\\)"
        val BRACE_PATTERN = "\\{|\\}"
        val BRACKET_PATTERN = "\\[|\\]"
        val SEMICOLON_PATTERN = "\\;"
        val STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\""
        val COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"
        val PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        )
        return PATTERN
    }

    private fun computeHighlighting(text:String):StyleSpans<Collection<String>> {
        val matcher = PATTERN.matcher(text)
        var lastKwEnd = 0
        val spansBuilder = StyleSpansBuilder<Collection<String>>()
        while (matcher.find()) {
            val styleClass = if (matcher.group("KEYWORD") != null)
                "keyword"
            else if (matcher.group("PAREN") != null)
                "paren"
            else if (matcher.group("BRACE") != null)
                "brace"
            else if (matcher.group("BRACKET") != null)
                "bracket"
            else if (matcher.group("SEMICOLON") != null)
                "semicolon"
            else if (matcher.group("STRING") != null)
                "string"
            else if (matcher.group("COMMENT") != null)
                "comment"
            else
                null /* never happens */

            spansBuilder.add(emptyList(), matcher.start() - lastKwEnd)
            if (styleClass != null)
                spansBuilder.add(setOf<String>(styleClass), matcher.end() - matcher.start())
            lastKwEnd = matcher.end()
        }
        spansBuilder.add(emptyList(), text.length - lastKwEnd)
        return spansBuilder.create()
    }

}