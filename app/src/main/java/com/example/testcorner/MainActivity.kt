package com.example.testcorner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.heyalex.CornerDrawer
import com.github.heyalex.cornersheet.behavior.CornerSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CornerDrawer>(R.id.cd_playstory_menu_corner).setOnClickListener {

            if (findViewById<FrameLayout>(R.id.fl_cornermenu_header).visibility == View.VISIBLE) {
                openCornerMenu()

            }
            else {
                closeCornerMenu(false)
            }
        }
    }

    private fun openCornerMenu(){
        if (findViewById<FrameLayout>(R.id.fl_cornermenu_header).visibility == View.VISIBLE) {
            val behavior = BottomSheetBehavior.from(findViewById<CornerDrawer>(R.id.cd_playstory_menu_corner)) as CornerSheetBehavior<CornerDrawer>
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun closeCornerMenu(hide: Boolean){
        if (findViewById<ConstraintLayout>(R.id.cl_cornermenu_container).visibility == View.VISIBLE) {

            val behavior = BottomSheetBehavior.from(findViewById<CornerDrawer>(R.id.cd_playstory_menu_corner)) as CornerSheetBehavior<CornerDrawer>
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

            if (hide) {
                Handler().postDelayed({
                    runOnUiThread {
                        findViewById<CoordinatorLayout>(R.id.cl_playstory_menu_container).visibility =
                            View.GONE
                    }
                }, 100)
            }
        }
        else {
            if (hide)
                findViewById<CoordinatorLayout>(R.id.cl_playstory_menu_container).visibility =
                    View.GONE
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            // Standard Android full-screen functionality.
            window
                .decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}