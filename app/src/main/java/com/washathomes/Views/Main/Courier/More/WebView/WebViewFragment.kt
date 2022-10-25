package com.washathomes.Views.Main.Courier.More.WebView

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.washathomes.AppUtils.AppDefs.AppDefs
import com.washathomes.R
import com.washathomes.Views.Main.Courier.CourierMainActivity
import com.washathomes.databinding.FragmentWebView2Binding

class WebViewFragment : Fragment() {

    lateinit var binding: FragmentWebView2Binding
    lateinit var courierMainActivity: CourierMainActivity
    lateinit var navController: NavController
    val args: WebViewFragmentArgs by navArgs()
    var url = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_web_view2, container, false)
        binding = FragmentWebView2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CourierMainActivity) {
            courierMainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        onClick()
        setWebView()
    }

    private fun initViews(view: View){
        navController = Navigation.findNavController(view)
        url = args.url
    }

    private fun onClick(){
        binding.toolbarBackIcon.setOnClickListener { navController.popBackStack() }
    }

    private fun setWebView(){
        when (url) {
            resources.getString(R.string.faq_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.faq)
            }
            resources.getString(R.string.feedback_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.leave_feedback)
            }
            resources.getString(R.string.dispute_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.disputes)
            }
            resources.getString(R.string.transaction_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.transaction_history)
            }
            resources.getString(R.string.payout_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.payout)
            }
            resources.getString(R.string.ratings_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.my_ratings)
            }
            resources.getString(R.string.contact_url) -> {
                binding.toolbarTitle.text = resources.getString(R.string.contact_us)
            }
        }

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val url = request.url.toString()
                }
                return false
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }
        }

        val header: MutableMap<String, String> = HashMap()
        header["Authorization"] = AppDefs.user.token!!
        binding.webView.loadUrl(url, header)
    }

}