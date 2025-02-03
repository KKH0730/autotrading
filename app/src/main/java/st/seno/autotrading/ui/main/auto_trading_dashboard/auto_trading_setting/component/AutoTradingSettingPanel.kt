package st.seno.autotrading.ui.main.auto_trading_dashboard.auto_trading_setting.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF4B5563
import st.seno.autotrading.theme.FF626975
import st.seno.autotrading.theme.FFE5E7EB
import st.seno.autotrading.theme.FFE7E9EA
import st.seno.autotrading.theme.FFF3F4F6
import st.seno.autotrading.theme.FFFFFFFF
import st.seno.autotrading.theme.Transparent

@Composable
fun AutoTradingSettingPanel(
    selectedCrypto: String,
    onClickDropdownMenuItem: (String) -> Unit
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(size = 8.dp),
        backgroundColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column {
            24.HeightSpacer()
            AutoTradingSettingTitle()
            25.HeightSpacer()
            TradingTargetCryptoDropDownMenu(
                selectedCrypto = selectedCrypto,
                onClickDropdownMenuItem = onClickDropdownMenuItem
            )
            25.HeightSpacer()
            KrwQuantity(maxKrwValue = "19999.45")
        }
    }
}

// region(title)
@Composable
fun AutoTradingSettingTitle() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            stringResource(R.string.auto_trading_setting_title),
            style = TextStyle(
                fontSize = 17.textDp,
                fontWeight = FontWeight.SemiBold,
                color = FF000000
            ),
            overflow = TextOverflow.Ellipsis,
        )
        this.FullWidthSpacer()
    }
}
// endregion

//region(CryptoDropDownMenu)
@Composable
fun TradingTargetCryptoDropDownMenu(
    selectedCrypto: String,
    onClickDropdownMenuItem: (String) -> Unit
) {
    var isExpandDropDownMenu by remember { mutableStateOf(false) }
    Box {
        SelectedAutoTradingCryptoTextPanel(
            selectedCrypto = selectedCrypto,
            onClick = { isExpandDropDownMenu = !isExpandDropDownMenu }
        )
        AutoTradingCryptoDropDownMenu(
            isExpandDropDownMenu = isExpandDropDownMenu,
            onClickDropdownMenu = { isExpandDropDownMenu = it },
            onClickDropdownMenuItem = onClickDropdownMenuItem
        )
    }
}

@Composable
fun SelectedAutoTradingCryptoTextPanel(
    selectedCrypto: String,
    onClick: () -> Unit
) {
    val cryptoDisplayName = selectedCrypto.split("-")[1]

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                stringResource(R.string.auto_trading_crypto_drop_down_menu_title),
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Medium,
                    color = FF374151
                )
            )
            2.WidthSpacer()
            Text(
                stringResource(R.string.auto_trading_crypto_drop_down_menu_title_additonal),
                style = TextStyle(
                    fontSize = 12.textDp,
                    fontWeight = FontWeight.Normal,
                    color = FF626975
                )
            )
        }
        5.HeightSpacer()
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = FFE5E7EB
                    ),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .clickable(onClick = onClick)
        ) {
            Row(modifier = Modifier.padding(all = 16.dp)) {
                Text(
                    cryptoDisplayName,
                    style = TextStyle(
                        fontSize = 14.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    )
                )
                FullWidthSpacer()
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = FF000000,
                    modifier = Modifier
                        .size(size = 14.dp)
                        .rotate(degrees = 90f)
                )
            }
        }
    }
}

@Composable
fun AutoTradingCryptoDropDownMenu(
    isExpandDropDownMenu: Boolean,
    onClickDropdownMenu: (Boolean) -> Unit,
    onClickDropdownMenuItem: (String) -> Unit
) {
    DropdownMenu(
        expanded = isExpandDropDownMenu,
        onDismissRequest = { onClickDropdownMenu.invoke(false) },
        scrollState = rememberScrollState(),
        offset = DpOffset(x = 24.dp, y = 0.dp),
        modifier = Modifier
            .width(width = 200.dp)
            .height(height = 300.dp)
    ) {
        PrefsManager.marketIdList.split(",")
            .map { it.split("-")[0] to it.split("-")[1] }
            .filter { it.first.uppercase() == "KRW" }
            .sortedBy { it.second }
            .forEach { marketIdPair ->
                DropdownMenuItem(
                    text = {
                        Text(
                            marketIdPair.second,
                            style = TextStyle(
                                fontSize = 14.textDp,
                                fontWeight = FontWeight.Normal,
                                color = FF000000
                            )
                        )
                    },
                    onClick = {
                        onClickDropdownMenu.invoke(false)
                        onClickDropdownMenuItem.invoke("${marketIdPair.second}/${marketIdPair.first}")
                    }
                )
            }
    }
}
//endregion

@Composable
fun KrwQuantity(maxKrwValue: String) {
    var quality by remember { mutableStateOf("") }
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
            onValueChange = {
                val newValue = try {
                    it.ifBlank { "0" }.toFloat()
                } catch (e: Exception) {
                    e.printStackTrace()
                    quality.ifBlank { "0" }.toFloat()
                }

                val value = if (newValue > maxKrwValue.toFloat()) maxKrwValue.toFloat() else newValue
                quality = value.toString()
                krwSliderValue = value


            }
        )
        12.HeightSpacer()
        KrwSlider(
            maxKrwValue = maxKrwValue,
            quality = krwSliderValue,
            onValueChange = {
                krwSliderValue = it
                quality = it.toString()
            }
        )
    }
}

@Composable
fun KrwInputContainer(
    quality: String,
    onValueChange: (String) -> Unit
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
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = quality,
            onValueChange = onValueChange,
            valueRange = 0f..maxKrwValue.toFloat(), // 슬라이더 범위
            colors = SliderDefaults.colors(),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Blue,
                    ),
                    enabled = false,
                    thumbSize = DpSize(24.dp, 24.dp),
                    modifier = Modifier.background(Color.Green)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(colors = SliderDefaults.colors(
                    inactiveTrackColor = Color.Yellow,
                    activeTrackColor = Color.Red
                ), enabled = true, sliderState = sliderState)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}