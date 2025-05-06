package com.riza0004.checkmymarket.ui.component


import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme

@Composable
fun CartDialog(
    onDismissReq: () -> Unit,
    onConfirmation: () -> Unit,
    title: String,
    msg: String,
    confirmBtn: String
){
    AlertDialog(
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = msg,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(
                    text = confirmBtn
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
fun CartDialogPreview(){
    CheckMyMarketTheme {
        CartDialog(
            onConfirmation = {},
            onDismissReq = {},
            title = stringResource(R.string.cart_purchase_dialog_title),
            msg = stringResource(R.string.cart_purchase_dialog_msg),
            confirmBtn = stringResource(R.string.cart_purchase_dialog_btn)
        )
    }
}