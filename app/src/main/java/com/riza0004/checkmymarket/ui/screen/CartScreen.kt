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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.dataclass.CartDataClass
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import com.riza0004.checkmymarket.navigation.Screen
import com.riza0004.checkmymarket.ui.component.CartDialog
import com.riza0004.checkmymarket.ui.component.OneBtnDialog
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.CustomerViewModel
import com.riza0004.checkmymarket.viewmodel.DetailTransactionViewModel
import com.riza0004.checkmymarket.viewmodel.ProductViewModel
import com.riza0004.checkmymarket.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCartScreen(
    productViewModel: ProductViewModel? = null,
    navHostController: NavHostController
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: ProductViewModel = productViewModel?:viewModel(factory = factory)
    val cart by viewModel.cart.collectAsState()
    val transactionViewModel: TransactionViewModel = viewModel(factory = factory)
    val detailTransactionViewModel: DetailTransactionViewModel = viewModel(factory = factory)
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableIntStateOf(0) }
    var cashGiven by remember { mutableStateOf("") }
    var cashGivenIsErr by remember { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<CustomerDataClass?>(null) }
    var customerIsErr by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.cart_screen)
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
                },
                actions = {
                    IconButton(
                        onClick = {
                            showDialog = true
                            dialogType = 2
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = stringResource(R.string.clear_btn),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            customerIsErr = selectedCustomer == null
                            cashGivenIsErr = cashGiven.isBlank()
                            if(!cashGivenIsErr && !customerIsErr){
                                if( cashGiven.toLong() >= viewModel.cart.value.sumOf { it.totalPrice }){
                                    showDialog = true
                                    dialogType = 1
                                }
                                else{
                                    cashGivenIsErr = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_check_24),
                            contentDescription = stringResource(R.string.confirm_button),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) {innerPadding->
        CartScreenContent(
            modifier = Modifier.padding(innerPadding),
            productViewModel = viewModel,
            value = cashGiven,
            isErr = cashGivenIsErr,
            onValueChange = {
                cashGiven = it
            },
            customerIsErr = customerIsErr,
            selectedCustomer = selectedCustomer,
            onCLick = {
                selectedCustomer = it
            },
            clickToAddCustomer = {
                navHostController.navigate(Screen.AddCustomer.route)
            }
        )
        if(showDialog){
            when (dialogType) {
                1 -> CartDialog(
                    onDismissReq = { showDialog = false },
                    onConfirmation = {
                        transactionViewModel.insert(
                            idCustomer = selectedCustomer?.id?:1,
                            price = cart.sumOf { it.totalPrice.toLong() },
                            onSuccess = {
                                detailTransactionViewModel.insert(
                                    idTransaction = it,
                                    cart = cart
                                )
                            }
                        )
                        dialogType = 3
                    },
                    title = stringResource(R.string.cart_purchase_dialog_title),
                    msg = stringResource(R.string.cart_purchase_dialog_msg),
                    confirmBtn = stringResource(R.string.cart_purchase_dialog_btn)
                )
                2 -> CartDialog(
                    onDismissReq = { showDialog = false },
                    onConfirmation = {
                        showDialog = false
                        viewModel.clearCart()
                        navHostController.popBackStack()
                        Toast.makeText(context, R.string.cart_cleared, Toast.LENGTH_SHORT).show()
                    },
                    title = stringResource(R.string.cart_clear_dialog_title),
                    msg = stringResource(R.string.cart_clear_dialog_msg),
                    confirmBtn = stringResource(R.string.cart_clear_dialog_btn)
                )
                3-> OneBtnDialog(
                    onDismissReq = {
                        showDialog = false
                        viewModel.clearCart()
                        navHostController.popBackStack()
                    },
                    title = stringResource(R.string.success_cart_purchase_dialog_title),
                    msg = stringResource(R.string.total_change, cashGiven.toLong() - viewModel.cart.collectAsState().value.sumOf { it.totalPrice })
                )
            }
        }
    }
}

@Composable
fun CartScreenContent(
    modifier: Modifier,
    productViewModel: ProductViewModel,
    value: String,
    isErr: Boolean,
    onValueChange: (String) ->Unit,
    selectedCustomer: CustomerDataClass?,
    customerIsErr: Boolean,
    onCLick: (CustomerDataClass) -> Unit,
    clickToAddCustomer: () -> Unit
){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val customerViewModel: CustomerViewModel = viewModel(factory = factory)
    val customers by customerViewModel.data.collectAsState()
    val cart by productViewModel.cart.collectAsState()
    val totalPrice = cart.sumOf { it.totalPrice }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if(cart.isEmpty()){
            Text(text = stringResource(R.string.empty_cart))
        }
        else{
            Text(
                text = stringResource(R.string.customer_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            SelectUserInCart(
                modifier = Modifier.padding(start = 8.dp),
                selectedCustomer = selectedCustomer,
                customers = customers,
                onClick = {
                    onCLick(it)
                },
                clickToAddCustomer = clickToAddCustomer,
                isErr = customerIsErr
            )
            Text(
                text = "${stringResource(R.string.list_product_screen)}: ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            LazyColumn(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               items(cart){data->
                   ListProductInCart(
                       data,
                       data.num
                   )
               }
            }
            Text(
                text = stringResource(R.string.total_price, totalPrice),
                modifier = Modifier.padding(start = 8.dp)
            )
            OutlinedTextField(
                modifier = Modifier.padding(start = 8.dp),
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(
                        text = stringResource(R.string.cash_received)
                    )
                },
                isError = isErr,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                supportingText = {
                    if(value.isNotBlank()){
                        if(totalPrice > value.toLong()){
                            TextFieldErrMessage(
                                isError = true,
                                label = stringResource(R.string.cash_received_notEnough)
                            )
                        }
                    }
                    else{
                        TextFieldErrMessage(
                            isError = isErr,
                            label = stringResource(R.string.error_message_with_label, stringResource(R.string.cash_received))
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ListProductInCart(
    cartDataClass: CartDataClass,
    num: Int
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "$num.",
            )
            Text(
                modifier = Modifier.weight(1f),
                text = cartDataClass.product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "" +
                        stringResource(R.string.price_format, cartDataClass.product.price) +
                        " X ${cartDataClass.qty} = ${stringResource(R.string.price_format, cartDataClass.totalPrice)}",
                maxLines = 1,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun SelectUserInCart(
    modifier: Modifier = Modifier,
    selectedCustomer: CustomerDataClass?,
    onClick: (CustomerDataClass) -> Unit,
    customers: List<CustomerDataClass>,
    clickToAddCustomer: () -> Unit,
    isErr: Boolean
){
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            if(selectedCustomer == null)
            {
                Text(
                    text = "Select Customer"
                )
            }
            else{
                Text(
                    text = "${selectedCustomer.id}. ${selectedCustomer.name} - ${selectedCustomer.phoneNum}",
                    overflow = TextOverflow.Ellipsis
                )
            }
            if(expanded){
                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_up_24),
                    contentDescription = stringResource(R.string.up_button)
                )
            }
            else{
                Icon(
                    painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = stringResource(R.string.down_button)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false},
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.add_customer),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                onClick = {
                    clickToAddCustomer()
                    expanded = false
                },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            customers.forEach {customer->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${customer.id}. ${customer.name} - ${customer.phoneNum}",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = {
                        onClick(customer)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }
        }
    }
    if(isErr){
        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = stringResource(R.string.error_message_with_label, stringResource(R.string.add_customer)),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CartScreenPreview() {
    CheckMyMarketTheme {
        MainCartScreen(
            navHostController = rememberNavController()
        )
    }
}