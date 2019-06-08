package com.escodro.category.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic [RecyclerView.ViewHolder] to be used in [RecyclerView.Adapter] and Data Binding.
 */
class BindingHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
