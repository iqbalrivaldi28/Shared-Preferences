package com.example.firstappiguanaversion.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firstappiguanaversion.R
import com.example.firstappiguanaversion.data.UserModel
import com.example.firstappiguanaversion.databinding.ActivityFormUserPreferenceBinding
import com.example.firstappiguanaversion.databinding.ActivityMainBinding
import com.example.firstappiguanaversion.sharedpreference.UserPreference

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference

    private var isPreferenceEmpety = false
    private lateinit var userModel: UserModel

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.data != null && result.resultCode == FormUserPreferenceActivity.RESULT_CODE) {
            userModel =
                result.data?.getParcelableExtra<UserModel>(FormUserPreferenceActivity.EXTRA_RESULT) as UserModel
            populateView(userModel)
            checkForm(userModel)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "My User Preference"

        mUserPreference = UserPreference(this)

        showExistingPreference()

    }

    private fun showExistingPreference() {
        userModel = mUserPreference.getUser()
        populateView(userModel)
        checkForm(userModel)
    }


    private fun populateView(userModel: UserModel) {
        binding.tvName.text =
            if (userModel.name.toString().isEmpty()) "Tidak ada" else userModel.name
        binding.tvAge.text =
            if (userModel.age.toString().isEmpty()) "Tida Ada" else userModel.age.toString()
        binding.tvEmail.text =
            if (userModel.email.toString().isEmpty()) "Tidak ada" else userModel.email.toString()
        binding.tvPhone.text = if (userModel.phoneNumber.toString()
                .isEmpty()
        ) "Tidak ada" else userModel.phoneNumber.toString()
        binding.tvIsLoveMu.text = if (userModel.isLove) "Ya" else "Tidak"
    }

    private fun checkForm(userModel: UserModel) {
        when {
            userModel.name.toString().isEmpty() -> {
                binding.btnSave.text = getString(R.string.change)
                isPreferenceEmpety = false
            }

            else -> {
                binding.btnSave.text = getString(R.string.save)
                isPreferenceEmpety = true
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_save) {
            val intent = Intent(this@MainActivity, FormUserPreferenceActivity::class.java)
            when {
                isPreferenceEmpety -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM,
                        FormUserPreferenceActivity.TYPE_ADD
                    )
                    intent.putExtra("USER", userModel)
                }

                else -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM,
                        FormUserPreferenceActivity.TYPE_EDIT
                    )
                    intent.putExtra("USER", userModel)
                }
            }
            resultLauncher.launch(intent)
        }
    }


}