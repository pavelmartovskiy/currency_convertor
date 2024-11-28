package com.pm.cc.presentation.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun HeaderMediumGrey(
    text: String,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    color = com.pm.cc.presentation.theme.Gray,
    modifier = modifier
)

@Composable
fun HeaderMediumWhite(
    text: String,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    color = com.pm.cc.presentation.theme.White,
    modifier = modifier
)

@Composable
fun BodyMedium(
    text: String,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    color = com.pm.cc.presentation.theme.Black,
    modifier = modifier
)


@Composable
fun ArrowIcon(
    @DrawableRes iconId: Int,
    bgColor: Color,
    modifier: Modifier
) = Icon(
    painter = painterResource(id = iconId),
    contentDescription = null,
    tint = Color.White,
    modifier = modifier
        .size(size = 36.dp)
        .background(color = bgColor, shape = RoundedCornerShape(20.dp))
)