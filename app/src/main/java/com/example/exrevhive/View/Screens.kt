package com.example.exrevhive.View

import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.exrevhive.Model.Category
import com.example.exrevhive.Model.Transaction
import com.example.exrevhive.Model.TransactionType
import com.example.exrevhive.Model.formatAmount
import com.example.exrevhive.R
import com.example.exrevhive.ViewModel.AuthViewModel
import com.example.exrevhive.ViewModel.TransactionViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Récupère le message d'erreur du ViewModel
    val errorMessage = viewModel.errorMessage

    // Contexte de l'application
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 72.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Titre et logo
            Text(
                text = "ExRevHive",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3e7287)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp),
                colorFilter = ColorFilter.tint(Color(0xFF3e7287))
            )
        }
        Spacer(Modifier.height(16.dp))

        // Message de bienvenue
        Text(
            text = "Connectez-vous pour commencer.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(Modifier.height(80.dp))

        // Champ nom d'utilisateur
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom d'utilisateur") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Champ mot de passe
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.errorMessage = "" // Réinitialise le message d'erreur
            },
            label = { Text("Mot de passe") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Affiche le message d'erreur si nécessaire
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(56.dp))

        // Bouton de connexion
        Button(
            onClick = {
                viewModel.authenticate(username, password, context) // Authentification
                if (viewModel.isLoggedIn) {
                    navController.navigate("home_screen") // Redirige vers l'écran d'accueil
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(10.dp, clip = true),
            colors = ButtonDefaults.buttonColors(Color(0xFF3e7287)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Connexion",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel) {
    val username by viewModel.username.collectAsState()

    // Contexte de l'application
    val context = LocalContext.current

    // Charger les données de l'utilisateur à l'initialisation de l'écran
    LaunchedEffect(Unit) {
        viewModel.init(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 72.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Titre de bienvenue
        Text(
            text = "ExRevHive",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3e7287),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp, top = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Description de l'application
        Text(
            text = "Gérez vos revenus et dépenses de manière simple et efficace avec ExRevHive.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF4a4a4a),
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Nom l’utilisateur
        Text(
            text = "Bienvenue $username,\nvotre espaces finaces vous attend!",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF4a4a4a),
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(90.dp))

        // Bouton pour naviguer vers la liste des transactions
        Button(
            onClick = {
                navController.navigate("TransactionList_screen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp)
                .shadow(10.dp, clip = true),
            colors = ButtonDefaults.buttonColors(Color(0xFF3e7287)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Mes transactions!",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(48.dp))

        // Logo de l'applicatio
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(50.dp),
            colorFilter = ColorFilter.tint(Color(0xFF3e7287))
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Bouton pour se déconnecter
        Button(
            onClick = {
                viewModel.logout(context, "loginKey") // Déconnexion
                navController.navigate("login_screen") // Redirection vers l'écran de connexion
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp)
                .shadow(10.dp, clip = true),
            colors = ButtonDefaults.buttonColors(Color(0xFF3e7287)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Déconnexion",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun TransactionListScreen(navController: NavController, viewModel: TransactionViewModel) {
    // Contexte de l'application
    val context = LocalContext.current

    // Observer les transactions du ViewModel de manière réactive
    val transactions = viewModel.transactions.collectAsState(initial = emptyList()).value

    // Dialog de confirmation
    var showDialog by remember { mutableStateOf(false) }

    // Transaction à supprimer
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    // Observer les transactions du ViewModel de manière réactive
    var searchQuery by remember { mutableStateOf("") }

    // States pour les menus déroulants de filtre et tri
    var expandedFilter by remember { mutableStateOf(false) }
    var expandedSort by remember { mutableStateOf(false) }

    // Charger les transactions au démarrage de l'application
    LaunchedEffect(Unit) {
        viewModel.getTransactions(context, "transactionKey") // Charger les transactions
    }

    // Obtenir la liste des transactions filtrées et triées
    val filteredTransactions = viewModel.getFilteredAndSortedTransactions(searchQuery)

    Scaffold(
        topBar = {
            // Barre d'outils avec titre et logo
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Liste des transactions",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .size(40.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                backgroundColor = Color(0xFF3e7287),
                contentColor = Color.White,
                modifier = Modifier.height(80.dp)
            )
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Bouton flottant pour retourner à la page d'acceuil
                FloatingActionButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 32.dp),
                    containerColor = Color(0xFF3e7287),
                    contentColor = Color.White
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                }

                // Bouton flottant pour ajouter une nouvelle transaction
                FloatingActionButton(
                    onClick = { navController.navigate("newTransaction_screen") },
                    containerColor = Color(0xFF3e7287),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter une transaction")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp)
        ) {
            // Barre de recherche et boutons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Icone de recherche
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Rechercher",
                    tint = Color(0xFF3e7287),
                    modifier = Modifier.padding(start = 8.dp, end = 4.dp)
                )

                // Champ de recherche
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .border(1.dp, Color(0xFF3e7287), shape = RoundedCornerShape(8.dp))
                        .width(180.dp)
                        .padding(14.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp)
                )

                // Bouton pour le filtre
                Box {
                    OutlinedButton(
                        onClick = { expandedFilter = true },
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                        border = BorderStroke(1.dp, Color(0xFF3e7287))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(43.dp)
                        ) {
                            Text(
                                text = "Filtrer",
                                color = Color(0xFF3e7287),
                                fontSize = 12.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Filtrer",
                                tint = Color(0xFF3e7287)
                            )
                        }
                    }

                    // Menu déroulant pour le filtre
                    DropdownMenu(
                        expanded = expandedFilter,
                        onDismissRequest = { expandedFilter = false },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.setTransactionFilter("All")
                            expandedFilter = false
                        }) {
                            Text("Toutes les transactions")
                        }
                        DropdownMenuItem(onClick = {
                            viewModel.setTransactionFilter("DEPENSE")
                            expandedFilter = false
                        }) {
                            Text("Dépenses")
                        }
                        DropdownMenuItem(onClick = {
                            viewModel.setTransactionFilter("REVENU")
                            expandedFilter = false
                        }) {
                            Text("Revenus")
                        }
                    }
                }

                // Bouton pour le tri
                Box {
                    OutlinedButton(
                        onClick = { expandedSort = true },
                        modifier = Modifier.padding(end = 8.dp),
                        border = BorderStroke(1.dp, Color(0xFF3e7287))
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(37.dp)
                        ) {
                            Text(
                                text = "Trier",
                                color = Color(0xFF3e7287),
                                fontSize = 12.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Trier",
                                tint = Color(0xFF3e7287)
                            )
                        }
                    }

                    // Menu déroulant pour le tri
                    DropdownMenu(
                        expanded = expandedSort,
                        onDismissRequest = { expandedSort = false },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        DropdownMenuItem(onClick = {
                            viewModel.setDateSortOrder(true)
                            expandedSort = false
                        }) {
                            Text("Trier par Date ↑")
                        }
                        DropdownMenuItem(onClick = {
                            viewModel.setDateSortOrder(false)
                            expandedSort = false
                        }) {
                            Text("Trier par Date ↓")
                        }
                    }
                }
            }
            // Liste des transactions
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("transactionDetails_screen/${transaction.id}") },
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Détails de la transaction
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = transaction.description
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${transaction.category} / ${transaction.date}"
                                )
                            }

                            // Texte en fonction du type de transaction
                            val transactionText = if (transaction.type == TransactionType.DEPENSE) {
                                "- ${formatAmount(transaction.amount)} $" // Pour une dépense
                            } else {
                                "+ ${formatAmount(transaction.amount)} $" // Pour un revenu
                            }

                            // Affichage du montant avec la couleur dynamique
                            Text(
                                text = transactionText,
                                fontSize = 16.sp,
                                color = if (transaction.type == TransactionType.DEPENSE) Color(0xFFa33939) else Color(0xFF39953E),
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            // Bouton modifier
                            IconButton(
                                onClick = {
                                    // Naviguer vers l'écran de modification avec l'ID de la transaction
                                    navController.navigate("modifyTransaction_screen/${transaction.id}")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Modifier la transaction",
                                    tint = Color(0xFF3e7287)
                                )
                            }

                            // Bouton pour supprimer la transaction
                            IconButton(
                                onClick = {
                                    transactionToDelete = transaction
                                    showDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Supprimer la transaction",
                                    tint = Color(0xFFa33939)
                                )
                            }
                        }
                    }
                }
            }
        }
        // Dialog de confirmation pour suppression
        if (showDialog && transactionToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmer la suppression") },
                text = { Text("Voulez-vous vraiment supprimer cette transaction ?") },
                confirmButton = {
                    TextButton(onClick = {
                        // Supprimer la transaction
                        transactionToDelete?.let {
                            viewModel.deleteTransaction(
                                it.id,
                                context,
                                "transactionKey"
                            )
                        }
                        showDialog = false // Fermer le dialog
                        navController.navigate("transactionList_screen")
                    }) {
                        Text("Supprimer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }
    }
}

@Composable
fun NewTransactionScreen(navController: NavController, viewModel: TransactionViewModel) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.AUTRE) } // Catégorie par défaut
    var isExpense by remember { mutableStateOf(false) } // Utilisé pour Switch (true pour dépense, false pour revenu)
    var date by remember { mutableStateOf("") }

    // Dialog de date
    var showDialog by remember { mutableStateOf(false) }

    // Liste des catégories (en utilisant les valeurs de l'énumération Category)
    val categories = Category.values()

    // Contexte de l'application
    val context = LocalContext.current

    Scaffold(
        topBar = {
            // Barre d'outils avec titre et logo
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Nouvelle transaction",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .size(40.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                backgroundColor = Color(0xFF3e7287),
                contentColor = Color.White,
                modifier = Modifier.height(80.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Champ pour le titre
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Montant de la transaction") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Champ pour la description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Champ pour la date
                        OutlinedTextField(
                            value = date,
                            onValueChange = { },
                            label = { Text("Date (DD-MM-YYYY)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDialog = true },
                            enabled = false
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Radio Group pour la catégorie
                        Text(
                            text = "Catégorie",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // Utilisation de LazyVerticalGrid pour organiser les catégories en 2 colonnes
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2), // Définir 2 colonnes
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            items(categories) { category ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedCategory == category,
                                        onClick = { selectedCategory = category }
                                    )
                                    Text(
                                        text = category.displayName,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Switch pour type de transaction (Dépense ou Revenu)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Type de transaction : ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            // Affichage dynamique des labels selon l'état du Switch
                            Text(
                                text = if (isExpense) "Dépense" else "Revenu",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpense) Color(0xFFa33939) else Color(0xFF39953E),
                                modifier = Modifier.padding(end = 70.dp)
                            )
                            Switch(
                                checked = isExpense,
                                onCheckedChange = { isExpense = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bouton pour ajouter la transaction
                Button(
                    onClick = {
                        errorMessage = ""
                        successMessage = ""
                        if (amount.isNotBlank() && description.isNotBlank() && date.isNotBlank()) {
                            val amountDouble = amount.toDoubleOrNull()
                            if (amountDouble != null) {
                                val transactionType =
                                    if (isExpense) TransactionType.DEPENSE else TransactionType.REVENU
                                successMessage = "Transaction ajoutée avec succès"
                                viewModel.addTransaction(
                                    amountDouble,
                                    description,
                                    selectedCategory,
                                    transactionType,
                                    date,
                                    context,
                                    "transactionIdKey",
                                    "transactionKey"
                                )
                                navController.popBackStack()
                            } else {
                                errorMessage = "Le montant doit être un nombre valide"
                            }
                        } else {
                            errorMessage = "Tous les champs doivent être remplis"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(10.dp, clip = true),
                    colors = ButtonDefaults.buttonColors(Color(0xFF3e7287)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ajouter",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                // Message de succès
                if (successMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = successMessage,
                        color = Color(0xFF39953E),
                        fontSize = 16.sp
                    )
                }

                // Message d'erreur
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0xFFa33939),
                        fontSize = 16.sp
                    )
                }

                // Dialog de DatePicker
                if (showDialog) {
                    // On récupère la date actuelle comme date par défaut
                    val calendar = Calendar.getInstance()
                    android.app.DatePickerDialog(
                        context, R.style.CustomDatePickerDialog,
                        { _, year, month, dayOfMonth ->
                            // Format de la date choisi (attention, month commence à 0)
                            date = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                            showDialog = false
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
    )
}

@SuppressLint("DefaultLocale", "ResourceType")
@Composable
fun ModifyTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel,
    transactionId: Int?
) {
    // Observer les transactions disponibles depuis le ViewModel
    val transactions = viewModel.transactions.collectAsState(initial = emptyList())

    // Trouver la transaction à modifier dans la liste des transactions
    val transaction = transactions.value.find { it.id == transactionId }

    // Si la transaction est nulle, on retourne à l'écran précédent
    if (transaction == null) {
        return
    }

    // Contexte de l'application
    val context = LocalContext.current

    var newAmount by remember { mutableStateOf(transaction.amount.toString()) }
    var newDescription by remember { mutableStateOf(transaction.description) }
    var selectedCategory by remember { mutableStateOf(transaction.category) }
    var isExpense by remember { mutableStateOf(transaction.type == TransactionType.DEPENSE) }
    var newDate by remember { mutableStateOf(transaction.date) }

    var successMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Dialog de date
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Barre d'outils avec titre et logo
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Modifier la transaction",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .size(40.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                backgroundColor = Color(0xFF3e7287),
                contentColor = Color.White,
                modifier = Modifier.height(80.dp)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Champ pour éditer le montant
                        OutlinedTextField(
                            value = newAmount,
                            onValueChange = { newAmount = it },
                            label = { Text("Montant de la transaction") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Champ pour éditer la description
                        OutlinedTextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Champ pour éditer la date
                        OutlinedTextField(
                            value = newDate,
                            onValueChange = {  },
                            label = { Text("Date (DD-MM-YYYY)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDialog = true },
                            enabled = false
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Radio Group pour la catégorie
                        Text(
                            text = "Catégorie",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // Utilisation de LazyVerticalGrid pour organiser les catégories en 2 colonnes
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2), // Définir 2 colonnes
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            items(Category.values()) { category ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedCategory == category,
                                        onClick = { selectedCategory = category }
                                    )
                                    Text(
                                        text = category.displayName,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Switch pour type de transaction (Dépense ou Revenu)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Type de transaction : ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            // Affichage dynamique des labels selon l'état du Switch
                            Text(
                                text = if (isExpense) "Dépense" else "Revenu",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpense) Color(0xFFa33939) else Color(0xFF39953E),
                                modifier = Modifier.padding(end = 70.dp)
                            )
                            Switch(
                                checked = isExpense,
                                onCheckedChange = { isExpense = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bouton pour sauvegarder les modifications de la transaction
                Button(
                    onClick = {
                        errorMessage = ""
                        successMessage = ""
                        if (newAmount.isNotBlank() && newDescription.isNotBlank()) {
                            val amountDouble = newAmount.toDoubleOrNull()
                            if (amountDouble != null) {
                                val transactionType =
                                    if (isExpense) TransactionType.DEPENSE else TransactionType.REVENU
                                successMessage = "Transaction modifiée avec succès"
                                viewModel.modifyTransaction(
                                    transaction.id,
                                    amountDouble,
                                    newDescription,
                                    selectedCategory,
                                    transactionType,
                                    newDate,
                                    context,
                                    "transactionKey"
                                )
                                navController.popBackStack()
                            } else {
                                errorMessage = "Le montant doit être un nombre valide"
                            }
                        } else {
                            errorMessage = "Tous les champs doivent être remplis"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(10.dp, clip = true),
                    colors = ButtonDefaults.buttonColors(Color(0xFF3e7287)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Sauvegarder",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }

                // Message de succès
                if (successMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = successMessage,
                        color = Color(0xFF39953E),
                        fontSize = 16.sp
                    )
                }

                // Message d'erreur'
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = Color(0xFFa33939),
                        fontSize = 16.sp
                    )
                }

                // Dialog de DatePicker
                if (showDialog) {
                    // On récupère la date actuelle comme date par défaut
                    val calendar = Calendar.getInstance()
                    android.app.DatePickerDialog(
                        context, R.style.CustomDatePickerDialog,
                        { _, year, month, dayOfMonth ->
                            // Format de la date choisi (attention, month commence à 0)
                            newDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                            showDialog = false
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
    )
}

@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    viewModel: TransactionViewModel,
    transactionId: Int?
) {
    // Collecter les transactions de manière réactive
    val transactions = viewModel.transactions.collectAsState(initial = emptyList())

    // Recherche de la transaction
    val transaction = transactions.value.find { it.id == transactionId }

    // Contexte de l'application
    val context = LocalContext.current

    // Si la transaction n'est pas trouvée, retour à l'écran précédent
    if (transaction == null) {
        return
    }

    // Dialog de confirmation
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Barre d'outils avec titre et logo
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "Détails de la transaction",
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .size(40.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                backgroundColor = Color(0xFF3e7287),
                contentColor = Color.White,
                modifier = Modifier.height(80.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Description
                Text(
                    text = transaction.description,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3e7287),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Montant
                val transactionText = if (transaction.type == TransactionType.DEPENSE) {
                    "- ${formatAmount(transaction.amount)} $" // For an expense
                } else {
                    "+ ${formatAmount(transaction.amount)} $" // For income
                }
                Text(
                    text = transactionText,
                    fontSize = 16.sp,
                    color = if (transaction.type == TransactionType.DEPENSE) Color(0xFFa33939) else Color(0xFF39953E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Date
                Text(
                    text = transaction.date,
                    fontSize = 16.sp,
                    color = Color(0xFF4a4a4a),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Catégorie
                Text(
                    text = "Catégorie : ${transaction.category}",
                    fontSize = 16.sp,
                    color = Color(0xFF4a4a4a),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Type de transaction
                Text(
                    text = "Type de transaction : ${if (transaction.type == TransactionType.DEPENSE) "Dépense" else "Revenu"}",
                    fontSize = 16.sp,
                    color = Color(0xFF4a4a4a),
                    modifier = Modifier.padding(bottom = 36.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Bouton pour modifier la transaction
                    Button(
                        onClick = {
                            navController.navigate("modifyTransaction_screen/${transaction.id}")
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF3e7287)),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Modifier",
                            color = Color.White
                        )
                    }

                    // Bouton pour supprimer la transaction
                    Button(
                        onClick = {
                            showDialog = true // Afficher le dialog de confirmation
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFFa33939)),
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Supprimer",
                            color = Color.White
                        )
                    }
                }

                // Dialog de confirmation pour supprission
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirmer la suppression") },
                        text = { Text("Voulez-vous vraiment supprimer cette transaction ?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.deleteTransaction(transaction.id, context,"transactionKey")
                                navController.navigate("transactionList_screen")
                                showDialog = false
                            }) {
                                Text("Supprimer")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Annuler")
                            }
                        }
                    )
                }
            }
        }
    )
}