package com.appxs.apex.presentation.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun parseMarkdown(text: String): AnnotatedString {
    return buildAnnotatedString {
        // Remove tabs as requested
        val processedText = text.replace("\t", "")
        val lines = processedText.split("\n")
        
        lines.forEachIndexed { lineIndex, line ->
            val headerMatch = Regex("^(#{1,3})\\s+(.*)").find(line)
            val quoteMatch = Regex("^>\\s*(.*)").find(line)
            val unorderedListMatch = Regex("^[*+]\\s+(.*)").find(line)
            val orderedListMatch = Regex("^(\\d+\\.)\\s+(.*)").find(line)

            when {
                headerMatch != null -> {
                    val level = headerMatch.groupValues[1].length
                    val content = headerMatch.groupValues[2]
                    val fontSize = when (level) {
                        1 -> 24.sp
                        2 -> 20.sp
                        else -> 18.sp
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = fontSize)) {
                        parseInlineMarkdown(content)
                    }
                }
                quoteMatch != null -> {
                    val content = quoteMatch.groupValues[1]
                    withStyle(style = SpanStyle(color = Color.Gray, fontStyle = FontStyle.Italic)) {
                        append("    ") // Indentation for quote
                        parseInlineMarkdown(content)
                    }
                }
                unorderedListMatch != null -> {
                    val content = unorderedListMatch.groupValues[1]
                    append("  • ")
                    parseInlineMarkdown(content)
                }
                orderedListMatch != null -> {
                    val number = orderedListMatch.groupValues[1]
                    val content = orderedListMatch.groupValues[2]
                    append("  $number ")
                    parseInlineMarkdown(content)
                }
                else -> {
                    parseInlineMarkdown(line)
                }
            }
            
            if (lineIndex < lines.size - 1) {
                append("\n")
            }
        }
    }
}

private fun AnnotatedString.Builder.parseInlineMarkdown(text: String) {
    // Pattern to find Bold (**), Italic (*), and URLs
    val pattern = Regex("(\\*\\*.*?\\*\\*|\\*.*?\\*|https?://\\S+)")
    
    var lastMatchEnd = 0
    pattern.findAll(text).forEach { match ->
        append(text.substring(lastMatchEnd, match.range.first))
        
        val matchValue = match.value
        when {
            matchValue.startsWith("**") && matchValue.endsWith("**") -> {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(matchValue.removeSurrounding("**"))
                }
            }
            matchValue.startsWith("*") && matchValue.endsWith("*") -> {
                withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(matchValue.removeSurrounding("*"))
                }
            }
            matchValue.startsWith("http") -> {
                val linkStyle = SpanStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
                pushLink(
                    LinkAnnotation.Url(
                        url = matchValue,
                        styles = TextLinkStyles(style = linkStyle)
                    )
                )
                append(matchValue)
                pop()
            }
        }
        lastMatchEnd = match.range.last + 1
    }
    
    append(text.substring(lastMatchEnd))
}
