package com.example.paging3demoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.example.paging3demoapp.presentation.screens.home.HomeScreen
import com.example.paging3demoapp.presentation.screens.search.SearchScreen

@OptIn(ExperimentalCoilApi::class)
@Composable
@ExperimentalPagingApi
fun setUpNavGraph(navController: NavHostController){
   NavHost(navController = navController, startDestination = Screen.Home.route ){
       composable(route = Screen.Home.route){
          HomeScreen(navController = navController)
       }
       composable(route = Screen.Search.route){
           SearchScreen(navController = navController)
       }
   }
}