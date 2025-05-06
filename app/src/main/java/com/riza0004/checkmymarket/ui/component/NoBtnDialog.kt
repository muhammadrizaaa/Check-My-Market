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
fun OneBtnDialog(
    onDismissReq: () -> Unit,
    title: String,
    msg: String
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
            TextButton(onClick = onDismissReq) {
                Text(
                    text = stringResource(R.string.close_btn)
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
fun NoBtnDialogPreview(){
    CheckMyMarketTheme {
        OneBtnDialog(
            onDismissReq = {},
            title = stringResource(R.string.success_cart_purchase_dialog_title),
            msg = stringResource(R.string.total_change, 10000),
        )
    }
}