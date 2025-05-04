package com.riza0004.checkmymarket.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import com.riza0004.checkmymarket.navigation.Screen
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(navHostController: NavHostController){
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {expanded = !expanded}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_more_vert_24),
                            contentDescription = stringResource(R.string.more)
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(R.string.products_list))
                                },
                                onClick = {
                                    expanded = false
                                    navHostController.navigate(Screen.ListProduct.route)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(R.string.list_customer_screen))
                                },
                                onClick = {
                                    expanded = false
                                    navHostController.navigate(Screen.ListCustomer.route)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(R.string.about_this_app))
                                },
                                onClick = {
                                    expanded = false
                                    Toast.makeText(
                                        context,
                                        "About This App",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding->
        HomeScreenContent(
            modifier = Modifier.padding(innerPadding),
            navHostController = navHostController
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    navHostController: NavHostController
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: ProductViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()
    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.products_list),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        if(data.isEmpty()){
            Text(
                text = stringResource(R.string.empty_data_product),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        else{
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data){data->
                    ProductListHome(data){id->
                        navHostController.navigate(Screen.DetailProduct.withId(id))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListHome(product: ProductDataClass, onClick: (id: Long) -> Unit){
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick(product.id) },
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
                    product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    product.price.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    stringResource(R.string.stock_product, product.stock),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(
                onClick = {
                    Toast.makeText(context, "add to cart", Toast.LENGTH_SHORT).show()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = stringResource(R.string.add_to_cart),
                    Modifier.size(48.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GreetingPreview() {
    CheckMyMarketTheme {
        MainHomeScreen(navHostController = rememberNavController())
    }
}