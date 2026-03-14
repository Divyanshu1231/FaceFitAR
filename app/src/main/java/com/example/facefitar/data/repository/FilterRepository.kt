package com.example.facefitar.data.repository

import com.example.facefitar.R
import com.example.facefitar.data.model.FilterModel

object FilterRepository {


    fun getFilters(): List<FilterModel> {

        return listOf(

            FilterModel(
                "Glasses",
                R.drawable.glasses_classic,
                R.drawable.glasses_classic
            ),

            FilterModel(
                "Rose Crown",
                R.drawable.rose_crown,
                R.drawable.rose_crown
            ),

            FilterModel(
                "Cat Ears",
                R.drawable.cat_ears,
                R.drawable.cat_ears
            ),

            FilterModel(
                "Dog Ears",
                R.drawable.dog_ears,
                R.drawable.dog_ears
            ),

            FilterModel(
                "Mask",
                R.drawable.mask_simple,
                R.drawable.mask_simple
            ),

            FilterModel(
                "Hearts",
                R.drawable.hearts_filter,
                R.drawable.hearts_filter
            ),

            FilterModel(
                "Stars",
                R.drawable.stars_filter,
                R.drawable.stars_filter
            )
        )
    }


}
