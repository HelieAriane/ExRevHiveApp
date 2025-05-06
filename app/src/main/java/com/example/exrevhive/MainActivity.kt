package com.example.exrevhive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exrevhive.View.HomeScreen
import com.example.exrevhive.View.LoginScreen
import com.example.exrevhive.View.ModifyTransactionScreen
import com.example.exrevhive.View.NewTransactionScreen
import com.example.exrevhive.View.TransactionDetailsScreen
import com.example.exrevhive.View.TransactionListScreen
import com.example.exrevhive.ViewModel.AuthViewModel
import com.example.exrevhive.ViewModel.TransactionViewModel
import com.example.exrevhive.ui.theme.ExRevHiveTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels() // ViewModel pour l'authentification
    private val transactionViewModel: TransactionViewModel by viewModels() // ViewModel pour les tâc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationApp(authViewModel = authViewModel, transactionViewModel = transactionViewModel)
        }
    }
}

@Composable
fun NavigationApp(authViewModel: AuthViewModel, transactionViewModel: TransactionViewModel) {
    val navController = rememberNavController() // Contrôleur de navigation

    // Contexte de l'application
    val context = LocalContext.current

    // Déterminer la destination de départ
    val startDestination = if (authViewModel.getLoginState(context, "loginKey")) {
        "home_screen"
    } else {
        "login_screen"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login_screen") { LoginScreen(navController, viewModel = authViewModel) }
        composable("home_screen") { HomeScreen(navController, viewModel = authViewModel) }
        composable("transactionList_screen") { TransactionListScreen(navController, viewModel = transactionViewModel) }
        composable("newTransaction_screen") { NewTransactionScreen(navController, viewModel = transactionViewModel) }
        composable(
            route = "modifyTransaction_screen/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType})
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId")
            ModifyTransactionScreen(navController, viewModel = transactionViewModel, transactionId = transactionId)
        }
        composable(
            route = "transactionDetails_screen/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType})
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId")
            TransactionDetailsScreen(navController, viewModel = transactionViewModel, transactionId = transactionId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExRevHiveTheme {
        NavigationApp(authViewModel = AuthViewModel(), transactionViewModel = TransactionViewModel())
    }
}