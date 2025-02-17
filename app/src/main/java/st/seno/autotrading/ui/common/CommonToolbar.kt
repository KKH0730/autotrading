package st.seno.autotrading.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import st.seno.autotrading.extensions.WidthSpacer
import st.seno.autotrading.extensions.textDp
import st.seno.autotrading.theme.FF000000
import st.seno.autotrading.theme.FFFFFFFF

@Composable
fun CommonToolbar(
    title: String,
    onClickBack: () -> Unit
) {
    Card(
        elevation = 2.dp,
        backgroundColor = FFFFFFFF,
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
    ) {
        Box {
            16.WidthSpacer()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(alignment = Alignment.CenterStart)

            ) {
                16.WidthSpacer()
                Box(
                    modifier = Modifier
                        .size(size = 36.dp)
                        .clip(shape = CircleShape)
                        .clickable { onClickBack.invoke() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = FF000000,
                        modifier = Modifier
                            .size(size = 20.dp)
                            .align(alignment = Alignment.Center)
                    )
                }
            }
            Text(
                title,
                style = TextStyle(
                    fontSize = 17.textDp,
                    fontWeight = FontWeight.SemiBold,
                    color = FF000000
                ),
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
    }
}