package space.jay.wirebarley.core.common.visualTransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.lang.Integer.max
import java.text.NumberFormat

class VisualTransformationPrice : VisualTransformation {

    override fun filter(text : AnnotatedString) : TransformedText {
        val result = if (text.isEmpty()) "" else NumberFormat.getNumberInstance().format(text.text.toLong())
        return TransformedText(
            AnnotatedString(result),
            object : OffsetMapping {
                override fun originalToTransformed(offset : Int) : Int {
                    // 현재 보이는 text 에서 콤마 개수 구하기
                    val commaCountAll = result.length / 4
                    // 실제 입력된 text 에서 커서 뒤에 있는 text 개수 구하기
                    val offsetBehindCount = text.text.length - offset
                    // 실제 입력된 text 의 커서 뒤에 있는 수자를 3으로 나눠서 커서 뒤에 있어야 하는 콤마 개수 구하기
                    val commaCountBehindCorrectionOffset = offsetBehindCount / 3
                    // 커서 앞에 있는 콤마 개수를 구하기 위해 전체 콤마 개수에서 커서 뒤에 있는 콤마 만큼 제거
                    // 단, 123,456 에서 커서가 맨 앞에 있을 경우 예 |123,456 일경우 -1이 나오기 때문에 최소 값을 0으로 해줌
                    val commaCountFrontCorrectionOffset = (commaCountAll - commaCountBehindCorrectionOffset).coerceAtLeast(0)
                    // offset 과 커서 앞에 실제 존재하는 컴마 개수를 더하기
                    val sum = offset + commaCountFrontCorrectionOffset
                    // 만약 배치 되는 커서 값이 ',' 뒤라면 한칸 더 뒤로 커서를 이동
                    return if (sum > 0 && result[sum - 1] == ',') sum + 1 else sum
                }

                override fun transformedToOriginal(offset : Int) : Int {
                    return if (offset > 0) {
                        // 들어온 offset 값에서 커서가 콤마 뒤에 있을 경우 숫자 뒤로 넘기기 위해 1을 더함
                        val correctionOffset = if (result[offset - 1] == ',') offset + 1 else offset
                        // 현재 보이는 text 에서 콤마 개수 구하기
                        val commaCountAll = result.length / 4
                        // 커서 뒤에 있는 콤마의 갯수를 구하기
                        val commaCountBehindCorrectionOffset = (result.length - correctionOffset) / 4
                        // 커서 앞에 있는 콤마의 갯수를 구하기
                        val commaCountFrontCorrectionOffset = commaCountAll - commaCountBehindCorrectionOffset
                        // 실제 입력한 text 의 커서 값 구하기
                        return correctionOffset - commaCountFrontCorrectionOffset
                    } else {
                        offset
                    }
                }
            }
        )
    }
}