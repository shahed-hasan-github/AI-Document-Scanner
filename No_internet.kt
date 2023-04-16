package com.scan.android.documentscanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails


class No_internet : AppCompatActivity(), BillingProcessor.IBillingHandler  {


    lateinit var rlay: RelativeLayout
    private lateinit var bp: BillingProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)



        if (Check_Network.isInternetAvailable(this@No_internet)) {

            startActivity(Intent(this@No_internet, scan::class.java))


        } else {
            bp = BillingProcessor(
                    this,
                    getString(R.string.bill_key),
                    this
            )
            bp.initialize()
            bp.loadOwnedPurchasesFromGoogle();
            if (bp.isPurchased("docsn124")) {

                try {
                    startActivity(Intent(this@No_internet, scan::class.java))

//code that may throw exception
                } catch (e: Exception) {
//code that handles exception
                }

            }else {


                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
                rlay = findViewById(R.id.nointernet_lay)
                rlay.visibility = View.VISIBLE
            }
        }
    }

    fun rdrc_unlock_features(view: View) {
        startActivity(Intent(this@No_internet, Unlock::class.java))

    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {

    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {

    }

    override fun onBillingInitialized() {
    }
}