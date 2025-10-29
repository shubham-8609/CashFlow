package com.codeleg.cashflow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeleg.cashflow.R
import com.codeleg.cashflow.databinding.ItemExpenseBinding
import com.codeleg.cashflow.model.ExpenseWithCategory
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseAdapter(private val onItemClick: (ExpenseWithCategory) -> Unit) :
    ListAdapter<ExpenseWithCategory, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding =
            ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ExpenseWithCategory) {
            binding.tvTitle.text = item.expense.title
            binding.tvAmount.text = "₹${item.expense.amount}"
            binding.tvCategory.text = item.category.name
            val iconResId = getCategoryIcon(item.category.name)
            binding.imgCategory.setImageResource(iconResId)
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val formattedDate = sdf.format(item.expense.date)
            binding.tvDate.text = formattedDate
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
        private fun getCategoryIcon(categoryName: String): Int {
            return when (categoryName.lowercase()) {
                "food" -> R.drawable.food
                "transport" -> R.drawable.transport
                "shopping" -> R.drawable.shopping
                "entertainment" -> R.drawable.entertainment
                "health" -> R.drawable.health
                "bills" -> R.drawable.bill
                "other" -> R.drawable.other
                else -> R.drawable.other
            }
        }

    }
}

class ExpenseDiffCallback : DiffUtil.ItemCallback<ExpenseWithCategory>() {
    override fun areItemsTheSame(oldItem: ExpenseWithCategory, newItem: ExpenseWithCategory): Boolean {
        return oldItem.expense.id == newItem.expense.id
    }

    override fun areContentsTheSame(oldItem: ExpenseWithCategory, newItem: ExpenseWithCategory): Boolean {
        return oldItem == newItem
    }
}
