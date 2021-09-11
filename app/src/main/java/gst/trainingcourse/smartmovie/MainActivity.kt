package gst.trainingcourse.smartmovie

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import gst.trainingcourse.smartmovie.util.ConnectionType
import gst.trainingcourse.smartmovie.util.NetworkMonitorUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_layout_network.*

class MainActivity : AppCompatActivity() {
    private val networkMonitor = NetworkMonitorUtil(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())

        networkMonitor.result = { isAvailable, type ->
            runOnUiThread {
                when (isAvailable) {
                    true -> {
                        when (type) {
                            ConnectionType.Wifi -> {
                                Toast.makeText(this, "wifi is connected", Toast.LENGTH_LONG).show()
                            }
                            ConnectionType.Cellular -> {
                                Toast.makeText(this, "4G is connected", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                            }
                        }
                    }
                    false -> {
                        showDialogNetwork()
                    }
                }
            }
        }

    }

    private fun showDialogNetwork() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout_network)
        val body = dialog.tvSignature
        body?.text = getString(R.string.network)
        val reload = dialog.btnTryAgain
        reload?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

}