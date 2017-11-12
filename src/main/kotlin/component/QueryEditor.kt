import java.util.Collections
import java.util.regex.Matcher
import java.util.regex.Pattern

import javafx.application.Application
import javafx.scene.Scene
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

    init{
        val codeArea = CodeArea()
            codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)

        codeArea.richChanges()
        .filter { ch-> ch.getInserted() != ch.getRemoved() } // XXX
        .subscribe { change-> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())) }
            codeArea.replaceText(0, 0, sampleCode)

        add(codeArea)

    }

    companion object {

        private val KEYWORDS = arrayOf("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while")

        private val KEYWORD_PATTERN = "\\b(" + KEYWORDS.joinToString("|") + ")\\b"
        private val PAREN_PATTERN = "\\(|\\)"
        private val BRACE_PATTERN = "\\{|\\}"
        private val BRACKET_PATTERN = "\\[|\\]"
        private val SEMICOLON_PATTERN = "\\;"
        private val STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\""
        private val COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"

        private val PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<PAREN>" + PAREN_PATTERN + ")"
                        + "|(?<BRACE>" + BRACE_PATTERN + ")"
                        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
                        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        )

        private val sampleCode = arrayOf("package com.example;", "", "import java.util.*;", "", "public class Foo extends Bar implements Baz {", "", "    /*", "     * multi-line comment", "     */", "    public static void main(String[] args) {", "        // single-line comment", "        for(String arg: args) {", "            if(arg.length() != 0)", "                System.out.println(arg);", "            else", "                System.err.println(\"Warning: empty string as argument\");", "        }", "    }", "", "}").joinToString("\n")
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