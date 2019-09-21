package com.airy.v2plus.login

import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airy.v2plus.R
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity(), View.OnClickListener{

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.dayNightSwitch.setOnClickListener {

        }
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
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.day_night_switch -> {
                
            }
        }
    }

}
