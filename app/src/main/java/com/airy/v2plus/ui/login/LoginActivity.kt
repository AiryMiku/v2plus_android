package com.airy.v2plus.ui.login

import android.graphics.drawable.Drawable
import androidx.activity.viewModels
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import com.airy.v2plus.R
import com.airy.v2plus.databinding.ActivityLoginBinding
import com.airy.v2plus.event.LoginEvent
import com.airy.v2plus.network.RequestHelper
import com.airy.v2plus.showToastLong
import com.airy.v2plus.showToastShort
import com.airy.v2plus.base.BaseActivity
import com.airy.v2plus.util.UserCenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var progressBar: ContentLoadingProgressBar
    private var hasRequestLoginKey: Boolean = false
    private lateinit var captchaImageListener: RequestListener<Drawable>

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
    }

    override val toolbarLabel: CharSequence? = "Login"
    override val displayHomeAsUpEnabled: Boolean? = true

    override fun initViews() {
        progressBar = binding.progressBar
        binding.submit.setOnClickListener {
            if (validateParams()) {
                progressBar.show()
                val userName = binding.userName.text.toString()
                val password = binding.password.text.toString()
                val verifyCode = binding.verifyCode.text.toString()
                viewModel.doLogin(userName, password, verifyCode)
                it.isEnabled = false
                showToastShort("Working on for your login...")
            }
        }

        captchaImageListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                showToastLong("Loading captcha failed, ${e?.message}")
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.progressBar.hide()
                makeSnackBarLong(binding.container, "Well, please try ~")
                return false
            }

        }

        binding.verifyCodeImage.setOnClickListener {
            progressBar.show()
            viewModel.requestLoginKey()
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

        viewModel.loginKey.observe(this, {
            if (hasRequestLoginKey) {
                binding.submit.isEnabled = true
            } else {
                hasRequestLoginKey = true
            }
            Glide.with(this)
                .load(RequestHelper.getCaptchaImageUrl(it.once))
                .dontAnimate()
                .listener(captchaImageListener)
                .placeholder(R.color.color_control_light)
                .into(binding.verifyCodeImage)
        })

        viewModel.loginResult.observe(this, {
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
                binding.submit.isEnabled = true
            } else {
                UserCenter.setUserName(it.userName)
                showToastShort("Login Success")
                EventBus.getDefault().post(LoginEvent())
                finish()
            }
        })

        viewModel.error.observe(this, {
            showToastLong(it.toString())
            binding.submit.isEnabled = true
        })
    }

}

