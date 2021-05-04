package com.excalibur.basesword.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<BD : ViewDataBinding, A : AppCompatActivity> : Fragment(),View.OnClickListener {

    open fun isSetCustomColorStatusBar() = false

    lateinit var binding: BD

    val rootActivity by lazy {
        activity as A
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                getLayoutId(), container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        initBinding()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isSetCustomColorStatusBar())
            setStatusBarColor(getStatusBarColor(), isDarkText())
        super.onViewCreated(view, savedInstanceState)
    }

    fun setStatusBarColor(color: Int = Color.BLACK, state: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = rootActivity.window
            if (window != null) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                var newUiVisibility = window.decorView.systemUiVisibility
                newUiVisibility = if (state) {
                    //Dark Text to show up on your light status bar
                    newUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    //Light Text to show up on your dark status bar
                    newUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                }
                window.decorView.systemUiVisibility = newUiVisibility
                window.statusBarColor = color
            }
        }
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    @ColorInt
    open fun getStatusBarColor(): Int = Color.WHITE

    open fun isDarkText(): Boolean = true

    open fun initView() {

    }

    open fun initBinding() {

    }

}