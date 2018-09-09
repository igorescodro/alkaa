package com.escodro.alkaa.common.databinding

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Generic [RecyclerView.ViewHolder] to be used in [RecyclerView.Adapter] and Data Binding.
 */
class BindingHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
