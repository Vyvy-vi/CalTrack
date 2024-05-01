package com.example.caltrack.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.caltrack.R

class FoodLogInputFragment : Fragment() {

    lateinit var editText: EditText
    private lateinit var dataListener: OnDataListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_food_log_input, container, false)
        editText = v.findViewById(R.id.quantity)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                dataListener.communicate(editText.text.toString())
            }
        })
        return  v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataListener=
            try {
                activity as OnDataListener
            }
            catch (e: ClassCastException){
                throw ClassCastException("$context must implement" + "communicate")
            }
    }
}