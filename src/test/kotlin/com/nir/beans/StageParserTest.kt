package com.nir.beans

import com.nir.utils.delete
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * Test for class [StageParser].
 */
class StageParserTest {

    private val kotlinObjectMapper = Beans.kotlinObjectMapper()
    private val reactionParser = StageParser

    @Test
    fun testParse() {
        datas().forEach {
            val leftCompounds = it[0] as List<String>
            val reactionSymbol = it[1] as String
            val rightCompounds = it[2] as List<String>
            val expectedJson = it[3] as String

            val actualRawReaction = reactionParser.parse("${add(leftCompounds)}$reactionSymbol${add(rightCompounds)}")
            val actualJson = kotlinObjectMapper.writeValueAsString(actualRawReaction)
            assertRawReaction(expectedJson, actualJson)

            val actualReaction = reactionParser.convert(actualRawReaction)
        }
    }

    private fun assertRawReaction(expected: String, actualJson: String?) {
        println("actualJson: $actualJson, \n  expected: $expected")
        Assertions.assertEquals(actualJson, expected)
    }

    fun datas(): Array<Array<Any>> {
        val s1 = "<=>"
        val lc1 = listOf("10H2SO4", "N3SO5", "Ge4Ua19To24")
        val rc1 = listOf("He2SiAu4", "12Ag3CO15")
        val expected = rawReaction(lc1, s1, rc1)

        val s2 = "=>"
        val lc2 = listOf("Au2Si4", "NO5")
        val rc2 = listOf("HeAu4", "Ag2O15")
        val expected1 = rawReaction(lc2, s2, rc2)

        val s3 = "->"
        val lc3 = listOf("Au2Si4", "NO5", "Ba4Re6", "AlBr10")
        val rc3 = listOf("HeAu4", "C2O15")
        val expected2 = rawReaction(lc3, s3, rc3)

        val s4 = "<->"
        val lc4 = listOf("Au5O7", "Br4Re6", "AlBr10")
        val rc4 = listOf("HeAu4", "Ag2O15")
        val expected3 = rawReaction(lc4, s4, rc4)
        return arrayOf(
                arrayOf(lc1, s1, rc1, expected),
                arrayOf(lc2, s2, rc2, expected1),
                arrayOf(lc3, s3, rc3, expected2),
                arrayOf(lc4, s4, rc4, expected3)
        )
    }

    private fun add(list: List<String>) = list.joinToString(truncated = "", separator = "+")

}

fun compound(amountAndCompound: String): String {
    var i = 0
    val amountSymbols = amountAndCompound.takeWhile { i++; it.isDigit(); }
    val compound = if (amountSymbols.isEmpty()) amountAndCompound else amountAndCompound.drop(i - 1)
    val amount = if (amountSymbols.isEmpty()) 1 else amountSymbols.toInt()

    return """{ "amount":$amount, "compound": {""" +
            compound
                    .split(StageParser.separateByCapital)
                    .joinToString(truncated = "") {
                        val split = it.split(StageParser.separateDigitsAndLetters)
                        val element = split[0]
                        val amount = when (split.size) {
                            1 -> 1
                            else -> split[1].toInt()
                        }
                        element(element, amount)
                    } +
            "}}"
}

private fun element(element: String, amount: Int) = """"$element": $amount"""

fun compounds(compounds: List<String>): String {
    return """[${compounds.joinToString(truncated = "") { compound(it) }}]"""
}

fun rawReaction(leftCompounds: List<String>, symbol: RawStageType, rightCompounds: List<String>): String {
    return """{"reagents": ${compounds(leftCompounds)},"stageType":"$symbol","products": ${compounds(rightCompounds)}}""".delete("\n", " ")
}
