package st.seno.autotrading.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import st.seno.autotrading.R

@Composable
fun CryptoIcon() {
    Box(
        modifier = Modifier.size(size = 24.dp)
            .clip(shape = RoundedCornerShape(size = 100.dp))
    ) {
        Icon(
            painterResource(R.drawable.ic_bitcoin_default),
            contentDescription = "image",
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}