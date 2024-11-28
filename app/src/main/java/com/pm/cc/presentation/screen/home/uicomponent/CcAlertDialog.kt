package com.pm.cc.presentation.screen.home.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.pm.cc.R
import com.pm.cc.presentation.screen.BodyMedium
import com.pm.cc.presentation.screen.HeaderMediumGrey
import com.pm.cc.presentation.screen.HeaderMediumWhite
import com.pm.cc.presentation.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CcAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
) =
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.8f)
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                HeaderMediumGrey(
                    text = title,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                BodyMedium(
                    text = text,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.8f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    HeaderMediumWhite(stringResource(R.string.ok).uppercase())
                }
            }
        }
    }
