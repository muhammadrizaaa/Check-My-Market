package com.riza0004.checkmymarket.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAddProductScreen(navHostController: NavHostController){
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var productNameIsErr by remember { mutableStateOf(false) }
    var productPriceIsErr by remember { mutableStateOf(false) }
    var productStockIsErr by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_product_screen)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {navHostController.popBackStack()}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            productNameIsErr = productName.isBlank()
                            productPriceIsErr = productPrice.isBlank()
                            productStockIsErr = productStock.isBlank()
                            if(!productNameIsErr && !productPriceIsErr && !productStockIsErr){
                                navHostController.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_check_24),
                            contentDescription = stringResource(R.string.more)
                        )
                    }
                }
            )
        }
    ) { innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = productName,
                label = {
                    Text(stringResource(R.string.product_name_label))
                },
                onValueChange = {productName = it},
                isError = productNameIsErr,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                supportingText = {
                    TextFieldErrMessage(productNameIsErr, stringResource(R.string.product_name_label))
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = productPrice,
                label = {
                    Text(stringResource(R.string.product_price_label))
                },
                onValueChange = {productPrice = it},
                isError = productPriceIsErr,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    TextFieldErrMessage(productPriceIsErr, stringResource(R.string.product_price_label))
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = productStock,
                label = {
                    Text(stringResource(R.string.product_stock_label))
                },
                onValueChange = {productStock = it},
                isError = productStockIsErr,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                supportingText = {
                    TextFieldErrMessage(productStockIsErr, stringResource(R.string.product_stock_label))
                }
            )
        }
    }
}

@Composable
fun TextFieldErrMessage(isError: Boolean, label: String = ""){
    if(isError){
        if(label.isBlank()){
            Text(stringResource(R.string.error_message_without_label))
        }
        else{
            Text(stringResource(R.string.error_message_with_label, label))
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddProductPreview() {
    CheckMyMarketTheme {
        MainAddProductScreen(navHostController = rememberNavController())
    }
}