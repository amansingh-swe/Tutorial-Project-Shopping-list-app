@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tutorialprojectshoppinglistapp.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingListItem(
    val id: Int, var itemName: String, var itemQuantity: Int, var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var shoppingListItems by remember { mutableStateOf(listOf<ShoppingListItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var addItemName by remember { mutableStateOf("") }
    var addItemQuantity by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { showDialog = true }) {
                Text(text = "Add Item")
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(shoppingListItems) { item ->
                    if (item.isEditing) {
                        ShoppingItemEdit(
                            listItem = item,
                            onEditComplete = { editedName, editedQuantity ->
                                shoppingListItems =
                                    shoppingListItems.map { it.copy(isEditing = false) }
                                val editedItem = shoppingListItems.find { it.id == item.id }
                                editedItem?.let {
                                    it.itemName = editedName
                                    it.itemQuantity = editedQuantity
                                }
                            })
                    } else {
                        ShowShoppingListItem(item = item, onEditClick = {
                            shoppingListItems = shoppingListItems.map {
                                it.copy(isEditing = it.id == item.id)
                            }
                        }, onDeleteClick = {
                            shoppingListItems = shoppingListItems - item
                        })
                    }

                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (addItemName.isNotBlank()) {

                        val newItem = ShoppingListItem(
                            id = shoppingListItems.size + 1,
                            itemName = addItemName,
                            itemQuantity = addItemQuantity.toIntOrNull() ?: 1
                        )

                        shoppingListItems = shoppingListItems + newItem
                        showDialog = false
                    }
                }) {
                    Text(text = "Add")
                }

                Button(onClick = {
                    showDialog = false
                }) {
                    Text(text = "Cancel")
                }

            }
        }, title = {
            Text(
                text = "Add an Item to the List"
            )
        }, text = {
            Column {
                OutlinedTextField(modifier = Modifier.padding(8.dp),
                    value = addItemName,
                    onValueChange = { addItemName = it })
                OutlinedTextField(modifier = Modifier.padding(8.dp),
                    value = addItemQuantity,
                    onValueChange = { addItemQuantity = it })
            }
        })
    }
}

@Composable
fun ShowShoppingListItem(
    item: ShoppingListItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(border = BorderStroke(2.dp, Color.Green), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = item.itemName, modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
            )
            Text(
                text = item.itemQuantity.toString(), modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
            )
        }
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun ShoppingItemEdit(listItem: ShoppingListItem, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(listItem.itemName) }
    var editedQuantity by remember { mutableStateOf(listItem.itemQuantity.toString()) }
    var isEditing by remember { mutableStateOf(listItem.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(modifier = Modifier.wrapContentSize() , value = editedName, onValueChange = { editedName = it })
            OutlinedTextField(value = editedQuantity, onValueChange = { editedQuantity = it })
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }
}