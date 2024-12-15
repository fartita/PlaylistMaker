package com.example.playlistmaker.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SettingsState
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.presentation.viewmodels.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding? = null
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(binding != null){
            binding!!.imageShare.setOnClickListener{
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                    type = "text/plain"
                    startActivity(Intent.createChooser(this, null))
                }
            }

            binding!!.imageTerm.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(getString(R.string.support_user_agreement))
                    startActivity(Intent.createChooser(this, null))
                }
            }

            binding!!.imageSupport.setOnClickListener {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                    startActivity(Intent.createChooser(this, null))
                }
            }
            viewModel.getDarkTheme()

            binding!!.switchToDarkTheme.setOnCheckedChangeListener { switcher, checked ->
                viewModel.setDarkTheme(checked)

            }

            viewModel.observeState().observe(requireActivity()){
                render(it)
            }
        }


    }

    private fun render(state: SettingsState) {
        when (state) {
            is SettingsState.GetDarkTheme -> setSwitch(state.isDarkTheme)
            is SettingsState.SetDarkTheme -> showTheme(state.isDarkTheme)
            SettingsState.Prepare -> {}
            }
        }

    private fun showTheme(darkTheme: Boolean) {
        (requireActivity().applicationContext as App).switchTheme(darkTheme)
    }

    private fun setSwitch(darkTheme: Boolean) {
        binding!!.switchToDarkTheme.setChecked(darkTheme)
        viewModel.setInit()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}