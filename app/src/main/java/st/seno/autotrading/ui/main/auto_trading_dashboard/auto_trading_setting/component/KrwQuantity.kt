package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.filterDigit
import st.seno.autotrading.extensions.numberWithCommas
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF3B82F6
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FFE5E7EB
import st.seno.autotrading.theme.FFE7E9EA
import st.seno.autotrading.theme.FFF3F4F6
import st.seno.autotrading.theme.Transparent

@Composable
fun KrwQuantity(maxKrwValue: String) {
    var quality by remember { mutableStateOf(TextFieldValue("")) }
    var krwSliderValue by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.quantity),
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151
            )
        )
        8.HeightSpacer()
        KrwInputContainer(
            quality = quality,
            onValueChange = { textFieldValue ->
                quality = TextFieldValue(
                    text = textFieldValue.text.filterDigit().numberWithCommas(),
                    selection = TextRange(textFieldValue.text.filterDigit().numberWithCommas().length)
                )
                krwSliderValue = textFieldValue.text.filterDigit().toFloat()

            }
        )
        12.HeightSpacer()
        KrwSlider(
            maxKrwValue = maxKrwValue,
            quality = krwSliderValue,
            onValueChange = {
                krwSliderValue = it
                quality = TextFieldValue(
                    text = it.numberWithCommas(),
                    selection = TextRange(it.toString().length)
                )
            }
        )
    }
}

@Composable
fun KrwInputContainer(
    quality: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 45.dp)
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .background(color = FFF3F4F6)
    ) {
        TextField(
            value = quality,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            placeholder = {
                Text(
                    "Total",
                    style = TextStyle(
                        fontSize = 12.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF4B5563,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            textStyle = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                focusedContainerColor = Transparent,
                unfocusedContainerColor = Transparent,
            ),
            modifier = Modifier.weight(8f)
        )
        Spacer(
            modifier = Modifier
                .width(width = 1.dp)
                .fillMaxHeight()
                .padding(vertical = 5.dp)
                .background(color = FFE7E9EA)
        )
        Text(
            "KRW",
            style = TextStyle(
                fontSize = 12.textDp,
                fontWeight = FontWeight.Medium,
                color = FF374151,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .weight(weight = 2f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KrwSlider(
    maxKrwValue: String,
    quality: Float,
    onValueChange: (Float) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = quality,
            onValueChange = onValueChange,
            valueRange = 0f..maxKrwValue.toFloat(),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(
                        thumbColor = FF3B82F6,
                    ),
                    thumbSize = DpSize(16.dp, 16.dp),
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    colors = SliderDefaults.colors(
                        inactiveTrackColor = FFE5E7EB,
                        activeTrackColor = FF3B82F6
                    ),
                    sliderState = sliderState,
                    modifier = Modifier.height(height = 10.dp)
                )
            },
        )
    }
}