package com.hsc.qda.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.hsc.qda.R
import com.hsc.qda.data.network.NetworkClient
import com.hsc.qda.ui.export.ExportActivity
import com.hsc.qda.ui.retrieve.RetrieveActivity
import com.hsc.qda.utilities.UserPreferences
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var userData: UserPreferences
    private lateinit var networkClient: NetworkClient
    lateinit var availableTags: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        userData = UserPreferences(this)
        networkClient = NetworkClient()
        availableTags = userData.getTag(UserPreferences.AVAILABLE_TAG).toString()
        setupView()
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        retrieveBtn.setOnClickListener{
            val retrieveIntent = Intent(this, RetrieveActivity::class.java)
            startActivity(retrieveIntent)
        }

        exportBtn.setOnClickListener {
            val exportIntent = Intent(this, ExportActivity::class.java)
            startActivity(exportIntent)
        }

        addTagBtn.setOnClickListener {
            showEditTag()
        }
    }

    private fun showEditTag() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Tag Data")
        val input = EditText(this)
        val lp = LinearLayout.LayoutParams (
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.setText(availableTags)
        input.layoutParams = lp
        alertDialog.setView(input)
        alertDialog.setPositiveButton("OK") { _, _ ->
            availableTags = input.text.toString()
            userData.setTag(availableTags)
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.show()
    }
}