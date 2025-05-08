package com.riza0004.checkmymarket.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.dataclass.DetailTransactionDataClass
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.DetailTransactionViewModel
import com.riza0004.checkmymarket.viewmodel.TransactionViewModel


const val KEY_ID_TRANSACTION = "idTransaction"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDetailTransactionScreen(navHostController: NavHostController, id:Long){
        val context = LocalContext.current
        val factory = ViewModelFactory(context)
        val transactionViewModel: TransactionViewModel = viewModel(factory = factory)
        val detailTransactionViewModel: DetailTransactionViewModel = viewModel(factory = factory)
        val transaction = transactionViewModel.getTransaction(id).collectAsState(null).value
        val detailTransaction by detailTransactionViewModel.getDetailTransactionById(id).collectAsState()

    if(id == 0L){
        navHostController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.detail_transaction_screen)
                    )
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
    ) { innerPadding->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.id_transaction, transaction?.id?:1))
                    Text(
                        stringResource(
                        R.string.customer_name,
                        transactionViewModel.getCustomer(transaction?.idCustomer?:1)
                            .collectAsState(null).value?.name?:""
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        transaction?.timeStamp?:"",
                        textAlign = TextAlign.End
                    )
                    Text(
                        stringResource(R.string.price_format, transaction?.price?:0),
                        textAlign = TextAlign.End
                    )
                    Text(
                        stringResource(R.string.products_num, detailTransaction.size)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(detailTransaction){
                    ListDetailTransaction(
                        detailTransaction = it,
                        detailTransactionViewModel = detailTransactionViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ListDetailTransaction(
    detailTransaction: DetailTransactionDataClass,
    detailTransactionViewModel: DetailTransactionViewModel
){
    val product = detailTransactionViewModel.getProductById(detailTransaction.productId).collectAsState(null).value
    val totalPrice = detailTransaction.qty*(product?.price?:0)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(product == null){
                Text(
                    "Deleted product"
                )
            }
            else{
                Text(
                    modifier = Modifier.weight(1f),
                    text = if(product.isDeleted)"${product.name} (Deleted)" else product.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.price_format, product.price)
                )

                Text("X")
                Text(
                    text = "${detailTransaction.qty}:"
                )
                Text(
                    text = stringResource(R.string.price_format, totalPrice)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ListDetailTransactionScreenPreview() {
    CheckMyMarketTheme {
        MainDetailTransactionScreen(navHostController = rememberNavController(), 1)
    }
}