package com.airy.v2plus.login

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity(){

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Login"
        binding.verifyCodeImage.setOnClickListener {
            viewModel.requestLoginKey()
            makeToastShort("Please try it again~")
        }
        subscribeUI()

    }

    private fun subscribeUI() {

        viewModel.picBitmap.observe(this, Observer {
            binding.verifyCodeImage.setImageBitmap(it)
        })

        viewModel.loginResult.observe(this, Observer {
            if (it.problems.isNotEmpty()) {
                makeToastLong(it.problems.toString())
            } else {
                makeToastShort("Login Success")
                finish()
            }
        })
    }

}
