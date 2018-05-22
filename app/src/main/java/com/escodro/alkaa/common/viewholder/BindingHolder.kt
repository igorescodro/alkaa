package com.escodro.alkaa.common.viewholder

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Generic [RecyclerView.ViewHolder] to be used in [RecyclerView.Adapter] and Data Binding.
 *
 * @author Igor Escodro on 1/3/18.
 */
class BindingHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)
