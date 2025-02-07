package st.seno.autotrading.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF2563EB
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF9CA3AF
import st.seno.autotrading.theme.FFF3F4F6

@Composable
fun CommonInputContainer(
    title: String,
    textValue: TextFieldValue,
    helperText: String = "",
    blockText: String = "",
    keyboardType: KeyboardType,
    enabled: Boolean = true,
    isBlock: Boolean = false,
    onTextChanged: (TextFieldValue) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            title,
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151
            )
        )
        8.HeightSpacer()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 40.dp)
                .clip(shape = RoundedCornerShape(size = 8.dp))
                .background(color = FFF3F4F6)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterStart)
            ) {
                BasicTextField(
                    value = textValue,
                    onValueChange = onTextChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType
                    ),
                    enabled = enabled,
                    maxLines = 1,
                    cursorBrush = SolidColor(value = FF2563EB),
                    modifier = Modifier
                        .align(alignment = Alignment.CenterStart)
                        .focusRequester(focusRequester)
                        .focusable(),
                    textStyle = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563,
                        textAlign = TextAlign.Start
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                        }
                    }
                )
                if (isBlock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = FFF3F4F6)
                    ) {
                        Text(
                            blockText,
                            style = TextStyle(
                                fontSize = 12.textDp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                color = FF000000,
                            ),
                            modifier = Modifier.align(alignment = Alignment.Center)
                        )
                    }
                }
            }
        }
        3.HeightSpacer()
        Text(
            helperText,
            style = TextStyle(
                fontSize = 10.textDp,
                fontWeight = FontWeight.Medium,
                color = FF9CA3AF
            )
        )
    }
}