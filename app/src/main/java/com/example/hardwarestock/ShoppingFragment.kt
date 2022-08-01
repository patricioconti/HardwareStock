package com.example.hardwarestock

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hardwarestock.databinding.FragmentShoppingBinding

/**
 * A simple [Fragment] subclass.
 */
class ShoppingFragment : Fragment() {


    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Go to Suppliers web site
        binding.buckleGuyAction.setOnClickListener {
            val queryUrl: Uri = Uri.parse("https://www.buckleguy.com/")
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            context?.startActivity(intent)

        }
        binding.ZipperStopAction.setOnClickListener {
            val queryUrl: Uri = Uri.parse("https://zipperstop.com/")
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            context?.startActivity(intent)
        }
        binding.ZipperThreadAction.setOnClickListener {
            val queryUrl: Uri = Uri.parse("https://zipperandthread.com/")
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            context?.startActivity(intent)
        }

    }
}