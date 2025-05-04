package com.riza0004.checkmymarket.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.R
import com.riza0004.checkmymarket.ui.component.DeleteDialog
import com.riza0004.checkmymarket.ui.theme.CheckMyMarketTheme
import com.riza0004.checkmymarket.util.ViewModelFactory
import com.riza0004.checkmymarket.viewmodel.CustomerViewModel

const val KEY_ID_Customer = "idCustomer"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAddCustomerScreen(navHostController: NavHostController, id: Long? = null){
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: CustomerViewModel = viewModel(factory = factory)
    var name by remember { mutableStateOf("") }
    var phoneNum by remember { mutableStateOf("") }
    var onInsert by remember { mutableStateOf("") }
    var nameIsErr by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if(id==null)return@LaunchedEffect
        val data = viewModel.getCustomer(id) ?: return@LaunchedEffect
        name = data.name
        phoneNum = data.phoneNum
        onInsert = data.onInsert
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if(id == null){
                        Text(stringResource(R.string.add_customer))
                    }
                    else{
                        Text(stringResource(R.string.edit_customer))
                    }
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
                            contentDescription = stringResource(R.string.back_button),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            nameIsErr = name.isBlank()
                            if(!nameIsErr){
                                if(id!=null){
                                    viewModel.update(
                                        id = id,
                                        name = name,
                                        phoneNum = phoneNum,
                                        onCreate = onInsert
                                    )
                                    Toast.makeText(context, R.string.customer_edited, Toast.LENGTH_SHORT).show()
                                    navHostController.popBackStack()
                                }
                                else{
                                    viewModel.insert(
                                        name = name,
                                        phoneNum = phoneNum
                                    )
                                    Toast.makeText(context, R.string.customer_added, Toast.LENGTH_SHORT).show()
                                    navHostController.popBackStack()
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_check_24),
                            contentDescription = stringResource(R.string.add_customer),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if(id!=null){
                        ShowDelete {
                            showDialog = true
                        }
                    }
                }
            )
        }
    ) {innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                label = {
                    Text(stringResource(R.string.customer_name_label))
                },
                onValueChange = {name = it},
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                isError = nameIsErr,
                supportingText = {
                    TextFieldErrMessage(
                        nameIsErr,
                        stringResource(R.string.customer_name_label)
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNum,
                label = {
                    Text(stringResource(R.string.customer_phoneNum_label))
                },
                onValueChange = {phoneNum = it},
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                )
            )
        }
        if(showDialog && id!=null){
            DeleteDialog(
                onDismissReq = {showDialog = false},
                onConfirmation = {
                    viewModel.delete(id = id)
                    showDialog = false
                    navHostController.popBackStack()
                    Toast.makeText(context, R.string.customer_deleted, Toast.LENGTH_SHORT).show()
                },
                name = name
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddCustomerPreview() {
    CheckMyMarketTheme {
        MainAddCustomerScreen(navHostController = rememberNavController())
    }
}
