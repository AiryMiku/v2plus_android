package com.airy.v2plus.ui.topic

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.airy.v2plus.Common
import com.airy.v2plus.R
import com.airy.v2plus.model.official.Reply
import com.airy.v2plus.databinding.ActivityTopicDetailBinding
import com.airy.v2plus.showToastLong
import com.airy.v2plus.showToastShort
import com.airy.v2plus.ui.base.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// todo unknown crash
class TopicDetailActivity : BaseActivity(), TopicDetailAdapter.ViewOfItemOnClickListener {

    private lateinit var binding: ActivityTopicDetailBinding
    private val viewModel: TopicDetailViewModel by viewModels() //Todo: need refactor
    private lateinit var adapter: TopicDetailAdapter
    private var id: Long = 0L

    private val TAG = "TopicDetailActivity"

    override val toolbarLabel: CharSequence? = "Detail"
    override val displayHomeAsUpEnabled: Boolean? = true

    override fun setContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_topic_detail)
    }

    override fun initViews() {
        id = intent.getLongExtra(Common.KEY_ID.TOPIC_ID, 0L)

        adapter = TopicDetailAdapter(this, this)
        binding.list.adapter = adapter

        subscribeUI()
    }

    override fun loadData() {
        if (id != 0L) {
            viewModel.getTopicDetails(id)
            binding.refresh.isRefreshing = true
        } else {
            viewModel.error.value = IllegalArgumentException("Illegal topicId!")
        }
    }

    private fun subscribeUI() {
        viewModel.error.observe(this, {
            showToastLong(it.toString())
        })
        viewModel.topicDetails.observe(this, {
            adapter.submitList(it)
            binding.refresh.isRefreshing = false
            handlingReplyPositionItem()
        })
    }

    private fun handlingReplyPositionItem() {
        val replyNo = intent.getLongExtra(Common.KEY_ID.REPLY_NO, -1L)
        if (replyNo == -1L) {
            return
        }
        launch(Dispatchers.IO) {
            val itemPosition = replyNo.toInt()
            if (itemPosition <= adapter.itemCount) {
                val displayPosition = if (itemPosition - 1 > 0) itemPosition - 1 else itemPosition
                binding.list.scrollToPosition(displayPosition)
                delay(1000L)
                val itemView = binding.list.layoutManager?.findViewByPosition(itemPosition)
                withContext(Dispatchers.Main) {
                    shakeItemView(itemView)
                }
            }
        }
    }

    override fun onThankClickListener(reply: Reply) {
        makeSnackBarShort(binding.container, "Thanks for your admired❤, id: ${reply.id}")
    }

    override fun onReplyLongClickListener(reply: Reply): Boolean {
        val listDialog = MaterialAlertDialogBuilder(this)
        listDialog.setItems(R.array.reply_items) {
                _, which ->
            when (which) {
                0 -> showToastShort("Dev~")
                1 -> showToastShort("Dev~")
                2 -> showToastShort("HoHoHo~")
            }
        }
        listDialog.show()
        return true
    }

    private fun shakeItemView(view: View?) {
        view?.run {
            val scaleSmall = 0.9f
            val scaleLarge = 1.1f
            val shakeDegrees = 3f
            val duration = 100L
            val scaleAnim = ScaleAnimation(scaleSmall, scaleLarge, scaleSmall , scaleLarge)
            val rotateAnim = RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

            scaleAnim.duration = duration
            rotateAnim.duration = duration
            rotateAnim.repeatMode = Animation.REVERSE
            rotateAnim.repeatCount = 3
            
            val animSet = AnimationSet(false)
//            animSet.addAnimation(scaleAnim)
            animSet.addAnimation(rotateAnim)

            startAnimation(animSet)
        }
    }

}
