package com.example.retrofit2.screens

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.retrofit2.models.Meme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    memesList : List<Meme>
) {

    Column(modifier.fillMaxSize()) {

        val textState = remember {
            mutableStateOf(TextFieldValue(""))
        }

        SerachView(state = textState, placeholder = "Search here ...", modifier = modifier)

        val searchedText = textState.value.text

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(items = memesList.filter {
                it.name.contains(searchedText, ignoreCase = true)
            }, key = {it.id}){item ->
                MemeItem(
                    itemName = item.name,
                    itemUrl = item.url,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun MemeItem(
    modifier: Modifier = Modifier,
    itemName: String,
    itemUrl: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable {
                navController.navigate("DetailScreen?name=$itemName&url=$itemUrl")
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffc107)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = itemUrl,
                contentDescription = itemName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = itemName,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee()
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerachView(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    placeholder: String
) {
    TextField(value = state.value, onValueChange = {value->
        state.value = value
    },
        modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 10.dp, start = 10.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
        placeholder = {
            Text(text = placeholder)
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xffffc107)
        ),
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(color = Color.Black)
    )
}