package com.cookiegames.smartcookie.settings.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cookiegames.smartcookie.AppTheme
import com.cookiegames.smartcookie.MainActivity
import com.cookiegames.smartcookie.R
import com.cookiegames.smartcookie.di.injector
import com.cookiegames.smartcookie.dialog.BrowserDialog
import com.cookiegames.smartcookie.extensions.resizeAndShow
import com.cookiegames.smartcookie.extensions.withSingleChoiceItems
import com.cookiegames.smartcookie.preference.UserPreferences
import javax.inject.Inject
import com.cookiegames.smartcookie.browser.ChooseNavbarCol
import com.cookiegames.smartcookie.browser.DrawerLineChoice
import com.cookiegames.smartcookie.browser.DrawerSizeChoice
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder




class DisplaySettingsFragment : AbstractSettingsFragment() {

    private lateinit var themeOptions: Array<String>

    @Inject internal lateinit var userPreferences: UserPreferences

    override fun providePreferencesXmlResource() = R.xml.preference_display

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        // preferences storage
        clickableDynamicPreference(
            preference = SETTINGS_THEME,
            summary = userPreferences.useTheme.toDisplayString(),
            onClick = ::showThemePicker
        )

        clickablePreference(
            preference = SETTINGS_TEXTSIZE,
            onClick = ::showTextSizePicker
        )

        clickablePreference(
                preference = SETTINGS_NAVBAR_COL,
                onClick = ::showColorPicker
        )

        switchPreference(
            preference = SETTINGS_HIDESTATUSBAR,
            isChecked = userPreferences.hideStatusBarEnabled,
            onCheckChange = { userPreferences.hideStatusBarEnabled = it }
        )

        switchPreference(
            preference = SETTINGS_FULLSCREEN,
            isChecked = userPreferences.fullScreenEnabled,
            onCheckChange = {userPreferences.fullScreenEnabled = it }
        )

        switchPreference(
                preference = SETTINGS_EXTRA,
                isChecked = userPreferences.showExtraOptions,
                onCheckChange = { userPreferences.showExtraOptions = it}
        )

        switchPreference(
            preference = SETTINGS_VIEWPORT,
            isChecked = userPreferences.useWideViewPortEnabled,
            onCheckChange = { userPreferences.useWideViewPortEnabled = it }
        )

        switchPreference(
            preference = SETTINGS_OVERVIEWMODE,
            isChecked = userPreferences.overviewModeEnabled,
            onCheckChange = { userPreferences.overviewModeEnabled = it }
        )

        switchPreference(
            preference = SETTINGS_REFLOW,
            isChecked = userPreferences.textReflowEnabled,
            onCheckChange = { userPreferences.textReflowEnabled = it }
        )

        switchPreference(
            preference = SETTINGS_BLACK_STATUS,
            isChecked = userPreferences.useBlackStatusBar,
            onCheckChange = { userPreferences.useBlackStatusBar = it }
        )

        switchPreference(
            preference = SETTINGS_DRAWERTABS,
            isChecked = userPreferences.showTabsInDrawer,
            onCheckChange = { userPreferences.showTabsInDrawer = it }
        )

        switchPreference(
            preference = SETTINGS_SWAPTABS,
            isChecked = userPreferences.bookmarksAndTabsSwapped,
            onCheckChange = { userPreferences.bookmarksAndTabsSwapped = it }
        )

        switchPreference(
                preference = SETTINGS_STARTPAGE,
                isChecked = userPreferences.startPageThemeEnabled,
                onCheckChange = { userPreferences.startPageThemeEnabled = it }
        )

        switchPreference(
                preference = SETTINGS_FOREGROUND,
                isChecked = userPreferences.tabsToForegroundEnabled,
                onCheckChange = { userPreferences.tabsToForegroundEnabled = it }
        )
        switchPreference(
                preference = SETTINGS_BOTTOM_BAR,
                isChecked = userPreferences.bottomBar,
                onCheckChange = {userPreferences.bottomBar = it; Toast.makeText(activity, R.string.please_restart, Toast.LENGTH_LONG).show()}
        )
        clickablePreference(
                preference = SETTINGS_LINES,
                onClick = ::showDrawerLines
        )

