package st.seno.autotrading.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R
import st.seno.autotrading.extensions.FullWidthSpacer
import st.seno.autotrading.extensions.HeightSpacer
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.prefs.PrefsManager
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FF374151
import st.seno.autotrading.theme.FF626975
import st.seno.autotrading.theme.FF9CA3AF
import st.seno.autotrading.theme.FFE5E7EB
import st.seno.autotrading.theme.FFF9FAFB
import st.seno.autotrading.theme.FFFACC15

//region(CryptoDropDown)
@Composable
fun CryptoDropDown(
    selectedCrypto: String,
    isExpandCryptoDropDownMenu: Boolean,
    bookmarkedTickers: List<String>,
    onClickCryptoText: () -> Unit,
    onClickCryptoDropdownMenu: (Boolean) -> Unit,
    onClickCryptoDropdownMenuItem: (String) -> Unit
) {
    Box {
        SelectedAutoTradingCryptoTextPanel(
            selectedCrypto = selectedCrypto,
            onClickCryptoText = onClickCryptoText
        )
        AutoTradingCryptoDropDownMenu(
            isExpandDropDownMenu = isExpandCryptoDropDownMenu,
            bookmarkedTickers = bookmarkedTickers,
            onClickDropdownMenu = onClickCryptoDropdownMenu,
            onClickCryptoDropdownMenuItem = onClickCryptoDropdownMenuItem
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SelectedAutoTradingCryptoTextPanel(
    selectedCrypto: String,
    onClickCryptoText: () -> Unit
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
        8.HeightSpacer()
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = FFE5E7EB
            ),
            backgroundColor = FFF9FAFB,
            shape = RoundedCornerShape(size = 8.dp),
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource(),
                    onClick = onClickCryptoText
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    cryptoDisplayName,
                    style = TextStyle(
                        fontSize = 14.textDp,
                        fontWeight = FontWeight.Normal,
                        color = FF000000
                    ),
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = FF9CA3AF,
                    modifier = Modifier
                        .size(size = 24.dp)
                        .rotate(degrees = 90f)
                        .align(alignment = Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
fun AutoTradingCryptoDropDownMenu(
    isExpandDropDownMenu: Boolean,
    bookmarkedTickers: List<String>,
    onClickDropdownMenu: (Boolean) -> Unit,
    onClickCryptoDropdownMenuItem: (String) -> Unit
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
            .sortedWith(comparator = compareByDescending<Pair<String, String>> { "${it.first}-${it.second}" in bookmarkedTickers }.thenBy { it.second })
            .forEach { marketIdPair ->
                DropdownMenuItem(
                    text = {
                        val isBookmarked = bookmarkedTickers.find { it ==  "${marketIdPair.first}-${marketIdPair.second}"} != null
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                marketIdPair.second,
                                style = TextStyle(
                                    fontSize = 14.textDp,
                                    fontWeight = FontWeight.Normal,
                                    color = FF000000
                                )
                            )
                            FullWidthSpacer()
                            if (isBookmarked) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_full_star),
                                    contentDescription = null,
                                    tint = FFFACC15,
                                    modifier = Modifier
                                        .size(size = 16.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        onClickDropdownMenu.invoke(false)
                        onClickCryptoDropdownMenuItem.invoke("${marketIdPair.first}-${marketIdPair.second}")
                    }
                )
            }
    }
}
//endregion