package com.example.tyche

import StrategyInfo
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StrategiesAdapter(
    private val onStrategyClick: (StrategyInfo) -> Unit = {}
) : ListAdapter<StrategyInfo, StrategiesAdapter.StrategyViewHolder>(StrategyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrategyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_strategy_card, parent, false)
        return StrategyViewHolder(view)
    }

    override fun onBindViewHolder(holder: StrategyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StrategyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val botName: TextView = itemView.findViewById(R.id.bot_name)
        private val lastAction: TextView = itemView.findViewById(R.id.last_action)
        private val buyPrice: TextView = itemView.findViewById(R.id.buy_price)
        private val currentBalanceDetail: TextView = itemView.findViewById(R.id.current_balance_detail)
        private val tradingPair: TextView = itemView.findViewById(R.id.trading_pair)
        private val currentPrice: TextView = itemView.findViewById(R.id.current_price)
        private val priceChange: TextView = itemView.findViewById(R.id.price_change)
        private val coinImage: ImageView = itemView.findViewById(R.id.coin_image)
        private val buyPriceLayout: LinearLayout = itemView.findViewById(R.id.buy_price_layout)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onStrategyClick(getItem(position))
                }
            }
        }

        fun bind(strategy: StrategyInfo) {
            botName.text = strategy.botName

            val actionText = getActionText(strategy.position.lastAction)
            lastAction.text = itemView.context.getString(R.string.last_action_label, actionText)
            lastAction.setTextColor(getActionColor(strategy.position.lastAction))

            buyPriceLayout.visibility = View.VISIBLE
            buyPrice.text = itemView.context.getString(R.string.currency_format, formatCurrency(strategy.balance.initial))

            currentBalanceDetail.text = itemView.context.getString(R.string.currency_format, formatCurrency(strategy.balance.current))

            val profit = strategy.balance.current - strategy.balance.initial
            val balanceColor = if (profit >= 0) {
                Color.parseColor("#158B2A")
            } else {
                Color.parseColor("#892E2E")
            }
            currentBalanceDetail.setTextColor(balanceColor)

            tradingPair.text = strategy.trading.pair
            currentPrice.text = itemView.context.getString(R.string.currency_format, formatCurrency(strategy.trading.currentPrice))

            val priceChangeValue = strategy.trading.priceChange
            val changePrefix = if (priceChangeValue >= 0) "+" else "-"
            priceChange.text = itemView.context.getString(R.string.percentage_format, changePrefix, String.format("%.2f", priceChangeValue))
            priceChange.setTextColor(
                if (priceChangeValue >= 0){
                    Color.parseColor("#158B2A")
                } else {
                    Color.parseColor("#892E2E")
                }
            )


            setCoinImage(strategy.trading.pair)
        }

        private fun formatCurrency(value: Double): String {
            return String.format("%.2f", value)
        }

        private fun getActionText(action: String): String {
            return when (action.uppercase()) {
                "BUY" -> itemView.context.getString(R.string.action_buy)
                "SELL" -> itemView.context.getString(R.string.action_sell)
                "NONE" -> itemView.context.getString(R.string.action_none)
                else -> action
            }
        }

        private fun getActionColor(action: String): Int {
            return when (action.uppercase()) {
                "BUY" -> Color.parseColor("#158B2A")
                "SELL" -> Color.parseColor("#892E2E")
                "NONE" -> ContextCompat.getColor(itemView.context, android.R.color.white)
                else -> ContextCompat.getColor(itemView.context, android.R.color.white)
            }
        }

        private fun setCoinImage(pair: String) {
            val imageRes = when {
                pair.contains("BTC") -> R.drawable.bitcoin_btc_logo
                pair.contains("ETH") -> R.drawable.eth_logo
                pair.contains("XRP") -> R.drawable.xrp_logo
                pair.contains("SOL") -> R.drawable.solana_sol_logo
                pair.contains("BNB") -> R.drawable.bnb_bnb_logo
                else -> R.drawable.logo
            }
            coinImage.setImageResource(imageRes)
        }

    }

    class StrategyDiffCallback : DiffUtil.ItemCallback<StrategyInfo>() {
        override fun areItemsTheSame(oldItem: StrategyInfo, newItem: StrategyInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StrategyInfo, newItem: StrategyInfo): Boolean {
            return oldItem == newItem
        }
    }
}