        clickablePreference(
                preference = SETTINGS_SIZE,
                onClick = ::showDrawerSize
        )
        /* switchPreference(
                preference = SETTINGS_WHATSNEW,
                isChecked = userPreferences.whatsNewEnabled,
                onCheckChange = {userPreferences.whatsNewEnabled = it}
        )
        clickablePreference(
                preference = SETTINGS_IMAGE_URL,
                onClick = ::showImageUrlPicker
        )*/
    }

    private fun showImageUrlPicker() {
        activity?.let {
            BrowserDialog.showEditText(it,
                    R.string.image_url,
                    R.string.hint_url,
                    userPreferences.imageUrlString,
                    R.string.action_ok) { s ->
                userPreferences.imageUrlString = s
            }
        }
    }

    private fun showColorPicker() {
        BrowserDialog.showCustomDialog(activity) {
            setTitle(R.string.navbar_col)
            val stringArray = resources.getStringArray(R.array.navbar_col)
            val values = ChooseNavbarCol.values().map {
                Pair(it, when (it) {
                    ChooseNavbarCol.NONE -> stringArray[0]
                    ChooseNavbarCol.COLOR -> stringArray[1]
                })
            }
            withSingleChoiceItems(values, userPreferences.navbarColChoice) {
                userPreferences.navbarColChoice = it
            }
            setPositiveButton(R.string.action_ok) { _, _ ->
                updateNavbarCol(userPreferences.navbarColChoice)
            }
        }

    }

    private fun showDrawerSize() {
        BrowserDialog.showCustomDialog(activity) {
            setTitle(R.string.drawer_size)
            val stringArray = resources.getStringArray(R.array.drawer_size)
            val values = DrawerSizeChoice.values().map {
                Pair(it, when (it) {
                    DrawerSizeChoice.AUTO -> stringArray[0]
                    DrawerSizeChoice.ONE -> stringArray[1]
                    DrawerSizeChoice.TWO -> stringArray[2]
                    DrawerSizeChoice.THREE -> stringArray[3]
                })
            }
            withSingleChoiceItems(values, userPreferences.drawerSize) {
                userPreferences.drawerSize = it
            }
            setPositiveButton(R.string.action_ok){_, _ ->
                Toast.makeText(activity, R.string.please_restart, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showDrawerLines() {
        BrowserDialog.showCustomDialog(activity) {
            setTitle(R.string.drawer_lines)
            val stringArray = resources.getStringArray(R.array.drawer_lines)
            val values = DrawerLineChoice.values().map {
                Pair(it, when (it) {
                    DrawerLineChoice.ONE -> stringArray[0]
                    DrawerLineChoice.TWO -> stringArray[1]
                    DrawerLineChoice.THREE -> stringArray[2]
                })
            }
            withSingleChoiceItems(values, userPreferences.drawerLines) {
                userPreferences.drawerLines = it
            }
            setPositiveButton(R.string.action_ok){_, _ ->
                Toast.makeText(activity, R.string.please_restart, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showNavbarColPicker(){
        var initColor = userPreferences.colorNavbar
        if(userPreferences.navbarColChoice == ChooseNavbarCol.NONE){
            initColor = 255
        }
        ColorPickerDialogBuilder
                .with(activity)
                .setTitle("Choose color")
                .initialColor(initColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener { /*selectedColor -> activity.toast("onColorSelected: 0x" + Integer.toHexString(selectedColor))*/ }
                .setPositiveButton("ok") { dialog, selectedColor, allColors -> userPreferences.colorNavbar = selectedColor }
                .setNegativeButton("cancel") { dialog, which -> }
                .build()
                .show()
    }

    private fun updateNavbarCol(choice: ChooseNavbarCol) {
        if (choice == ChooseNavbarCol.COLOR) {
            showNavbarColPicker()

        }

        userPreferences.navbarColChoice = choice
    }


    private fun showTextSizePicker() {
        val maxValue = 5
        AlertDialog.Builder(activity).apply {
            val layoutInflater = activity.layoutInflater
            val customView = (layoutInflater.inflate(R.layout.dialog_seek_bar, null) as LinearLayout).apply {
                val text = TextView(activity).apply {
                    setText(R.string.untitled)
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                addView(text)
                findViewById<SeekBar>(R.id.text_size_seekbar).apply {
                    setOnSeekBarChangeListener(TextSeekBarListener(text))
                    max = maxValue
                    progress = maxValue - userPreferences.textSize
                }
            }
            setView(customView)
            setTitle(R.string.title_text_size)
            setPositiveButton(android.R.string.ok) { _, _ ->
                val seekBar = customView.findViewById<SeekBar>(R.id.text_size_seekbar)
                userPreferences.textSize = maxValue - seekBar.progress
            }
        }.resizeAndShow()
    }


    private fun showThemePicker(summaryUpdater: SummaryUpdater) {
       val currentTheme = userPreferences.useTheme
        AlertDialog.Builder(activity).apply {
            setTitle(resources.getString(R.string.theme))
            val values = AppTheme.values().map { Pair(it, it.toDisplayString()) }
            withSingleChoiceItems(values, userPreferences.useTheme) {
                userPreferences.useTheme = it
                summaryUpdater.updateSummary(it.toDisplayString())
            }
            setPositiveButton(resources.getString(com.cookiegames.smartcookie.R.string.action_ok)) { _, _ ->
                if (currentTheme != userPreferences.useTheme) {
                    //activity.onBackPressed()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            setOnCancelListener {
                if (currentTheme != userPreferences.useTheme) {
                    activity.onBackPressed()
                }
            }
        }.resizeAndShow()

    }

    private fun AppTheme.toDisplayString(): String = getString(when (this) {
        AppTheme.LIGHT -> R.string.light_theme
        AppTheme.DARK -> R.string.dark_theme
        AppTheme.BLACK -> R.string.black_theme
        /*AppTheme.BLUE -> R.string.blue_theme
        AppTheme.GREEN -> R.string.green_theme
        AppTheme.YELLOW -> R.string.yellow_theme*/
    })

    private class TextSeekBarListener(
        private val sampleText: TextView
    ) : SeekBar.OnSeekBarChangeListener {

        override fun onProgressChanged(view: SeekBar, size: Int, user: Boolean) {
            this.sampleText.textSize = getTextSize(size)
        }

        override fun onStartTrackingTouch(arg0: SeekBar) {}

        override fun onStopTrackingTouch(arg0: SeekBar) {}

    }

    companion object {

        private const val SETTINGS_HIDESTATUSBAR = "fullScreenOption"
        private const val SETTINGS_FULLSCREEN = "fullscreen"
        private const val SETTINGS_VIEWPORT = "wideViewPort"
        private const val SETTINGS_OVERVIEWMODE = "overViewMode"
        private const val SETTINGS_REFLOW = "text_reflow"
        private const val SETTINGS_THEME = "app_theme"
        private const val SETTINGS_TEXTSIZE = "text_size"
        private const val SETTINGS_DRAWERTABS = "cb_drawertabs"
        private const val SETTINGS_SWAPTABS = "cb_swapdrawers"
        private const val SETTINGS_BLACK_STATUS = "black_status_bar"
        private const val SETTINGS_STARTPAGE = "startpage_theme"
        private const val SETTINGS_FOREGROUND = "new_tabs_foreground"
        private const val SETTINGS_EXTRA = "show_extra"
        private const val SETTINGS_BOTTOM_BAR = "bottom_bar"
        private const val SETTINGS_NAVBAR_COL = "navbar_col"
        private const val SETTINGS_LINES = "drawer_lines"
        private const val SETTINGS_SIZE = "drawer_size"
        private const val SETTINGS_WHATSNEW = "show_whats_new"
        private const val SETTINGS_IMAGE_URL = "image_url"



        private const val XX_LARGE = 30.0f
        private const val X_LARGE = 26.0f
        private const val LARGE = 22.0f
        private const val MEDIUM = 18.0f
        private const val SMALL = 14.0f
        private const val X_SMALL = 10.0f

        private fun getTextSize(size: Int): Float = when (size) {
            0 -> X_SMALL
            1 -> SMALL
            2 -> MEDIUM
            3 -> LARGE
            4 -> X_LARGE
            5 -> XX_LARGE
            else -> MEDIUM
        }
    }
}
