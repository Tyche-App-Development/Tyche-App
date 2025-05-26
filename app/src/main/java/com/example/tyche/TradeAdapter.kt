package com.example.tyche

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tyche.api.TradeItem

class TradeAdapter(private val trades: List<TradeItem>) :
    RecyclerView.Adapter<TradeAdapter.TradeViewHolder>() {

    class TradeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val symbol: TextView = view.findViewById(R.id.coin_symbol)
        val amountEur: TextView = view.findViewById(R.id.amount_eur)
        val amountCoin: TextView = view.findViewById(R.id.amount_coin)
        val pnl: TextView = view.findViewById(R.id.coin_pnl)
        val image: ImageView = view.findViewById(R.id.coin_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_card_item, parent, false)
        return TradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TradeViewHolder, position: Int) {
        val trade = trades[position]
        val asset = trade.symbol.removeSuffix("USDT").uppercase()

        holder.symbol.text = trade.symbol
        holder.amountEur.text = "$%.2f".format(trade.quoteQuantity)
        holder.amountCoin.text = "%.4f".format(trade.amount) + " $asset"
        holder.pnl.text = "%.2f".format(trade.gain_loss) + " USDT"
        holder.pnl.setTextColor(
            if (trade.gain_loss >= 0) 0xFF00FF00.toInt() else 0xFFFF4444.toInt()
        )

        val imageRes = when (asset) {
            "BTC" -> R.drawable.bitcoin_btc_logo
            "ETH" -> R.drawable.ethereum_eth_logo
            "XRP" -> R.drawable.xrp_logo
            "BNB" -> R.drawable.bnb_bnb_logo
            "SOL" -> R.drawable.solana_sol_logo
            else -> R.drawable.bnb_bnb_logo
        }

        holder.image.setImageResource(imageRes)
    }


    override fun getItemCount() = trades.size
}
