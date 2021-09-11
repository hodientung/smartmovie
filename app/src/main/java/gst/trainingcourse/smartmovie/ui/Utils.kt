package gst.trainingcourse.smartmovie.ui

import android.app.Dialog
import android.content.Context
import android.view.Window
import gst.trainingcourse.smartmovie.R
import gst.trainingcourse.smartmovie.network.Resource
import kotlinx.android.synthetic.main.custom_layout.*





     fun showDialog(title: String, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val body = dialog.tvInfo
        body.text = title
        val reload = dialog.btnReload
        reload.setOnClickListener {
            //PopularFragment.newInstance()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun handleApiError(
        failure: Resource.Failure, context: Context?
    ) {
        when {
            failure.isNetworkError -> context?.let {
                showDialog(
                    "Load data failed,can't get data from server, please try again later",
                    it
                )
            }
            else -> {
                val error = failure.errorBody?.string().toString()
                if (context != null) {
                    showDialog(error, context)
                }
            }
        }
    }