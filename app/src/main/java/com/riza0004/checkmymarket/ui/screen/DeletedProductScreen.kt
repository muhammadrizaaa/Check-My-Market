package com.riza0004.checkmymarket.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import com.riza0004.checkmymarket.ui.component.CartDialog
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDeletedProductScreen(navHostController: NavHostController){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: ProductViewModel = viewModel(factory = factory)
    val data by viewModel.deletedData.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.deleted_product_screen))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
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
                }
            )
        }
    ) {innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding).padding(8.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(data.isEmpty()){
                Text(stringResource(R.string.deleted_product_empty))
            }
            else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(data){data->
                        ListDeletedProduct(data) {
                            showDialog = true
                        }
                        HorizontalDivider()

                        if(showDialog){
                            CartDialog(
                                title = stringResource(R.string.restore_title_dialog),
                                msg = stringResource(R.string.restore_msg_dialog),
                                confirmBtn = stringResource(R.string.restore_button),
                                onConfirmation = {
                                    viewModel.update(
                                        id = data.id,
                                        name = data.name,
                                        price = data.price,
                                        stock = data.stock,
                                        desc = data.desc,
                                        onInsert = data.onInsert,
                                        isDeleted = false
                                    )
                                    showDialog = false
                                    Toast.makeText(context, R.string.product_restored, Toast.LENGTH_SHORT).show()
                                },
                                onDismissReq = {
                                    showDialog = false
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ListDeletedProduct(
    product: ProductDataClass,
    onClick:(Long)->Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    product.desc,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
                Text(
                    stringResource(R.string.stock_product, product.stock),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    stringResource(R.string.price_format, product.price),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    stringResource(R.string.onInsert_product, product.onInsert),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Thin
                )
                Text(
                    stringResource(R.string.onUpdate_product, product.onUpdate),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Thin
                )
            }
            IconButton(
                onClick = { onClick(product.id) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_restore_24),
                    contentDescription = stringResource(R.string.restore_button)
                )
            }
        }
    }
}