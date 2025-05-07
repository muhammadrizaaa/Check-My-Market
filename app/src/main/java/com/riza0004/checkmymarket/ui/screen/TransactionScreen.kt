package com.riza0004.checkmymarket.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import com.riza0004.checkmymarket.dataclass.TransactionDataClass
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListTransactionScreen(navHostController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.transaction_screen)
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
        TransactionScreenContent(
            modifier = Modifier.padding(innerPadding),
            navHostController = navHostController
        )
    }
}

@Composable
fun TransactionScreenContent(
    modifier: Modifier,
    navHostController: NavHostController
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: TransactionViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if(data.isEmpty()){
            Text(
                text = stringResource(R.string.empty_data_transaction)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data){
                ListTransaction(
                    customerName = viewModel.getCustomer(it.idCustomer).collectAsState(null).value?.name?: "",
                    transaction = it,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun ListTransaction(
    onClick: () -> Unit,
    customerName: String,
    transaction: TransactionDataClass
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${transaction.id}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = customerName,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = stringResource(R.string.total_price, transaction.price),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = transaction.timeStamp,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ListTransactionScreenPreview() {
    CheckMyMarketTheme {
        MainListTransactionScreen(navHostController = rememberNavController())
    }
}