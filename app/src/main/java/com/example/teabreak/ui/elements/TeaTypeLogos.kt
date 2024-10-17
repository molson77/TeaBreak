package com.example.teabreak.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.teabreak.R
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.ui.theme.TeaTheme

@Composable
fun TeaTypeLogo(
    modifier: Modifier = Modifier,
    teaType: TeaType
) {
    TeaTheme(teaType = teaType) {
        Surface(
            modifier = modifier.size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.tea_leaf),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

