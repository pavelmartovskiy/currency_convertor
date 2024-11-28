package com.pm.cc.presentation.screen.home.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pm.cc.R
import com.pm.cc.presentation.screen.HeaderMediumWhite
import com.pm.cc.presentation.theme.Blue

@Preview
@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp)
            .background(color = Blue)
    ) {
        HeaderMediumWhite(
            text = stringResource(id = R.string.currency_converter),
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 32.dp)

        )
    }
}