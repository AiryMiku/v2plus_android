package com.airy.v2plus.ui.login

import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityLoginBinding
import com.airy.v2plus.event.RequestUserInfoFromLoginEvent
import com.airy.v2plus.ui.base.BaseActivity
import com.airy.v2plus.util.UserCenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(){

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var progressBar: ContentLoadingProgressBar
    private var hasRequestLoginKey: Boolean = false

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Login"
        progressBar = binding.progressBar

        binding.submit.setOnClickListener {
            if (validateParams()) {
                progressBar.show()
                val userName = binding.userName.text.toString()
                val password = binding.password.text.toString()
                val verifyCode = binding.verifyCode.text.toString()
                viewModel.doLogin(userName, password, verifyCode)
            }
        }

        binding.verifyCodeImage.setOnClickListener {
            progressBar.show()
            viewModel.requestLoginKey()
            makeSnackBarLong(binding.container, "Loading login data, please wait for the verify code show up")
        }

        subscribeUI()
    }

    private fun validateParams(): Boolean {
        if (binding.userName.text.isNullOrBlank()) {
            binding.userName.error = "Must not be blank"
            return false
        }
        if (binding.password.text.isNullOrBlank()) {
            binding.password.error = "Must not be blank"
            return false
        }
        if (binding.verifyCode.text.isNullOrBlank()) {
            binding.verifyCode.error = "Must not be blank"
            return false
        }
        return true
    }

    private fun subscribeUI() {

        viewModel.loginKey.observe(this, Observer {
            if (hasRequestLoginKey) {
                makeToastShort("Well, please try again~")
            } else {
                hasRequestLoginKey = true
            }
        })

        viewModel.picBitmap.observe(this, Observer {
            progressBar.hide()
            binding.verifyCodeImage.setImageBitmap(it)
        })

        viewModel.loginResult.observe(this, Observer {
            progressBar.hide()
            if (it.problems.isNotEmpty()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Something Wrong")
                    .setMessage(it.problems.run {
                        var problems =""
                        it.problems.forEach { s->
                            problems += "$s\n"
                        }
                        problems })
                    .setPositiveButton("OK") { _, _->
                        viewModel.requestLoginKey()
                        progressBar.show()
                    }
                    .show()
            } else {
                UserCenter.setUserName(it.userName)
                makeToastShort("Login Success")
                EventBus.getDefault().post(RequestUserInfoFromLoginEvent())
                finish()
            }
        })

        viewModel.error.observe(this, Observer {
            makeToastLong(it.toString())
        })
    }

}
