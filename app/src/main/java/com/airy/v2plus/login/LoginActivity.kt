package com.airy.v2plus.login

import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityLoginBinding
import com.airy.v2plus.util.showLong
import com.airy.v2plus.util.showShort

class LoginActivity : BaseActivity(){

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.loginKey.observe(this, Observer {
            Log.d(this::class.java.simpleName, it.toString())
            viewModel.requestVerifyBitmap(it.once)
        })

        viewModel.picBitmap.observe(this, Observer {
            binding.verifyCodeImage.setImageBitmap(it)
        })

        viewModel.loginResult.observe(this, Observer {
            if (it.problems.isNotEmpty()) {
                showLong(it.problems.toString())
            } else {
                showShort("Login Success")
                finish()
            }
        })
    }

}
