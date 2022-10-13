package com.example.paging3demoapp.presentation.screens.home

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.paging3demoapp.navigation.Screen
import com.example.paging3demoapp.presentation.common.HomeTopBar
import com.example.paging3demoapp.presentation.common.ListContent
import kotlinx.serialization.json.JsonNull.content

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val getAllImages = homeViewModel.getAllImages.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            HomeTopBar(
                onSearchClicked = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }, content = {

            ListContent(items = getAllImages, padding = it)
        }
    )

}