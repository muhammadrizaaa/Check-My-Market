package com.riza0004.checkmymarket.ui.component

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme

@Composable
fun DeleteDialog(
    onDismissReq: () -> Unit,
    onConfirmation: () -> Unit,
    name: String
){
    AlertDialog(
        text = {
            Text(
                text = stringResource(R.string.delete_msg, name),
                overflow = TextOverflow.Ellipsis
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(
                    text = stringResource(R.string.delete_btn)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissReq) {
                Text(
                    text = stringResource(R.string.cancel_btn)
                )
            }
        },
        onDismissRequest = {
            onDismissReq()
        }
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview(){
    CheckMyMarketTheme {
        DeleteDialog(
            onConfirmation = {},
            onDismissReq = {},
            name = "Aqua"
        )
    }
}