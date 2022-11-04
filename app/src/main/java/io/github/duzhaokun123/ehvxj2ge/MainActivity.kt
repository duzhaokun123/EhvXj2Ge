package io.github.duzhaokun123.ehvxj2ge

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class MainActivity : AppCompatActivity() {
    private val tmpFile: File = File.createTempFile("xjjjjj", ".db")

    private val openResponse = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it == null) {
            toast(getString(R.string.uc))
            return@registerForActivityResult
        }
        tmpFile.outputStream().use { out ->
            contentResolver.openInputStream(it)?.copyTo(out)
        }
        runCatching {
            SQLiteDatabase.openDatabase(tmpFile.path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS)
        }.onFailure {
            MaterialAlertDialogBuilder(this)
                .setMessage(getString(R.string.cantr))
                .show()
        }.onSuccess { db ->
            val version = db.version
            when {
                version <= 4 -> {
                    MaterialAlertDialogBuilder(this)
                        .setMessage(getString(R.string.v1, version))
                        .show()
                }
//                version == 5 -> {
//                    MaterialAlertDialogBuilder(this)
//                        .setMessage("version: 5\nsafe to back to 4")
//                        .setPositiveButton("do!") { _, _ ->
//                            do5back4(db)
//                        }.show()
//                }
                else -> {
                    MaterialAlertDialogBuilder(this)
                        .setMessage(getString(R.string.v2, version))
                        .setPositiveButton(getString(R.string.t)) { _, _ ->
                            set4andWrite(db)
                        }.show()
                }
            }
        }
    }

    private val writeResponse = registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) {
        if (it == null) {
            toast(getString(R.string.uc))
            return@registerForActivityResult
        }
        contentResolver.openOutputStream(it)?.use { out ->
            tmpFile.inputStream().copyTo(out)
        }
        toast(getString(R.string.done))
    }

    private fun set4andWrite(db: SQLiteDatabase) {
        db.version = 4
        db.close()
        writeResponse.launch("${System.currentTimeMillis()}.db")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener {
            openResponse.launch(arrayOf("application/octet-stream"))
        }
        @SuppressLint("SetTextI18n")
        findViewById<TextView>(R.id.tv_version).text = "version: ${BuildConfig.VERSION_NAME}"
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}