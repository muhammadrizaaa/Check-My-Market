package com.riza0004.checkmymarket.ui.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.dataclass.CartDataClass
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import com.riza0004.checkmymarket.navigation.Screen
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.ProductViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel? = null
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: ProductViewModel = productViewModel?: viewModel(factory = factory)
    var expanded by remember { mutableStateOf(false) }
    val cart by viewModel.cart.collectAsState()
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
        },
        floatingActionButton = {
            if(cart.isNotEmpty()){
                Box {
                    FloatingActionButton(
                        onClick = {
                            navHostController.navigate(Screen.Cart.route)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_add_shopping_cart_24),
                            contentDescription = stringResource(R.string.fab_cart)
                        )
                    }

                    // Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .size(18.dp)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cart.size.toString(), // Replace with your dynamic cart count
                            color = MaterialTheme.colorScheme.primaryContainer,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding->
        HomeScreenContent(
            modifier = Modifier.padding(innerPadding),
            navHostController = navHostController,
            viewModel = viewModel,
            cart = cart
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier,
    navHostController: NavHostController,
    viewModel: ProductViewModel,
    cart: List<CartDataClass>
){
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
                    ProductListHome(
                        product = data,
                        viewModel = viewModel,
                        cart = cart
                        ){id->
                        navHostController.navigate(Screen.DetailProduct.withId(id))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListHome(
    product: ProductDataClass,
    viewModel: ProductViewModel,
    cart: List<CartDataClass>,
    onClick: (id: Long) -> Unit
){
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
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
            if(cart.any{it.product == product}){
                val singleCart by remember(cart) {
                    derivedStateOf {
                        cart.find { it.product.id == product.id }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = {viewModel.minQtyCart(product)},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.baseline_remove_24),
                            contentDescription = stringResource(R.string.minus_button)
                        )
                    }
                    Text("${singleCart?.qty ?: 0}")
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = {viewModel.plusQtyCart(product)},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.baseline_add_24),
                            contentDescription = stringResource(R.string.plus_button)
                        )
                    }
                }
            }
            else{
                IconButton(
                    onClick = {
                        viewModel.addToCart(product = product, 1)
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_add_24),
                        contentDescription = stringResource(R.string.add_to_cart),
                        Modifier.size(32.dp)
                    )
                }
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