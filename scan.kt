package com.scan.android.documentscanner


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

import android.widget.Toast
import com.facebook.ads.*

import android.widget.LinearLayout









class scan : AppCompatActivity(), BillingProcessor.IBillingHandler {


    lateinit var imageView: ImageView
    lateinit var imageView2: LottieAnimationView
    lateinit var adimageView: ImageView
    lateinit var editText: EditText
    lateinit var bttn: Button
    lateinit var unlock: Button
    lateinit var slt: Button
    lateinit var dtc: Button
    lateinit var scrlv: ScrollView
    lateinit var svbtn: Button
    lateinit var svbtn2: Button

  lateinit var txtv: TextView
    lateinit var txt_ad: TextView
    lateinit var back: ImageView
    lateinit var txtr: TextView
    private val RECORD_REQUEST_CODE = 101
    private val TAG = "PermissionDemo"
    lateinit var menubtn1: ImageButton
    lateinit var menubtn2: ImageButton

    private val APP_ID = ""
    private val INT_ZONE_ID = ""
    private val BNR_ZONE_ID = ""
    lateinit var bnrshow: RelativeLayout
    var showButton: Button? = null
    private var adView: AdView? = null


    private lateinit var adview_lay: LinearLayout
    private lateinit var bp: BillingProcessor

    private var interstitialAd: InterstitialAd? = null



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan)

        AudienceNetworkAds.initialize(this);

        adView = AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50)

        val adContainer = findViewById<View>(R.id.banner_container) as LinearLayout

        adContainer.addView(adView)


        adView!!.loadAd()




            menubtn1 = findViewById(R.id.menubtn1)
        menubtn2 = findViewById(R.id.menubtn2)


        try {


            imageView = findViewById(R.id.imageView)
            imageView2 = findViewById(R.id.imageView2)
            adimageView = findViewById(R.id.adbtn)
            editText = findViewById(R.id.editText)
            scrlv = findViewById(R.id.scview)

            bttn = findViewById(R.id.bttn)
            unlock = findViewById(R.id.unclk)
            svbtn = findViewById(R.id.svbtn)
            svbtn2 = findViewById(R.id.svbtn2)


            txtv = findViewById(R.id.txtv_s)
            txtr = findViewById(R.id.txtr)
            txt_ad = findViewById(R.id.txt_ad)



            slt = findViewById(R.id.slt)
            dtc = findViewById(R.id.dtc)

            svbtn2.setOnClickListener() {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))




            }

            svbtn.setOnClickListener() {

                val txt: String = editText.getText().toString()
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody = txt
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, "Share via"))

            }


            setupPermissions()

        } catch (e: Exception) {


        }


        bp = BillingProcessor(
                this,
                getString(R.string.bill_key),
                this
        )
        bp.initialize()
        bp.loadOwnedPurchasesFromGoogle();
        if (bp.isPurchased("")) {

            try {
                unlock.visibility = View.GONE
                menubtn1.visibility = View.VISIBLE


//code that may throw exception
            } catch (e: Exception) {
//code that handles exception
            }

        } else {
        }


        adimageView.setOnClickListener() {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))
        }


    }
    //method
    override fun onDestroy() {
        if (adView != null) {
            adView!!.destroy()
        }
        super.onDestroy()
    }


    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RECORD_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val a = Intent(Intent.ACTION_MAIN)
                    a.addCategory(Intent.CATEGORY_HOME)
                    a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(a)

                    Toast.makeText(this, "Give Storage Permission", Toast.LENGTH_LONG).show()
                    slt.visibility = View.GONE


                } else {
                    slt.visibility = View.VISIBLE

                }
            }
        }
    }


    fun selectImage(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(data!!.data)
            imageView2.visibility = View.GONE
            adimageView.visibility = View.GONE
            txt_ad.visibility = View.GONE

//            adview_lay.visibility = View.GONE
        }
    }


    fun startRecognizing(v: View) {

        Toast.makeText(this, "Scanning File...", Toast.LENGTH_LONG).show()

        if (imageView.drawable != null) {
            editText.setText("")
            v.isEnabled = false
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

            detector.processImage(image)
                    .addOnSuccessListener { firebaseVisionText ->
                        v.isEnabled = true
                        processResultText(firebaseVisionText)
                        //Toast.makeText(this, "Detected", Toast.LENGTH_LONG).show()
                        editText.visibility = View.VISIBLE
                        bttn.visibility = View.VISIBLE
                        txtv.visibility = View.GONE
                        txtr.visibility = View.VISIBLE
                        svbtn.visibility = View.VISIBLE

                        scrlv.visibility = View.VISIBLE
                        unlock.visibility = View.GONE
                        slt.visibility = View.GONE
                        adimageView.visibility = View.GONE
                        txt_ad.visibility = View.GONE
                        dtc.visibility = View.GONE
                        imageView.visibility = View.GONE
                        menubtn2.visibility = View.GONE
                        menubtn1.visibility = View.GONE


                    }
                    .addOnFailureListener {
                        v.isEnabled = true
                        editText.setText("Failed")
                    }
        } else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_LONG).show()
        }


    }


    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            editText.setText("No Text Found")
            return
        }
        for (block in resultText.textBlocks) {
            val blockText = block.text
            editText.append(blockText + "\n")
        }
    }


    /*  override fun onBackPressed() {
          val a = Intent(Intent.ACTION_MAIN)
          a.addCategory(Intent.CATEGORY_HOME)
          a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
          startActivity(a)
      }*/

    fun rdrc_unlock(view: View) {


        val intent = Intent(this, Unlock::class.java)
        startActivity(intent)


    }



    override fun onProductPurchased(productId: String, details: TransactionDetails?) {

    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {

    }

    override fun onBillingInitialized() {

    }

    fun rdrc_home(view: View) {
        val intent = Intent(this, scan::class.java)
        startActivity(intent)

    }

    override fun onBackPressed() {
        //rdrc_ad()
        super.onBackPressed()
        startActivity(Intent(applicationContext, scan::class.java))
        onPause()
    }


    fun openmenu(view: android.view.View) {
        val popupMenu: PopupMenu = PopupMenu(this, menubtn1)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.twor ->
                    startActivity(Intent(this, Reader::class.java))


                R.id.fourr ->
                    startActivity(Intent(this, Count::class.java))


            }
            true
        })
        popupMenu.show()
    }

    fun openmenu2(view: android.view.View) {


        val popupMenu: PopupMenu = PopupMenu(this, menubtn2)
        popupMenu.menuInflater.inflate(R.menu.main_menu2, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.threer ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))
                R.id.fiver ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("")))


            }
            true
        })
        popupMenu.show()


    }

    fun again(view: android.view.View) {
        //rdrc_ad()
        startActivity(Intent(this, scan::class.java))

    }

}
