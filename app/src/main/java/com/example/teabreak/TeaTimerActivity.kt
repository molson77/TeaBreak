package com.example.teabreak

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import com.example.teabreak.data.Tea
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.data.getName
import com.example.teabreak.ui.theme.TeaBreakTheme
import com.example.teabreak.ui.theme.TeaTimerTheme

class TeaTimerActivity : ComponentActivity() {

    private var teaId: Int = 0
    private lateinit var teaType: TeaType

    companion object {
        const val TEA_ID = "tea_id"
        const val TEA_TYPE = "tea_type"
        fun createTimerActivityIntent(
            context: Context,
            tea: Tea
        ): Intent {
            return Intent(context, TeaTimerActivity::class.java).apply {
                putExtra(TEA_ID, tea.id)
                putExtra(TEA_TYPE, tea.type.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        teaId = intent.extras?.getInt(TEA_ID) ?: 0
        teaType = intent.extras?.getString(TEA_TYPE)?.let { TeaType.valueOf(it) } ?: TeaType.GREEN

        val backgroundColor = Utils.getTeaBackgroundColor(teaType)

        setContent {
            TeaTimerTheme(teaType = teaType) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeaBreakTheme {
        Greeting("Android")
    }
}