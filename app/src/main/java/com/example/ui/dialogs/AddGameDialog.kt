package com.example.ui.dialogs

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.GameItem
import com.example.ui.theme.*

data class PredefinedGame(
    val name: String,
    val packageName: String,
    val category: String,
    val iconEmoji: String
)

@Composable
fun AddGameDialog(
    existingGames: List<GameItem>,
    onGameSelected: (name: String, pkg: String, emoji: String, category: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var installedApps by remember { mutableStateOf<List<PredefinedGame>>(emptyList()) }

    // Fetch installed apps
    val refreshApps = remember {
        {
            val pm = context.packageManager
            // Get all installed apps
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            
            val filtered = apps.filter { appInfo ->
                // Show almost everything, just ignore the core system/test stuff
                val isNotCoreSystem = !appInfo.packageName.startsWith("com.android.") && 
                                      !appInfo.packageName.startsWith("android.") &&
                                      !appInfo.packageName.startsWith("com.google.android.")
                
                isNotCoreSystem
            }.map {
                PredefinedGame(
                    name = it.loadLabel(pm).toString(),
                    packageName = it.packageName,
                    category = "App Instalado",
                    iconEmoji = "🎮"
                )
            }
            installedApps = filtered
        }
    }

    LaunchedEffect(Unit) {
        refreshApps()
    }

    val filteredGames = remember(searchQuery, installedApps) {
        if (searchQuery.isEmpty()) installedApps
        else installedApps.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp)),
            color = FoxDarkBackground,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonPurple.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Adicionar Jogo ao Shadow",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Refresh Button
                Button(
                    onClick = { refreshApps() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Atualizar Lista de Apps")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Pesquisar jogo (ex: Free Fire, Blood Strike)...", color = Color(0xFF9CA3AF), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = PurpleAccent)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = FoxBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Jogos Populares Compatíveis:",
                    color = Color(0xFFC084FC),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredGames) { item ->
                        val alreadyAdded = existingGames.any { it.packageName == item.packageName || it.name == item.name }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = FoxSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, FoxBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !alreadyAdded) {
                                    onGameSelected(item.name, item.packageName, item.iconEmoji, item.category)
                                    Toast.makeText(context, "${item.name} adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(FoxSurfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = item.iconEmoji, fontSize = 24.sp)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = item.name,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = item.category,
                                            color = Color(0xFF9CA3AF),
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (alreadyAdded) {
                                    Text(
                                        text = "Já adicionado",
                                        color = EmeraldGlow,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    FilledTonalButton(
                                        onClick = {
                                            onGameSelected(item.name, item.packageName, item.iconEmoji, item.category)
                                            Toast.makeText(context, "${item.name} adicionado!", Toast.LENGTH_SHORT).show()
                                            onDismiss()
                                        },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = NeonPurple.copy(alpha = 0.2f),
                                            contentColor = PurpleAccent
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("+ Adicionar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
