package com.riza0004.checkmymarket.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import com.riza0004.checkmymarket.navigation.Screen
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListCustomerScreen(navHostController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.list_customer_screen)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Screen.AddCustomer.route)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = stringResource(R.string.add_customer)
                )
            }
        }
    ) { innerPadding->
        CustomerScreenContent(
            modifier = Modifier.padding(innerPadding),
            navHostController = navHostController
        )
    }
}

@Composable
fun CustomerScreenContent(
    modifier: Modifier,
    navHostController: NavHostController
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: CustomerViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.list_customer_screen),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        if(data.isEmpty()){
            Text(
                text = stringResource(R.string.empty_data_customer),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        else{
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data){data->
                    ListCustomer(data){ id->
                        navHostController.navigate(Screen.DetailCustomer.withId(id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListCustomer(customer: CustomerDataClass, onClick:(id: Long) -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick(customer.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Column {
                Text(
                    customer.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    customer.phoneNum,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
                Text(
                    stringResource(R.string.onInsert_product, customer.onInsert),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Thin
                )
                Text(
                    stringResource(R.string.onUpdate_product, customer.onUpdate),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Thin
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ListCustomerScreenPreview() {
    CheckMyMarketTheme {
        MainListCustomerScreen(navHostController = rememberNavController())
    }
}