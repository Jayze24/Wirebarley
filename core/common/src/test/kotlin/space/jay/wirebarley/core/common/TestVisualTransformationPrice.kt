package space.jay.wirebarley.core.common

import androidx.compose.ui.text.AnnotatedString
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import space.jay.wirebarley.core.common.visualTransformation.VisualTransformationPrice
import java.text.NumberFormat

class TestVisualTransformationPrice {

    private val price = VisualTransformationPrice()

    @Test
    fun format_number_change() {
        (1..10).fold("") { acc ,i ->
            val fake = acc + i
            val expected = NumberFormat.getNumberInstance().format(fake.toLong())

            val formedText = price.filter(AnnotatedString(fake))
            assertThat(formedText.text.text).isEqualTo(expected)

            fake
        }
    }

    @Test
    fun format_number_empty() {
        val formedText = price.filter(AnnotatedString(""))
        assertThat(formedText.text.text).isEmpty()
    }

    @Test
    fun format_change_cursor() {
        val formedText = price.filter(AnnotatedString("1234567890123"))
        assertThat(formedText.text.text).isEqualTo("1,234,567,890,123")
        listOf(
            0 to 0,
            2 to 3,
            4 to 5,
            5 to 7,
            7 to 9,
            8 to 11,
            10 to 13,
            11 to 15,
            13 to 17
        ).forEach { fake ->
            val cursor = formedText.offsetMapping.originalToTransformed(fake.first)
            assertThat(cursor).isEqualTo(fake.second)
        }
    }

    @Test
    fun format_cursor_selection_to_original() {
        val formedText = price.filter(AnnotatedString("1234567890123"))
        assertThat(formedText.text.text).isEqualTo("1,234,567,890,123")
        listOf(
            0 to 0,
            3 to 2,
            5 to 4,
            7 to 5,
            9 to 7,
            11 to 8,
            13 to 10,
            15 to 11,
            17 to 13
        ).forEach { fake ->
            val cursor = formedText.offsetMapping.transformedToOriginal(fake.first)
            assertThat(cursor).isEqualTo(fake.second)
        }
    }
}