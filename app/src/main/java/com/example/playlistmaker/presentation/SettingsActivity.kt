package com.example.playlistmaker.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.data.states.SearchState
import com.example.playlistmaker.data.states.SettingsState
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.presentation.viewmodels.search.SearchViewModel
import com.example.playlistmaker.presentation.viewmodels.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.arrowBackSettings.setOnClickListener {
            finish()
        }

        binding.imageShare.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }

        binding.imageTerm.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.support_user_agreement))
                startActivity(Intent.createChooser(this, null))
            }
        }

        binding.imageSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(Intent.createChooser(this, null))
            }
        }
        viewModel.getDarkTheme()

        binding.switchToDarkTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.setDarkTheme(checked)

        }

        viewModel.observeState().observe(this){
            render(it)
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
        (applicationContext as App).switchTheme(darkTheme)
    }

    private fun setSwitch(darkTheme: Boolean) {
        binding.switchToDarkTheme.setChecked(darkTheme)
        viewModel.setInit()
    }
}