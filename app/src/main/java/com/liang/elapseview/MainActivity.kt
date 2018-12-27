package com.liang.elapseview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mock()
    }

    private fun mock() {
        val comments = mutableListOf<String>()
        for (i in 1..5) {
            comments.add("来自火星的男人: 这是我的第${i}条评论，不要打断哦")
        }
        val handler = android.os.Handler()
        Thread {
            while (comments.size > 0) {
                handler.post { ev_demo.updateText(comments.removeAt(0)) }
                Thread.sleep(2000L)
            }
        }.start()
    }
}
