package com.example.xiaochengshu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_create_introduction.*

class AddFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_create_introduction,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        addIntroductionButton.setOnClickListener {
//            val context: Context = view.context
//            val intent = Intent(context,CreateIntroductionActivity::class.java)
//            context.startActivity(intent)
//        }
        save.setOnClickListener {
            val intent = Intent(context,IntroductionActivity::class.java)
            startActivity(intent)
        }
    }

}
